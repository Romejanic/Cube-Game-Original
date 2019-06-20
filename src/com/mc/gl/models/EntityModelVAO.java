package com.mc.gl.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;

import org.joml.MatrixStackf;
import org.joml.Vector3f;

import com.mc.gl.objects.Shader;
import com.mc.utils.Mathf;
import com.mc.utils.Utils;

public class EntityModelVAO {

	private int vao, vbo, indexCount;
	
	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f();
	public final Vector3f scale = new Vector3f(1f);
	
	public EntityModelVAO(ArrayList<Float> data) {
		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();
		this.indexCount = data.size() / 8;
		
		glBindVertexArray(this.vao);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, Utils.storeFloatList(data), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 8 * 4, 3 * 4);
		glVertexAttribPointer(2, 3, GL_FLOAT, true,  8 * 4, 5 * 4);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void render(EntityModel model, Shader shader, MatrixStackf modelMat) {
		modelMat.translate(this.position).rotateXYZ(Mathf.rad(this.rotation)).scale(this.scale);
		shader.uniform("modelMat").set(modelMat);
		
		glBindVertexArray(this.vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glDrawArrays(GL_TRIANGLES, 0, this.indexCount);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	public void delete() {
		glDeleteVertexArrays(this.vao);
		glDeleteBuffers(this.vbo);
	}
	
}