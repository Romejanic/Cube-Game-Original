package com.mc.world.gen;

import com.mc.world.World;
import com.mc.world.block.Block;

public class WorldGenerator {

	public static final float TREE_DENSITY = 0.0075f;
	public static final float GRASS_DENSITY = 0.1f;
	public static final float FLOWER_DENSITY = 0.01f;

	public void generate(World world, int width, int height, int depth) {
//		Thread t = new Thread("World Generator") {
//			public void run() {
				ThinMatrixHeights noise = new ThinMatrixHeights();
				float noiseScl = 8f;
				for(int x = 0; x < width; x++) {
					for(int z = 0; z < depth; z++) {
						int h = height/2 + (int)(noise.generateHeight((float)x/noiseScl, (float)z/noiseScl) * (float)height/4);
						for(int y = 0; y < height; y++) {
							if(y > h) {
								break;
							} else if(y == h) {
								world.setBlock(x, y, z, Block.grass.id);
							} else if(y > h - 3) {
								world.setBlock(x, y, z, Block.dirt.id);
							} else {
								world.setBlock(x, y, z, Block.stone.id);
							}
						}
					}
				}
				WorldGenerator.this.addWater(world, width, depth);
				WorldGenerator.this.generateFlowers(world, width, depth, GRASS_DENSITY, Block.tallGrass);
				WorldGenerator.this.generateFlowers(world, width, depth, FLOWER_DENSITY, Block.yellowFlower);
				WorldGenerator.this.generateFlowers(world, width, depth, FLOWER_DENSITY, Block.redFlower);
				WorldGenerator.this.generateTrees(world, width, depth, TREE_DENSITY);
//			}
//		};
//		t.start();
	}

	private void generateTrees(World world, int width, int depth, float density) {
		int count = (int)(((float)width*(float)depth)/(1f/density));
		for(int i = 0; i < count; i++) {
			int x = Perlin.RANDOM.nextInt(width);
			int z = Perlin.RANDOM.nextInt(depth);
			int y = world.getHeight(x, z);
			if(world.getBlock(x, y, z) != Block.grass.id && world.getBlock(x, y, z) != Block.dirt.id) {
				continue;
			}
			world.setBlock(x, y, z, Block.dirt.id);
			y++;

			int h = y + (4 + Perlin.RANDOM.nextInt(2));
			for(int y1 = y; y1 <= h; y1++) {
				world.setBlock(x, y1, z, Block.log.id);
			}

			// base layer
			world.setBlock(x - 1, h - 1, z, Block.leaves.id);
			world.setBlock(x + 1, h - 1, z, Block.leaves.id);
			world.setBlock(x, h - 1, z - 1, Block.leaves.id);
			world.setBlock(x, h - 1, z + 1, Block.leaves.id);
			// corners
			world.setBlock(x - 1, h - 1, z - 1, Block.leaves.id);
			world.setBlock(x + 1, h - 1, z + 1, Block.leaves.id);
			world.setBlock(x - 1, h - 1, z + 1, Block.leaves.id);
			world.setBlock(x + 1, h - 1, z - 1, Block.leaves.id);

			world.setBlock(x - 1, h - 2, z - 1, Block.leaves.id);
			world.setBlock(x + 1, h - 2, z - 1, Block.leaves.id);
			world.setBlock(x - 1, h - 2, z + 1, Block.leaves.id);
			world.setBlock(x + 1, h - 2, z + 1, Block.leaves.id);
			world.setBlock(x - 1, h - 2, z, Block.leaves.id);
			world.setBlock(x + 1, h - 2, z, Block.leaves.id);
			world.setBlock(x, h - 2, z - 1, Block.leaves.id);
			world.setBlock(x, h - 2, z + 1, Block.leaves.id);
			world.setBlock(x - 1, h - 3, z - 1, Block.leaves.id);
			world.setBlock(x + 1, h - 3, z - 1, Block.leaves.id);
			world.setBlock(x - 1, h - 3, z + 1, Block.leaves.id);
			world.setBlock(x + 1, h - 3, z + 1, Block.leaves.id);
			world.setBlock(x - 1, h - 3, z, Block.leaves.id);
			world.setBlock(x + 1, h - 3, z, Block.leaves.id);
			world.setBlock(x, h - 3, z - 1, Block.leaves.id);
			world.setBlock(x, h - 3, z + 1, Block.leaves.id);

			world.setBlock(x - 2, h - 2, z - 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x + 2, h - 2, z - 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x - 2, h - 2, z + 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x + 2, h - 2, z + 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x - 1, h - 2, z - 2, Block.leaves.id);
			world.setBlock(x + 1, h - 2, z - 2, Block.leaves.id);
			world.setBlock(x - 1, h - 2, z + 2, Block.leaves.id);
			world.setBlock(x + 1, h - 2, z + 2, Block.leaves.id);
			world.setBlock(x - 2, h - 2, z - 1, Block.leaves.id);
			world.setBlock(x + 2, h - 2, z - 1, Block.leaves.id);
			world.setBlock(x - 2, h - 2, z + 1, Block.leaves.id);
			world.setBlock(x + 2, h - 2, z + 1, Block.leaves.id);
			world.setBlock(x - 2, h - 2, z, Block.leaves.id);
			world.setBlock(x + 2, h - 2, z, Block.leaves.id);
			world.setBlock(x, h - 2, z - 2, Block.leaves.id);
			world.setBlock(x, h - 2, z + 2, Block.leaves.id);

			world.setBlock(x - 2, h - 3, z - 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x + 2, h - 3, z - 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x - 2, h - 3, z + 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x + 2, h - 3, z + 2, Perlin.RANDOM.nextBoolean() ? Block.leaves.id : 0);
			world.setBlock(x - 1, h - 3, z - 2, Block.leaves.id);
			world.setBlock(x + 1, h - 3, z - 2, Block.leaves.id);
			world.setBlock(x - 1, h - 3, z + 2, Block.leaves.id);
			world.setBlock(x + 1, h - 3, z + 2, Block.leaves.id);
			world.setBlock(x - 2, h - 3, z - 1, Block.leaves.id);
			world.setBlock(x + 2, h - 3, z - 1, Block.leaves.id);
			world.setBlock(x - 2, h - 3, z + 1, Block.leaves.id);
			world.setBlock(x + 2, h - 3, z + 1, Block.leaves.id);
			world.setBlock(x - 2, h - 3, z, Block.leaves.id);
			world.setBlock(x + 2, h - 3, z, Block.leaves.id);
			world.setBlock(x, h - 3, z - 2, Block.leaves.id);
			world.setBlock(x, h - 3, z + 2, Block.leaves.id);

			// top layer
			world.setBlock(x, h, z - 1, Block.leaves.id);
			world.setBlock(x, h, z + 1, Block.leaves.id);
			world.setBlock(x - 1, h, z, Block.leaves.id);
			world.setBlock(x + 1, h, z, Block.leaves.id);
			world.setBlock(x, h, z, Block.leaves.id);
		}
	}

	private void generateFlowers(World world, int width, int depth, float density, Block block) {
		int count = (int)(((float)width*(float)depth)/(1f/density));
		for(int i = 0; i < count; i++) {
			int x = Perlin.RANDOM.nextInt(width);
			int z = Perlin.RANDOM.nextInt(depth);
			int y = world.getHeight(x, z);
			byte b = world.getBlock(x, y, z);
			if(b != Block.grass.id && b != Block.dirt.id) {
				continue;
			}
			world.setBlock(x, y + 1, z, block.id);
		}
	}

	private void addWater(World world, int width, int depth) {
		int seaLevel = world.getHeight() / 2;
		int sandLevel = seaLevel - 4;
		for(int x = 0; x < width; x++) {
			for(int z = 0; z < depth; z++) {
				int y = world.getHeight(x, z);
				if(y < seaLevel) {
					if(y > sandLevel) {
						world.setBlock(x, y, z, Block.sand.id);
					} else if(y == sandLevel) {
						world.setBlock(x, y, z, Perlin.RANDOM.nextFloat() < 0.5f ? Block.sand.id : Block.dirt.id);
					} else if(y == sandLevel - 1) {
						world.setBlock(x, y, z, Perlin.RANDOM.nextFloat() < 0.15f ? Block.sand.id : Block.dirt.id);
					} else {
						world.setBlock(x, y, z, Block.dirt.id);
					}
					for(y++; y <= seaLevel; y++) {
						world.setBlock(x, y, z, Block.water.id);
					}
				}
			}
		}
	}

}