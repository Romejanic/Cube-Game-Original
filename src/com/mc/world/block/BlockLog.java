package com.mc.world.block;

public class BlockLog extends Block {

	public BlockLog(int id, String name) {
		super(id, name);
	}

	public int getTexture(Facing face) {
		switch(face) {
		case UP:
		case DOWN:
			return 5;
		default:
			return super.getTexture(face);
		}
	}

}