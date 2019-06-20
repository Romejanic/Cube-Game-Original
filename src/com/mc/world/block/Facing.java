package com.mc.world.block;

import org.joml.Vector3f;
import org.joml.Vector3fc;

public enum Facing {

	UP(0, new Vector3f(0f, 1f, 0f)),
	DOWN(1, new Vector3f(0f, -1f, 0f)),
	LEFT(2, new Vector3f(-1f, 0f, 0f)),
	RIGHT(3, new Vector3f(1f, 0f, 0f)),
	FRONT(4, new Vector3f(0f, 0f, 1f)),
	BACK(5, new Vector3f(0f, 0f, -1f));
	
	public final int id;
	public final Vector3fc normal;
	
	Facing(int id, Vector3f normal) {
		this.id = id;
		this.normal = normal.normalize().toImmutable();
	}
	
}