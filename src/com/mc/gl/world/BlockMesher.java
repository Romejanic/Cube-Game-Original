package com.mc.gl.world;

import java.util.ArrayList;

import com.mc.world.World;
import com.mc.world.block.Block;
import com.mc.world.block.BlockBush;
import com.mc.world.block.Facing;

public class BlockMesher {

	public static boolean generateBlockMesh(World world, Block block, int x, int y, int z, ArrayList<Float> vertices) {
		return generateBlockMesh(world, block, x, y, z, vertices, false);
	}

	private static boolean generateBlockMesh(World world, Block block, int x, int y, int z, ArrayList<Float> vertices, boolean overrideCustom) {
		if(!block.isRenderable()) {
			return false;
		}
		if(!overrideCustom) {
			if(block.id == Block.water.id) {
				return renderWater(world, x, y, z, vertices);
			} else if(block instanceof BlockBush) {
				return renderBush(world, block, x, y, z, vertices);
			}
		}
		int faceCount = 0;
		for(Facing face : Facing.values()) {
			if(block.isFaceOccluded(world, x, y, z, face)) {
				continue;
			}
			faceCount++;
			int tex = block.getTexture(face);
			float uMin = (float)(tex % 16) * 16f / 256f;
			float vMin = (float)(tex / 16) * 16f / 256f;
			float uMax = uMin + (16f/256f);
			float vMax = vMin + (16f/256f);
			switch(face) {
			case UP:				
				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));
				break;
			case DOWN:
				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));
				break;
			case LEFT:
				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));
				break;
			case RIGHT:
				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));
				break;
			case FRONT:
				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));
				break;
			case BACK:
				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)block.id);
				vertices.add((float)face.id);
				vertices.add((float)block.getWaveAmount(uMin, vMax));
				break;
			default:
				continue;
			}
		}
		return faceCount > 0;
	}

	private static boolean renderWater(World world, int x, int y, int z, ArrayList<Float> vertices) {
		if(world.getBlock(x, y + 1, z) == Block.water.id) {
			return generateBlockMesh(world, Block.water, x, y, z, vertices, true);
		}
		int faceCount = 0;
		for(Facing face : Facing.values()) {
			if(Block.water.isFaceOccluded(world, x, y, z, face)) {
				continue;
			}
			faceCount++;
			int tex = Block.water.getTexture(face);
			float uMin = (float)(tex % 16) * 16f / 256f;
			float vMin = (float)(tex / 16) * 16f / 256f;
			float uMax = uMin + (16f/256f);
			float vMax = vMin + (16f/256f);
			switch(face) {
			case UP:				
				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));
				break;
			case DOWN:
				vMin = vMax - (12f/256f);

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));
				break;
			case LEFT:
				vMin = vMax - (12f/256f);

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));
				break;
			case RIGHT:
				vMin = vMax - (12f/256f);

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));
				break;
			case FRONT:
				vMin = vMax - (12f/256f);

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z + 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));
				break;
			case BACK:
				vMin = vMax - (12f/256f);

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y + 0.25f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMin);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMin));

				vertices.add((float)x + 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMax);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMax, vMax));

				vertices.add((float)x - 0.5f);
				vertices.add((float)y - 0.5f);
				vertices.add((float)z - 0.5f);
				vertices.add(uMin);
				vertices.add(vMax);
				vertices.add((float)Block.water.id);
				vertices.add((float)face.id);
				vertices.add((float)Block.water.getWaveAmount(uMin, vMax));
				break;
			default:
				continue;
			}
		}
		return faceCount > 0;
	}
	
	private static boolean renderBush(World world, Block block, int x, int y, int z, ArrayList<Float> vertices) {
		if(!block.isRenderable()) {
			return false;
		}
		
		boolean surrounded = true;
		for(Facing face : Facing.values()) {
			if(!block.isFaceOccluded(world, x, y, z, face)) {
				surrounded = false;
				break;
			}
		}
		if(surrounded) {
			return false;
		}
		
		int tex = block.getTexture(Facing.UP);
		float uMin = (float)(tex % 16) * 16f / 256f;
		float vMin = (float)(tex / 16) * 16f / 256f;
		float uMax = uMin + (16f/256f);
		float vMax = vMin + (16f/256f);
		
		vertices.add((float)x - 0.5f);
		vertices.add((float)y + 0.5f);
		vertices.add((float)z - 0.5f);
		vertices.add(uMin);
		vertices.add(vMin);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(0f, 0f));
		
		vertices.add((float)x + 0.5f);
		vertices.add((float)y - 0.5f);
		vertices.add((float)z + 0.5f);
		vertices.add(uMax);
		vertices.add(vMax);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(1f, 1f));
		
		vertices.add((float)x + 0.5f);
		vertices.add((float)y + 0.5f);
		vertices.add((float)z + 0.5f);
		vertices.add(uMax);
		vertices.add(vMin);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(1f, 0f));
		
		//
		
		vertices.add((float)x - 0.5f);
		vertices.add((float)y + 0.5f);
		vertices.add((float)z - 0.5f);
		vertices.add(uMin);
		vertices.add(vMin);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(0f, 0f));
		
		vertices.add((float)x - 0.5f);
		vertices.add((float)y - 0.5f);
		vertices.add((float)z - 0.5f);
		vertices.add(uMin);
		vertices.add(vMax);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(0f, 1f));
		
		vertices.add((float)x + 0.5f);
		vertices.add((float)y - 0.5f);
		vertices.add((float)z + 0.5f);
		vertices.add(uMax);
		vertices.add(vMax);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(1f, 1f));
		
		//
		
		vertices.add((float)x - 0.5f);
		vertices.add((float)y + 0.5f);
		vertices.add((float)z + 0.5f);
		vertices.add(uMin);
		vertices.add(vMin);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(0f, 0f));
		
		vertices.add((float)x + 0.5f);
		vertices.add((float)y - 0.5f);
		vertices.add((float)z - 0.5f);
		vertices.add(uMax);
		vertices.add(vMax);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(1f, 1f));
		
		vertices.add((float)x + 0.5f);
		vertices.add((float)y + 0.5f);
		vertices.add((float)z - 0.5f);
		vertices.add(uMax);
		vertices.add(vMin);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(1f, 0f));
		
		//
		
		vertices.add((float)x - 0.5f);
		vertices.add((float)y + 0.5f);
		vertices.add((float)z + 0.5f);
		vertices.add(uMin);
		vertices.add(vMin);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(0f, 0f));
		
		vertices.add((float)x - 0.5f);
		vertices.add((float)y - 0.5f);
		vertices.add((float)z + 0.5f);
		vertices.add(uMin);
		vertices.add(vMax);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(0f, 1f));
		
		vertices.add((float)x + 0.5f);
		vertices.add((float)y - 0.5f);
		vertices.add((float)z - 0.5f);
		vertices.add(uMax);
		vertices.add(vMax);
		vertices.add((float)block.id);
		vertices.add((float)Facing.UP.id);
		vertices.add((float)block.getWaveAmount(1f, 1f));
		
		return true;
	}

}