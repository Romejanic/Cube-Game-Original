package com.mc.gl.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.mc.utils.Utils;

public class Mesher {

	private static final ArrayList<Integer> vaos = new ArrayList<Integer>();
	private static final ArrayList<Integer> vbos = new ArrayList<Integer>();
	
	private static final HashMap<String, Mesh> generatedMeshes = new HashMap<String, Mesh>();
	
	public static Mesh getQuad() {
		if(generatedMeshes.containsKey("quad")) {
			return generatedMeshes.get("quad");
		}
		int vao = glGenVertexArrays();
		int vbo = glGenBuffers();
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		float[] vertices = {
			-1f, -1f,
			 1f, -1f,
			-1f,  1f,
			 1f,  1f
		};
		glBufferData(GL_ARRAY_BUFFER, Utils.store(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		Mesh quad = new Mesh(vao, vertices.length / 2, GL_TRIANGLE_STRIP, false, 0);
		vaos.add(vao); vbos.add(vbo);
		generatedMeshes.put("quad", quad);
		return quad;
	}
	
	public static void deleteAll() {
		glDeleteVertexArrays(Utils.storeIntList(vaos));
		glDeleteBuffers(Utils.storeIntList(vbos));
		generatedMeshes.clear();
	}
	
}