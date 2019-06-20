package com.mc.gl.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;

import com.mc.utils.Utils;

@Deprecated
/**
 * @deprecated No longer used; chunks now used one VAO for all render queues.
 */
public class ChunkVAO {

	private int vao;
	private int vbo;
	private int elementCount;

	public ChunkVAO() {
		this.init();
	}

	public boolean isEmpty() {
		return this.elementCount <= 0;
	}

	private void init() {
		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();
		glBindVertexArray(this.vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, 33000 * 4, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 8 * 4, 3 * 4);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 8 * 4, 5 * 4);
	}

	public void update(ArrayList<Float> vertices) {
		if(vertices.isEmpty()) {
			return;
		}
		glBindVertexArray(this.vao);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		this.elementCount = vertices.size() / 8;
		glBufferData(GL_ARRAY_BUFFER, vertices.size() * 4, GL_STREAM_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, Utils.storeFloatList(vertices));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void draw() {
		if(this.elementCount <= 0) {
			return;
		}
		glBindVertexArray(this.vao);
		glDrawArrays(GL_TRIANGLES, 0, this.elementCount);
	}

	public void delete() {
		glDeleteVertexArrays(this.vao);
		glDeleteBuffers(this.vbo);
	}

}