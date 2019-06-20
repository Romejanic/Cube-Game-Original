package com.mc.world.block;

public enum RenderQueue {

	NORMAL(),
	NO_CULL(),
	TRANSPARENT(false);
	
	private boolean drawShadows;
	
	RenderQueue() {
		this(true);
	}
	
	RenderQueue(boolean drawShadows) {
		this.drawShadows = drawShadows;
	}
	
	public boolean shouldDrawShadows() {
		return this.drawShadows;
	}
	
}