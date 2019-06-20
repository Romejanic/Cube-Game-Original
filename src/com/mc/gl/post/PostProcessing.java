package com.mc.gl.post;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;

import com.mc.gl.mesh.Mesh;
import com.mc.gl.mesh.Mesher;
import com.mc.gl.objects.Texture;
import com.mc.gl.post.PostProcessFBO.ColorMode;
import com.mc.gl.post.PostProcessFBO.DepthMode;
import com.mc.gl.post.fx.PostEffectACES;
import com.mc.gl.post.fx.PostEffectBloom;
import com.mc.gl.post.fx.PostEffectCA;
import com.mc.gl.post.fx.PostEffectPass;

public class PostProcessing {

	private static final ArrayList<Class<? extends PostEffect>> effectTypes = new ArrayList<Class<? extends PostEffect>>();
	private static final boolean DEBUG_SKIP_POST_FX = false;

	private Mesh quadMesh;
	private ArrayList<PostEffect> effects = new ArrayList<PostEffect>();

	private PostProcessFBO bufferA;
	private PostProcessFBO bufferB;
	private boolean bufferBActive = false;
	private PostEffectPass passEffect;

	public void init() {
		this.quadMesh = Mesher.getQuad();
		this.passEffect = new PostEffectPass();
		this.passEffect.init();

		if(!DEBUG_SKIP_POST_FX) {
			for(Class<? extends PostEffect> effect : effectTypes) {
				try {
					this.effects.add(effect.newInstance());
				} catch (Exception e) {
					System.err.println("Failed to create post effect instance of " + effect.getName());
					e.printStackTrace();
				}
			}
			for(PostEffect effect : this.effects) {
				effect.init();
			}
		}
	}

	public void draw(int w, int h, int ow, int oh, Texture color, Texture depth) {
		this.quadMesh.bind();
		glDisable(GL_DEPTH_TEST);
		if(!DEBUG_SKIP_POST_FX) {
			this.bufferA = PostProcessFBO.checkResize(this.bufferA, w, h, ColorMode.NEAREST_TEX, DepthMode.NONE);
			this.bufferB = PostProcessFBO.checkResize(this.bufferB, w, h, ColorMode.NEAREST_TEX, DepthMode.NONE);
			for(int i = 0; i < this.effects.size(); i++) {
				Texture colorTex = i == 0 ? color : this.getOtherBuffer().getColorTexture();
				PostEffect effect = this.effects.get(i);
				effect.preRender(this, w / effect.getDownscaleX(), h / effect.getDownscaleY(), colorTex, depth);
				this.getActiveBuffer().bind();
				effect.render(this, w / effect.getDownscaleX(), h / effect.getDownscaleY(), colorTex, depth);
				this.getActiveBuffer().unbind();
				this.swapBuffers();
			}
		}
		glViewport(0, 0, ow, oh);
		glClear(GL_COLOR_BUFFER_BIT);
		this.passEffect.render(this, w, h, this.getOtherBuffer().getColorTexture(), depth);
		glEnable(GL_DEPTH_TEST);
		this.quadMesh.unbind();
	}

	private PostProcessFBO getActiveBuffer() {
		return this.bufferBActive ? this.bufferB : this.bufferA;
	}

	private PostProcessFBO getOtherBuffer() {
		return this.bufferBActive ? this.bufferA : this.bufferB;
	}

	private void swapBuffers() {
		this.bufferBActive = !this.bufferBActive;
	}

	public void drawQuad() {
		this.quadMesh.draw();
	}

	public void destroy() {
		this.passEffect.destroy();
		if(!DEBUG_SKIP_POST_FX) {
			for(PostEffect effect : this.effects) {
				effect.destroy();
			}
			this.bufferA.delete();
			this.bufferB.delete();
		}
	}

	static {
		effectTypes.add(PostEffectCA.class);
		effectTypes.add(PostEffectBloom.class);
		effectTypes.add(PostEffectACES.class);
//		effectTypes.add(PostEffectInvert.class);
	}

}