package com.mc.utils;

import java.util.Random;

import org.joml.Vector3f;

public class Mathf {

	public static final float PI = (float)Math.PI;
	public static final float E  = (float)Math.E;
	
	public static final Random RANDOM = new Random();
	
	private static final int STEPS_PER_DEGREE = 25;
	private static final float[] SIN_TABLE = new float[360 * STEPS_PER_DEGREE];
	private static final float[] COS_TABLE = new float[360 * STEPS_PER_DEGREE];
	
	public static float sin(float rad) {
		int i = Math.floorMod((int)((rad/(2f*PI))*(float)SIN_TABLE.length), SIN_TABLE.length);
		return SIN_TABLE[i];
	}
	
	public static float cos(float rad) {
		int i = Math.floorMod((int)((rad/(2f*PI))*(float)COS_TABLE.length), COS_TABLE.length);
		return COS_TABLE[i];
	}
	
	public static float rad(float deg) {
		return deg * (PI / 180f);
	}
	
	public static float asin(float x) {
		return (float)Math.asin((double)x);
	}
	
	public static float acos(float x) {
		return (float)Math.acos((double)x);
	}
	
	public static float atan(float x) {
		return (float)Math.atan((double)x);
	}
	
	public static float atan2(float y, float x) {
		return (float)Math.atan2((double)y, (double)x);
	}
	
	public static float deg(float rad) {
		return rad * (180f / PI);
	}
	
	public static float sqrt(float x) {
		return (float)Math.sqrt((double)x);
	}
	
	public static float lerp(float a, float b, float x) {
		return (1f - x) * a + x * b;
	}
	
	public static float random(float min, float max) {
		return min + RANDOM.nextFloat() * (max - min);
	}
	
	public static Vector3f rad(Vector3f angles) {
		Utils.TEMP_VEC3.set(rad(angles.x), rad(angles.y), rad(angles.z));
		return Utils.TEMP_VEC3;
	}
	
	public static float clamp(float x, float min, float max) {
		if(x < min) return min;
		if(x > max) return max;
		return x;
	}
	
	static {
		for(int i = 0; i < SIN_TABLE.length; i++) {
			double rad = ((double)i/(double)SIN_TABLE.length) * 2d * Math.PI;
			SIN_TABLE[i] = (float)Math.sin(rad);
		}
		for(int i = 0; i < COS_TABLE.length; i++) {
			double rad = ((double)i/(double)COS_TABLE.length) * 2d * Math.PI;
			COS_TABLE[i] = (float)Math.cos(rad);
		}
	}
	
}