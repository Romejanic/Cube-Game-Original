package com.mc.camera.hit;

import com.mc.world.block.Block;
import com.mc.world.block.Facing;

public class CameraHit {

	public final int x;
	public final int y;
	public final int z;
	public final Block block;
	public final Facing face;
	
	public CameraHit(int x, int y, int z, Block block, Facing face) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
		this.face = face;
	}
	
}