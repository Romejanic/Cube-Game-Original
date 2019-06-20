package com.mc.gl.post;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

import com.mc.gl.objects.Texture;
import com.mc.utils.Utils;

public class PostProcessFBO {

	private final int fbo;
	private final int width;
	private final int height;

	private int colorTex;
	private int depthTex;
	private int depthBuf;

	private Texture colorTexUniform;
	private Texture depthTexUniform;

	public PostProcessFBO(int width, int height) {
		this.fbo = glGenFramebuffers();
		this.width = width;
		this.height = height;
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		glDrawBuffers(Utils.store(GL_NONE));
	}

	public PostProcessFBO addColorTexture(boolean hdr, boolean linear) {
		this.colorTex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.colorTex);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, linear ? GL_LINEAR : GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, linear ? GL_LINEAR : GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, hdr ? GL_RGB32F : GL_RGB, this.width, this.height, 0, GL_RGB, hdr ? GL_FLOAT : GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.colorTex, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		this.colorTexUniform = Texture.from2D(this.hashCode() + "_color", this.colorTex);
		glDrawBuffers(Utils.store(GL_COLOR_ATTACHMENT0));
		return this;
	}

	public PostProcessFBO addDepthTexture() {
		this.depthTex = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.depthTex);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT16, this.width, this.height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, this.depthTex, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		this.depthTexUniform = Texture.from2D(this.hashCode() + "_depth", this.depthTex);
		return this;
	}

	public PostProcessFBO addDepthBuffer() {
		this.depthBuf = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, this.depthBuf);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, this.width, this.height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, this.depthBuf);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		return this;
	}

	public PostProcessFBO bind() {
		return this.bind(false);
	}

	public PostProcessFBO bind(boolean bindOnly) {
		glBindFramebuffer(GL_FRAMEBUFFER, this.fbo);
		if(!bindOnly) {
			glViewport(0, 0, this.width, this.height);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		}
		return this;
	}

	public PostProcessFBO unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Texture getColorTexture() {
		return this.colorTexUniform;
	}

	public Texture getDepthTexture() {
		return this.depthTexUniform;
	}

	public void delete() {
		glDeleteFramebuffers(this.fbo);
		if(this.colorTex > 0) {
			Texture.deleteTexture(this.colorTexUniform.getName());
		}
		if(this.depthTex > 0) {
			Texture.deleteTexture(this.depthTexUniform.getName());
		}
		if(this.depthBuf > 0) {
			glDeleteRenderbuffers(this.depthBuf);
		}
	}
	
	public static PostProcessFBO checkResize(PostProcessFBO fbo, int w, int h, ColorMode colorMode, DepthMode depthMode) {
		if(fbo != null && (fbo.getWidth() != w || fbo.getHeight() != h)) {
			fbo.delete();
			fbo = null;
		}
		if(fbo == null) {
			fbo = new PostProcessFBO(w, h);
			if(colorMode != null && colorMode != ColorMode.NONE) {
				fbo.addColorTexture(true, colorMode == ColorMode.LINEAR_TEX);
			}
			if(depthMode == DepthMode.TEXTURE) {
				fbo.addDepthTexture();
			} else if(depthMode == DepthMode.BUFFER) {
				fbo.addDepthBuffer();
			}
		}
		return fbo;
	}

	public enum ColorMode { NONE, LINEAR_TEX, NEAREST_TEX }
	public enum DepthMode { NONE, TEXTURE, BUFFER }
	
}