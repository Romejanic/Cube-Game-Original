package com.mc.world.block;

import com.mc.world.World;

public class BlockWater extends Block {

	private static final Facing[] ADJACENT_FACES = { Facing.FRONT, Facing.BACK, Facing.LEFT, Facing.RIGHT };
	
	public BlockWater(int id, String name) {
		super(id, name);
		this.setWaveAmount(1f);
		this.setTransparent();
		this.setRenderQueue(RenderQueue.TRANSPARENT);
	}

	@Override
	public boolean isFaceOccluded(World world, int x, int y, int z, Facing face) {
		if(face == Facing.UP) {
			if(world.getBlock(x, y + 1, z) == this.id) {
				return true;
			} else {
				for(Facing f : ADJACENT_FACES) {
					int tx = (int)((float)x + f.normal.x());
					int ty = y + 1;
					int tz = (int)((float)z + f.normal.z());
					if(world.getBlock(tx, ty, tz) == this.id) {
						return true;
					}
				}
				return false;
			}
		}
		int tx = (int)((float)x + face.normal.x());
		int ty = (int)((float)y + face.normal.y());
		int tz = (int)((float)z + face.normal.z());
		if(world.getBlock(tx, ty, tz) == this.id) {
			return true;
		}
		return super.isFaceOccluded(world, x, y, z, face);
	}

}