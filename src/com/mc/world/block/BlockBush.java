package com.mc.world.block;

public class BlockBush extends Block {

	public BlockBush(int id, String name) {
		super(id, name);
		this.setTransparent();
		this.setRenderQueue(RenderQueue.NO_CULL);
	}
	
	@Override
	public float getWaveAmount(float u, float v) {
		return super.getWaveAmount(u, v) * (1f - v);
	}
	
}