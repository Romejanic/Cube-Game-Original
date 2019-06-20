package com.mc.gl.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

	private int vao;
	private int elementCount;
	private int[] attribs;
	private boolean hasIndexBuffer;

	private int drawMode = GL_TRIANGLES;

	public Mesh(int vao, int elementCount, boolean hasIndexBuffer, int... attribs) {
		this.vao = vao;
		this.elementCount = elementCount;
		this.attribs = attribs;
		this.hasIndexBuffer = hasIndexBuffer;
	}

	public Mesh(int vao, int elementCount, int drawMode, boolean hasIndexBuffer, int... attribs) {
		this(vao, elementCount, hasIndexBuffer, attribs);
		this.drawMode = drawMode;
	}

	public void bind() {
		glBindVertexArray(this.vao);
		for(int attrib : this.attribs) {
			glEnableVertexAttribArray(attrib);
		}
	}

	public void unbind() {
		for(int attrib : this.attribs) {
			glDisableVertexAttribArray(attrib);
		}
		glBindVertexArray(0);
	}

	public void draw() {
		if(this.hasIndexBuffer) {
			glDrawElements(this.drawMode, this.elementCount, GL_UNSIGNED_INT, 0);
		} else {
			glDrawArrays(this.drawMode, 0, this.elementCount);
		}
	}

}