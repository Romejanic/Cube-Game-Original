package com.mc.gl.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.BufferUtils;

import com.mc.camera.Camera;
import com.mc.gl.objects.Shader;
import com.mc.utils.Utils;

public class Skybox {

	public static final float[] VERTICES = {
		-0.5f, -0.5f, -0.5f, // 0
		 0.5f, -0.5f, -0.5f, // 1
		-0.5f,  0.5f, -0.5f, // 2
		 0.5f,  0.5f, -0.5f, // 3
		 
		-0.5f, -0.5f,  0.5f, // 4
		 0.5f, -0.5f,  0.5f, // 5
		-0.5f,  0.5f,  0.5f, // 6
		 0.5f,  0.5f,  0.5f  // 7
	};
	public static final int[] INDICES = {
		 0, 1, 2,
		 2, 1, 3,
		 6, 5, 4,
		 7, 5, 6,
		 
		 1, 5, 3,
		 3, 5, 7,
		 2, 4, 0,
		 6, 4, 2,
		 
		 3, 6, 2,
		 7, 6, 3,
		 0, 4, 1,
		 1, 4, 5,
	};
	
	public static final Vector3fc BOTTOM_COLOR = new Vector3f(0.8f,0.8f,1f).toImmutable();
	public static final Vector3fc TOP_COLOR = new Vector3f(0.1f,0.3f,1f).toImmutable();
	
	private int vao;
	private IntBuffer vbos;
	private Shader shader;
	
	private final Matrix4f tempViewMat = new Matrix4f();
	
	public Skybox() {
		this.vao = glGenVertexArrays();
		this.shader = Shader.get("skybox");
		
		this.vbos = BufferUtils.createIntBuffer(2);
		glGenBuffers(this.vbos);
		
		glBindVertexArray(this.vao);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbos.get(0));
		glBufferData(GL_ARRAY_BUFFER, Utils.store(VERTICES), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vbos.get(1));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.store(INDICES), GL_STATIC_DRAW);
		glBindVertexArray(0);
	}
	
	public void render(Camera camera, SunLight sun) {
		camera.getViewMatrix().get(this.tempViewMat);
		this.tempViewMat.m30(0f);
		this.tempViewMat.m31(0f);
		this.tempViewMat.m32(0f);
		
		this.shader.uniform("projMat").set(camera.getProjectionMatrix());
		this.shader.uniform("viewMat").set(this.tempViewMat);
		this.shader.uniform("bottomColor").set(BOTTOM_COLOR);
		this.shader.uniform("topColor").set(TOP_COLOR);
		sun.uploadUniforms(this.shader);
		
		this.shader.bind();
		glDisable(GL_DEPTH_TEST);
		glBindVertexArray(this.vao);
		glEnableVertexAttribArray(0);
		glDrawElements(GL_TRIANGLES, INDICES.length, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		glEnable(GL_DEPTH_TEST);
		this.shader.unbind();
	}
	
	public void delete() {
		glDeleteVertexArrays(this.vao);
		glDeleteBuffers(this.vbos);
	}
	
}