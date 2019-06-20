package com.mc.physics;

import org.joml.Vector3f;

public class AABB {

	private final Vector3f center, size;

	public AABB(Vector3f center, Vector3f size) {
		this(center.x, center.y, center.z, size.x, size.y, size.z);
	}

	public AABB(float cx, float cy, float cz, float sx, float sy, float sz) {
		this.center = new Vector3f(cx, cy, cz);
		this.size = new Vector3f(sx, sy, sz);
	}
	
	public Vector3f getCenter() {
		return this.center;
	}
	
	public Vector3f getSize() {
		return this.size;
	}

	public AABB grow(float x, float y, float z) {
		this.size.add(x, y, z);
		return this;
	}

	public AABB shrink(float x, float y, float z) {
		return grow(-x, -y, -z);
	}

	public AABB move(float x, float y, float z) {
		this.center.add(x, y, z);
		return this;
	}
	
	public AABB moveTo(Vector3f position) {
		return moveTo(position.x, position.y, position.z);
	}
	
	public AABB moveTo(float x, float y, float z) {
		this.center.set(x, y, z);
		return this;
	}
	
	public AABB setSize(Vector3f size) {
		this.size.set(size);
		return this;
	}

	public boolean collides(AABB other) {
		if(Math.abs(this.center.x - other.center.x) < this.size.x + other.size.x) {
			if(Math.abs(this.center.y - other.center.y) < this.size.y + other.size.y) {
				return Math.abs(this.center.z - other.center.z) < this.size.z + other.size.z;
			}
		}
		return false;
	}
	
	public boolean isPointInside(Vector3f p) {
		return isPointInside(p.x, p.y, p.z);
	}
	
	public boolean isPointInside(float px, float py, float pz) {
		if(Math.abs(this.center.x - px) < this.size.x) {
			if(Math.abs(this.center.y - py) < this.size.y) {
				return Math.abs(this.center.z - pz) < this.size.z;
			}
		}
		return false;
	}

}