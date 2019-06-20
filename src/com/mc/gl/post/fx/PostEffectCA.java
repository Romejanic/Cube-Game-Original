package com.mc.gl.post.fx;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mc.gl.objects.Texture;
import com.mc.gl.post.PostProcessing;

public class PostEffectCA extends PostEffectPass {

	private static final Vector3fc DEFAULT_REFRACTION = new Vector3f(1.0f, 1.015f, 1.03f).toImmutable();
	public final Vector3fc refractiveIndex;
	public float intensity = 0.25f;
	
	public PostEffectCA() {
		this(DEFAULT_REFRACTION);
	}
	
	public PostEffectCA(Vector3fc refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}
	
	@Override
	public String getShaderName() {
		return "post/ca";
	}
	
	@Override
	public void render(PostProcessing post, int w, int h, Texture color, Texture depth) {
		this.getShader().uniform("refractiveIndex").set(this.refractiveIndex);
		this.getShader().uniform("colorTex").set(color);
		this.getShader().uniform("intensity").set(this.intensity);
		this.getShader().bind();
		post.drawQuad();
		this.getShader().unbind();
	}
	
}