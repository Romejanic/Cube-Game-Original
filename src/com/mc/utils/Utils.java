package com.mc.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class Utils {

	public static final Vector3f TEMP_VEC3 = new Vector3f();
	
	public static InputStream getResource(String name) throws FileNotFoundException {
		String path = "/res" + (name.startsWith("/") ? "" : "/") + name;
		InputStream stream = Utils.class.getResourceAsStream(path);
		if(stream == null) {
			throw new FileNotFoundException(path);
		}
		return stream;
	}
	
	public static String read(String fileName) throws Exception {
		InputStream stream = getResource(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder out = new StringBuilder();
		for(String ln = reader.readLine(); ln != null; ln = reader.readLine()) {
			out.append(ln).append("\n");
		}
		reader.close();
		return out.toString().trim();
	}
	
	public static ArrayList<String> readLines(String fileName) throws Exception {
		InputStream stream = getResource(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		ArrayList<String> out = new ArrayList<String>();
		for(String ln = reader.readLine(); ln != null; ln = reader.readLine()) {
			out.add(ln);
		}
		reader.close();
		return out;
	}
	
	public static FloatBuffer store(float... data) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(data.length);
		return (FloatBuffer)buf.put(data).flip();
	}
	
	public static IntBuffer store(int... data) {
		IntBuffer buf = BufferUtils.createIntBuffer(data.length);
		return (IntBuffer)buf.put(data).flip();
	}
	
	public static FloatBuffer storeFloatList(ArrayList<Float> list) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(list.size());
		for(Float f : list) {
			buf.put(f);
		}
		return (FloatBuffer)buf.flip();
	}
	
	public static IntBuffer storeIntList(ArrayList<Integer> list) {
		IntBuffer buf = BufferUtils.createIntBuffer(list.size());
		for(Integer i : list) {
			buf.put(i);
		}
		return (IntBuffer)buf.flip();
	}
	
	public static String joinList(ArrayList<String> list, String divider) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < list.size(); i++) {
			out.append(list.get(i));
			if(i != list.size() - 1) {
				out.append(divider);
			}
		}
		return out.toString();
	}
	
	public static String joinArray(int[] array, String divider) {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < array.length; i++) {
			out.append(String.valueOf(array[i]));
			if(i != array.length - 1) {
				out.append(divider);
			}
		}
		return out.toString();
	}
	
}