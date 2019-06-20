package com.mc.world.block;

public class BlockGrass extends Block {

	public BlockGrass(int id, String name) {
		super(id, name);
		this.setTexture(1);
	}

	public int getTexture(Facing face) {
		switch(face) {
		case DOWN:
			return Block.dirt.getTexture(face);
		case UP:
			return 2;
		default:
			return super.getTexture(face);
		}
	}
	
}