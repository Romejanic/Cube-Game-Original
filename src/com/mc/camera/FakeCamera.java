package com.mc.camera;

import org.joml.Matrix4f;

public class FakeCamera implements IView {

	private final Matrix4f projectionMatrix;
	private final Matrix4f viewMatrix;
	
	public FakeCamera(IView view) {
		this.projectionMatrix = new Matrix4f().set(view.getProjectionMatrix());
		this.viewMatrix = new Matrix4f().set(view.getViewMatrix());
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	@Override
	public Matrix4f getViewMatrix() {
		return this.viewMatrix;
	}
	
}