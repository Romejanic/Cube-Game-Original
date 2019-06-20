package com.mc.camera.hit;

import org.joml.Vector3f;

import com.mc.camera.Camera;
import com.mc.world.World;
import com.mc.world.block.Block;
import com.mc.world.block.Facing;

public class VoxelTracer {

	private Vector3f position = new Vector3f();
	public float maxDistance = 5f;
	
	@SuppressWarnings("unused")
	public CameraHit trace(Camera camera, World world) {
		Vector3f forward = camera.getForward();
		float maxDist = this.maxDistance*this.maxDistance;
		int prevX = (int)(this.position.x + 0.5f);
		int prevY = (int)(this.position.y + 0.5f);
		int prevZ = (int)(this.position.z + 0.5f);
		this.position.set(camera.position);
		while(camera.position.distanceSquared(this.position) < maxDist) {
			int blockX = (int)(this.position.x + 0.5f);
			int blockY = (int)(this.position.y + 0.5f);
			int blockZ = (int)(this.position.z + 0.5f);
			if(!world.isAir(blockX, blockY, blockZ)) {
				Block block = Block.blocks[world.getBlock(blockX, blockY, blockZ)];
				
				return new CameraHit(blockX, blockY, blockZ, block, Facing.UP);
			} else {
				this.position.add(forward);
				prevX = blockX;
				prevY = blockY;
				prevZ = blockZ;
			}
		}
		return null;
	}
	
}