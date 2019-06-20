package com.mc.gl.post.fx;

import org.joml.Vector2f;

import com.mc.gl.objects.Texture;
import com.mc.gl.post.PostProcessing;

public class PostEffectBlur extends PostEffectPass {

	public BlurDirection direction = BlurDirection.HORIZONTAL;
	public int downscale = 1;
	
	private final Vector2f screenSize = new Vector2f();
	private final Vector2f axis = new Vector2f();
	
	public PostEffectBlur() {
		super();
	}
	
	public PostEffectBlur(BlurDirection direction, int downscale) {
		super();
		this.direction = direction;
		this.downscale = downscale;
	}
	
	public String getShaderName() {
		return "post/blur";
	}

	public int getDownscaleX() { return this.downscale; }
	public int getDownscaleY() { return this.downscale; }
	
	@Override
	public void render(PostProcessing post, int width, int height, Texture color, Texture depth) {
		this.screenSize.set((float)width, (float)height);
		this.axis.set(direction.x, direction.y);
		
		this.getShader().uniform("screenSize").set(this.screenSize);
		this.getShader().uniform("axis").set(this.axis);
		super.render(post, width, height, color, depth);
	}

	@Override
	public void destroy() {}
	
	public enum BlurDirection {
		HORIZONTAL(1f, 0f),
		VERTICAL(0f, 1f);
		
		protected final float x;
		protected final float y;
		BlurDirection(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

}