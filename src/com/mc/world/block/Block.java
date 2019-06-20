package com.mc.world.block;

import com.mc.physics.AABB;
import com.mc.world.World;

public class Block {
	
	public static final Block[] blocks = new Block[2048];
	public static final Block air = new BlockAir(0);
	public static final Block dirt = new Block(1, "dirt").setTexture(0);
	public static final Block grass = new BlockGrass(2, "grass");
	public static final Block stone = new Block(3, "stone").setTexture(3);
	public static final Block log = new BlockLog(4, "log").setTexture(4);
	public static final Block leaves = new Block(5, "leaves").setTexture(6).setTransparent().setWaveAmount(1f);
	public static final Block tallGrass = new BlockBush(6, "tallGrass").setTexture(7).setWaveAmount(1.5f);
	public static final Block water = new BlockWater(7, "water").setTexture(8);
	public static final Block sand = new Block(8, "sand").setTexture(9);
	public static final Block yellowFlower = new BlockBush(9, "yellowFlower").setTexture(10).setWaveAmount(2f);
	public static final Block redFlower = new BlockBush(10, "redFlower").setTexture(11).setWaveAmount(2f);
	
	public final byte id;
	private int tex;
	private String name;
	
	private boolean transparent = false;
	private float waveAmount = 0f;
	private RenderQueue renderQueue = RenderQueue.NORMAL;
	
	public Block(int id, String name) {
		this.id = (byte)id;
		this.name = name;
		if(blocks[id] != null) {
			throw new IllegalArgumentException("A block with the id " + id + " is already registered! (" + blocks[id].name + ")");
		} else {
			blocks[id] = this;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getTexture(Facing face) {
		return this.tex;
	}
	
	public Block setTexture(int tex) {
		this.tex = tex;
		return this;
	}
	
	public boolean isTransparent() {
		return this.transparent;
	}
	
	public Block setTransparent() {
		this.transparent = true;
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Block)) {
			return false;
		}
		return this.id == ((Block)other).id;
	}
	
	public boolean isRenderable() {
		return true;
	}
	
	public AABB getBoundingBox(int x, int y, int z) {
		return new AABB((float)x, (float)y, (float)z, 0.5f, 0.5f, 0.5f);
	}
	
	public float getWaveAmount(float u, float v) {
		return this.waveAmount;
	}
	
	protected Block setWaveAmount(float amount) {
		this.waveAmount = amount;
		return this;
	}
	
	public RenderQueue getRenderQueue() {
		return this.renderQueue;
	}
	
	public Block setRenderQueue(RenderQueue queue) {
		this.renderQueue = queue;
		return this;
	}
	
	public boolean isFaceOccluded(World world, int x, int y, int z, Facing face) {
		int tx = (int)((float)x + face.normal.x());
		int ty = (int)((float)y + face.normal.y());
		int tz = (int)((float)z + face.normal.z());
		return !world.isAir(tx, ty, tz) && !world.isTransparent(tx, ty, tz);
	}
	
}