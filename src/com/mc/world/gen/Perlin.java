package com.mc.world.gen;

import java.util.Random;

public class Perlin {

	public static final Random RANDOM = new Random();
	public static final float NOISE_SCALE = 128f;
	
	private final int width;
	private final int height;
	private final float[][] noise;
	
	public Perlin(int width, int height) {
		this.width = width;
		this.height = height;
		this.noise = new float[width][height];
		this.generate();
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public float getNoise(int x, int y) {
		if(x < 0 || y < 0 || x >= this.width || y >= this.height) {
			return 0f;
		}
		return this.noise[x][y];
	}
	
	public void generate() {
		float[][] rand = new float[this.width][this.height];
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				rand[x][y] = RANDOM.nextFloat();
			}
		}
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				float posX = (float)x / NOISE_SCALE;
				float posY = (float)y / NOISE_SCALE;
				float fractX = posX - (int)posX;
				float fractY = posY - (int)posY;
				int x1 = ((int)x+this.width)%this.width;
				int y1 = ((int)y+this.height)%this.height;
				int x2 = ((int)x1+this.width-1)%this.width;
				int y2 = ((int)y1+this.height-1)%this.height;
				
				float value = 0f;
				value += fractX * fractY * rand[x1][y1];
				value += (1-fractX) * fractY * rand[x2][y1];
				value += fractX * (1-fractY) * rand[x1][y2];
				value += (1-fractX) * (1-fractY) * rand[x2][y2];
				
				this.noise[x][y] = value;
			}
		}
	}
	
}