package com.mc.gl.post.fx;

import com.mc.gl.objects.Shader;
import com.mc.gl.objects.Texture;
import com.mc.gl.post.PostEffect;
import com.mc.gl.post.PostProcessFBO;
import com.mc.gl.post.PostProcessing;
import com.mc.gl.post.PostProcessFBO.ColorMode;
import com.mc.gl.post.PostProcessFBO.DepthMode;
import com.mc.gl.post.fx.PostEffectBlur.BlurDirection;

public class PostEffectBloom extends PostEffect {

	private PostProcessFBO blurA;
	private PostProcessFBO blurB;
	private PostProcessFBO blurC;
	private PostProcessFBO blurD;
	private PostProcessFBO blurE;
	private PostEffectBlur blurH;
	private PostEffectBlur blurV;

	private Shader thresholdShader;
	private Shader blendShader;

	private Texture lensDirt;

	@Override
	public void init() {
		this.thresholdShader = Shader.get("post/bloom_threshold");
		this.blendShader = Shader.get("post/bloom_blend");
		this.lensDirt = Texture.get("lens_dirt_1");
		this.blendShader.uniform("lensDirt").set(this.lensDirt);
		this.blurH = new PostEffectBlur(BlurDirection.HORIZONTAL, 1);
		this.blurV = new PostEffectBlur(BlurDirection.VERTICAL, 1);
		this.blurH.init();
		this.blurV.init();
	}

	@Override
	public void preRender(PostProcessing post, int width, int height, Texture color, Texture depth) {
		this.checkResize(width, height);
		this.blurA.bind();
		this.thresholdShader.uniform("colorTex").set(color);
		this.thresholdShader.bind();
		post.drawQuad();
		this.thresholdShader.unbind();
		this.blurB.bind();
		this.blurH.render(post, this.blurB.getWidth(), this.blurB.getHeight(), this.blurA.getColorTexture(), depth);
		this.blurC.bind();
		this.blurV.render(post, this.blurC.getWidth(), this.blurC.getHeight(), this.blurB.getColorTexture(), depth);
		this.blurD.bind();
		this.blurH.render(post, this.blurD.getWidth(), this.blurD.getHeight(), this.blurA.getColorTexture(), depth);
		this.blurE.bind();
		this.blurV.render(post, this.blurE.getWidth(), this.blurE.getHeight(), this.blurD.getColorTexture(), depth);
		this.blurE.unbind();
	}

	@Override
	public void render(PostProcessing post, int width, int height, Texture color, Texture depth) {
		this.blendShader.uniform("colorTex").set(color);
		this.blendShader.uniform("blurTex").set(this.blurC.getColorTexture());
		this.blendShader.uniform("dirtTex").set(this.blurE.getColorTexture());
		this.blendShader.bind();
		post.drawQuad();
		this.blendShader.unbind();
	}

	private void checkResize(int w, int h) {
		int fbW  = w / 8;
		int fbH  = h / 8;
		int fbW2 = w / 32;
		int fbH2 = h / 32;
		this.blurA = PostProcessFBO.checkResize(this.blurA, w, h, ColorMode.LINEAR_TEX, DepthMode.NONE);
		this.blurB = PostProcessFBO.checkResize(this.blurB, fbW, fbH, ColorMode.LINEAR_TEX, DepthMode.NONE);
		this.blurC = PostProcessFBO.checkResize(this.blurC, fbW, fbH, ColorMode.LINEAR_TEX, DepthMode.NONE);
		this.blurD = PostProcessFBO.checkResize(this.blurD, fbW2, fbH2, ColorMode.LINEAR_TEX, DepthMode.NONE);
		this.blurE = PostProcessFBO.checkResize(this.blurE, fbW2, fbH2, ColorMode.LINEAR_TEX, DepthMode.NONE);
	}

	@Override
	public void destroy() {
		this.blurA.delete();
		this.blurB.delete();
		this.blurC.delete();
		this.blurD.delete();
		this.blurE.delete();
		this.blurH.destroy();
		this.blurV.destroy();
	}

}