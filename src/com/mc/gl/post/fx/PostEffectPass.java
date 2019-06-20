package com.mc.gl.post.fx;

import com.mc.gl.objects.Shader;
import com.mc.gl.objects.Texture;
import com.mc.gl.post.PostEffect;
import com.mc.gl.post.PostProcessing;

public class PostEffectPass extends PostEffect {

	private Shader shader;
	
	@Override
	public void init() {
		this.shader = Shader.get(this.getShaderName());
	}

	public String getShaderName() {
		return "post/pass";
	}
	
	public Shader getShader() {
		return this.shader;
	}
	
	@Override
	public void render(PostProcessing post, int width, int height, Texture color, Texture depth) {
		this.shader.uniform("colorTex").set(color);
		this.shader.uniform("depthTex").set(depth);
		this.shader.bind();
		post.drawQuad();
		this.shader.unbind();
	}

	@Override
	public void destroy() {}

}