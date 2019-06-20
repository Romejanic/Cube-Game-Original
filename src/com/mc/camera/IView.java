package com.mc.camera;

import org.joml.Matrix4f;

public interface IView {

	Matrix4f getProjectionMatrix();
	Matrix4f getViewMatrix();
	
}