package com.mc.gl.post;

import com.mc.gl.objects.Texture;

public abstract class PostEffect {

	public abstract void init();
	public abstract void render(PostProcessing post, int width, int height, Texture color, Texture depth);
	public abstract void destroy();
	
	public void preRender(PostProcessing post, int width, int height, Texture color, Texture depth) {}
	public int getDownscaleX() { return 1; }
	public int getDownscaleY() { return 1; }
	
}