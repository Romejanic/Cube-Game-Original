package com.mc.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.mc.physics.AABB;
import com.mc.world.block.Block;
import com.mc.world.entities.Entity;
import com.mc.world.entities.EntityRegistry;
import com.mc.world.gen.WorldGenerator;

public class World {

	public static final boolean ENABLE_SAVING = false;
	public static final String SAVE_PATH = "level.cubes";
	
	private static final WorldGenerator WORLD_GEN = new WorldGenerator();
	
	private byte[] blocks;
	private int width, height, depth;
	private float time;
	private ArrayList<Entity> entities;
	
	private ArrayList<WorldListener> listeners = new ArrayList<WorldListener>();
	
	public World(int width, int height, int depth) {
		this(width, height, depth, 150f, new byte[width*height*depth]);
	}
	
	private World(int width, int height, int depth, float time, byte[] blocks) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.time = time;
		this.blocks = blocks;
		this.entities = new ArrayList<Entity>();
	}

	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public float getTime() {
		return this.time;
	}
	
	public byte getBlock(int x, int y, int z) {
		if(x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
			return 0;
		}
		return blocks[(z*width*height)+(y*width)+x];
	}
	
	public void setBlock(int x, int y, int z, byte b) {
		if(x < 0 || y < 0 || z < 0 || x >= width || y >= height || z >= depth) {
			return;
		}
		int idx = (z*width*height)+(y*width)+x;
		if(blocks[idx] != b) {
			byte old = blocks[idx];
			blocks[idx] = b;
			for(WorldListener listener : listeners) {
				listener.onBlockUpdate(x, y, z, old, b);
			}
		}
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public float getMaxTime() {
		return 30f * 60f; // 30 minutes
	}
	
	public void tickWorld(float delta) {
		this.time = (this.time + delta) % this.getMaxTime();
		
		Iterator<Entity> iterator = this.entities.iterator();
		while(iterator.hasNext()) {
			Entity e = iterator.next();
			e.update();
			if(e.isDead()) {
				this.entities.remove(e);
			}
		}
	}
	
	public boolean isAir(int x, int y, int z) {
		return getBlock(x, y, z) == Block.air.id;
	}
	
	public boolean isTransparent(int x, int y, int z) {
		byte b = getBlock(x, y, z);
		if(b == 0) {
			return true;
		}
		if(b < 0 || b >= Block.blocks.length || Block.blocks[b] == null) {
			return false;
		}
		return Block.blocks[b].isTransparent();
	}
	
	public int getHeight(int x, int z) {
		int y = this.height;
		while(isAir(x, y, z) && y > 0) {
			y--;
		}
		return y;
	}
	
	public ArrayList<AABB> getBoundingBoxes(AABB aabb, ArrayList<AABB> list) {
		int x0 = (int)(aabb.getCenter().x - aabb.getSize().x - 1f);
		int y0 = (int)(aabb.getCenter().y - aabb.getSize().y - 1f);
		int z0 = (int)(aabb.getCenter().z - aabb.getSize().z - 1f);
		int x1 = (int)(aabb.getCenter().x + aabb.getSize().x + 1f);
		int y1 = (int)(aabb.getCenter().y + aabb.getSize().y + 1f);
		int z1 = (int)(aabb.getCenter().z + aabb.getSize().z + 1f);
		
		list.clear();
		for(int x = x0; x <= x1; x++) {
			for(int y = y0; y <= y1; y++) {
				for(int z = z0; z <= z1; z++) {
					byte blockID = getBlock(x, y, z);
					if(blockID <= 0) {
						continue;
					}
					Block block = Block.blocks[blockID];
					if(block != null) {
						AABB box = block.getBoundingBox(x, y, z);
						if(box != null) {
							list.add(box);
						}
					}
				}
			}
		}
		return list;
	}
	
	public void generateNewWorld() {
		WORLD_GEN.generate(this, this.width, this.height, this.depth);
		for(WorldListener listener : listeners) {
			listener.allBlocksUpdated();
		}
	}
	
	public void addWorldListener(WorldListener listener) {
		listeners.add(listener);
	}
	
	public Entity spawnEntity(String entityName) {
		if(!EntityRegistry.isEntityRegistered(entityName)) {
			throw new IllegalArgumentException("Entity " + entityName + " is not registered!");
		}
		Entity entity = EntityRegistry.getEntityInstance(entityName, this);
		this.entities.add(entity);
		return entity;
	}
	
	public ArrayList<Entity> getEntities() {
		return this.entities;
	}
	
	public void save() {
		if(!ENABLE_SAVING) {
			return;
		}
		try {
			FileOutputStream fout = new FileOutputStream(SAVE_PATH);
			DataOutputStream out = new DataOutputStream(new GZIPOutputStream(fout));
			out.writeInt(width);
			out.writeInt(height);
			out.writeInt(depth);
			out.writeFloat(time);
			out.write(blocks);
			out.flush();
			out.close();
		} catch(Exception e) {
			System.err.println("Failed to save world!");
			e.printStackTrace(System.err);
		}
	}
	
	public static World load() {
		try {
			File f = new File(SAVE_PATH);
			if(!f.exists()) {
				return null;
			}
			FileInputStream fin = new FileInputStream(f);
			DataInputStream in = new DataInputStream(new GZIPInputStream(fin));
			
			int width = in.readInt();
			int height = in.readInt();
			int depth = in.readInt();
			float time = in.readFloat();
			byte[] blocks = new byte[width*height*depth];
			in.readFully(blocks);
			
			in.close();
			return new World(width, height, depth, time, blocks);
		} catch(Exception e) {
			System.err.println("Failed to load world!");
			e.printStackTrace(System.err);
			return null;
		}
	}
	
}