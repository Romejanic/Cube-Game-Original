package com.mc.gl;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector3f;

import com.mc.camera.Camera;
import com.mc.camera.Frustum;
import com.mc.gl.mesh.Mesher;
import com.mc.gl.models.EntityModel;
import com.mc.gl.objects.GlJobs;
import com.mc.gl.objects.Shader;
import com.mc.gl.objects.Texture;
import com.mc.gl.post.PostProcessFBO;
import com.mc.gl.post.PostProcessFBO.ColorMode;
import com.mc.gl.post.PostProcessFBO.DepthMode;
import com.mc.gl.post.PostProcessing;
import com.mc.gl.world.Chunk;
import com.mc.gl.world.EntityRenderer;
import com.mc.gl.world.Skybox;
import com.mc.gl.world.SunLight;
import com.mc.gl.world.WorldRenderer;
import com.mc.world.World;

public class MasterRenderer {
	
	public static final float RESOLUTION_SCALE = 1f;
	
	private WorldRenderer worldRenderer;
	private EntityRenderer entityRenderer;
	private Skybox skybox;
	private ShadowRenderer shadowmap;
	private SunLight sunLight;
	private PostProcessing postFX;
	
	private Frustum frustum = new Frustum();
	private PostProcessFBO fbo;

	public final GlJobs jobQueue = new GlJobs();
	
	private float last;
	
	public void init(World world) {
		glClearColor(0f, 0f, 0f, 1f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glDepthFunc(GL_LEQUAL);
		glCullFace(GL_BACK);
		
		this.worldRenderer = new WorldRenderer(world);
		this.entityRenderer = new EntityRenderer(world);
		this.skybox = new Skybox();
		this.shadowmap = new ShadowRenderer();
		this.sunLight = new SunLight(new Vector3f(1f, 1f, 1f));
		this.postFX = new PostProcessing();
		
		this.postFX.init();
		
		System.out.println("=== OpenGL Info ===");
		System.out.println("Version:  " + glGetString(GL_VERSION));
		System.out.println("Vendor:   " + glGetString(GL_VENDOR));
		System.out.println("GPU Name: " + glGetString(GL_RENDERER));
	}
	
	public void render(Camera camera, World world, int w, int h) {
		if(!Window.hasFocus()) {
			return;
		}
		this.jobQueue.executeJobs();
		
		int trueW = w, trueH = h;
		w = (int)((float)w * RESOLUTION_SCALE);
		h = (int)((float)h * RESOLUTION_SCALE);
		
		this.sunLight.updateSunLight(world);
		this.worldRenderer.preRender(camera, this.sunLight);
		this.entityRenderer.preRender(camera, this.sunLight);
		
		this.shadowmap.startDrawing(camera, this.sunLight);
		this.frustum.calculateFrustum(this.shadowmap);
		this.worldRenderer.cullChunks(this.frustum);
		this.worldRenderer.render(camera, this.shadowmap, true);
		this.entityRenderer.renderEntities(camera, this.frustum, this.shadowmap, true);
		this.shadowmap.stopDrawing();
		glFlush();
		
		this.fbo = PostProcessFBO.checkResize(this.fbo, w, h, ColorMode.NEAREST_TEX, DepthMode.TEXTURE);
		
		this.fbo.bind(true);
		glViewport(0, 0, w, h);
		glClear(GL_DEPTH_BUFFER_BIT);
		this.skybox.render(camera, this.sunLight);
		this.frustum.calculateFrustum(camera);
		this.worldRenderer.cullChunks(this.frustum);
		this.entityRenderer.renderEntities(camera, this.frustum, this.shadowmap, false);
		this.worldRenderer.render(camera, this.shadowmap, false);
		this.fbo.unbind();
		glFlush();
		
		this.postFX.draw(w, h, trueW, trueH, this.fbo.getColorTexture(), this.fbo.getDepthTexture());
		
		float time = Window.getTime();
		if((time-last) >= 1f) {
			last = time;
			Window.setTitle((1f/Window.getDelta()) + "fps, " + Chunk.chunkUpdates + " chunk updates (" + w + "x" + h + ")");
			Chunk.chunkUpdates = 0;
		}
		
		if(Window.isKeyPressed(Window.KEY_R)) {
			Shader.deleteAll();
			System.out.println("Recompiled shaders");
		}
		
		glFlush();
	}
	
	public void destroy() {
		this.worldRenderer.delete();
		this.skybox.delete();
		this.shadowmap.delete();
		this.postFX.destroy();
		this.fbo.delete();
		Shader.deleteAll();
		Texture.deleteAll();
		Mesher.deleteAll();
		EntityModel.deleteAll();
	}
	
	public WorldRenderer getWorldRenderer() {
		return this.worldRenderer;
	}
	
}