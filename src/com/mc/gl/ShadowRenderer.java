package com.mc.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

import org.joml.Matrix4f;

import com.mc.camera.Camera;
import com.mc.camera.IView;
import com.mc.gl.objects.Shader;
import com.mc.gl.objects.Texture;
import com.mc.gl.world.SunLight;
import com.mc.utils.Mathf;

public class ShadowRenderer implements IView {
	
	public static final int SHADOW_RES = 4096;
	public static final float SHADOW_DST = 70f;
	
	private int fbo;
	private int shadowmap;
	
	private Matrix4f projMat = new Matrix4f();
	private Matrix4f viewMat = new Matrix4f();
	
	private Texture shadowTexture;
	
	public ShadowRenderer() {
		this.fbo = glGenFramebuffers();
		this.shadowmap = glGenTextures();
		
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glBindTexture(GL_TEXTURE_2D, this.shadowmap);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, SHADOW_RES, SHADOW_RES, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.shadowmap, 0);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glDrawBuffers(new int[]{ GL_NONE });
		
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		if(status != GL_FRAMEBUFFER_COMPLETE) {
			throw new RuntimeException("Shadow framebuffer is incomplete! (status " + status + ")");
		}
		
		this.shadowTexture = Texture.from2D("shadowmap", this.shadowmap);
		
		 // if shadow distance ever changes, update this every frame
		this.projMat.identity().ortho(-SHADOW_DST, SHADOW_DST, -SHADOW_DST, SHADOW_DST, -SHADOW_DST * 4f, SHADOW_DST * 4f);
	}
	
	public void startDrawing(Camera camera, SunLight light) {
		float camX = camera.position.x + Mathf.sin(Mathf.rad(camera.rotation.y)) * 5f;
		float camZ = camera.position.z + Mathf.cos(Mathf.rad(camera.rotation.y)) * 5f;
		float camY = camera.position.y;
		this.viewMat.identity().lookAt(camX + light.direction.x, camY + light.direction.y, camZ + light.direction.z, camX, camY, camZ, 0f, 1f, 0f, this.viewMat);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glViewport(0, 0, SHADOW_RES, SHADOW_RES);
		glColorMask(false, false, false, false);
		glClear(GL_DEPTH_BUFFER_BIT);
	}
	
	public void stopDrawing() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glColorMask(true, true, true, true);
	}
	
	public Texture getShadowmap() {
		return this.shadowTexture;
	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projMat;
	}
	
	public Matrix4f getViewMatrix() {
		return this.viewMat;
	}
	
	public void uploadUniforms(Shader shader, boolean isShadowPass) {
		shader.uniform("shadowProj").set(getProjectionMatrix());
		shader.uniform("shadowView").set(getViewMatrix());
		if(!isShadowPass) {
			shader.uniform("shadowDist").set(SHADOW_DST);
			shader.uniform("shadowmapSize").set(SHADOW_RES);
			shader.uniform("shadowmap").set(getShadowmap());
		}
	}
	
	public void delete() {
		glDeleteFramebuffers(this.fbo);
		Texture.deleteTexture(this.shadowTexture.getName());
	}
	
}