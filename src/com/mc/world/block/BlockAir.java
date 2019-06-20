package com.mc.world.block;

public class BlockAir extends Block {

	public BlockAir(int id) {
		super(id, "air");
	}

	@Override
	public boolean isRenderable() {
		return false;
	}
	
}