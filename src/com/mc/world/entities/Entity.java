package com.mc.world.entities;

import org.joml.Vector3f;

import com.mc.gl.Window;
import com.mc.physics.AABB;
import com.mc.utils.Mathf;
import com.mc.utils.Utils;
import com.mc.world.World;

public class Entity {

	public final World world;
	public final Vector3f position;
	public final Vector3f velocity;
	public final Vector3f size;
	
	public float yaw = 0f;
	public float targetYaw = 0f;
	
	private boolean dead = false;
	private AABB aabb;
	
	public Entity(World world) {
		this.world = world;
		
		this.position = new Vector3f();
		this.velocity = new Vector3f();
		this.size = new Vector3f(1f);
		
		this.aabb = new AABB(this.position, this.size);
	}
	
	public void setDead() {
		this.dead = true;
	}
	
	public boolean isDead() {	
		return this.dead;
	}
	
	public void update() {
		if(this.targetYaw != this.yaw) {
			this.yaw = Mathf.lerp(this.yaw, this.targetYaw, Window.getDelta() * 2f);
		}
		this.aabb.moveTo(this.position);
		this.aabb.setSize(this.size.mul(0.5f, Utils.TEMP_VEC3));
	}
	
	public AABB getBoundingBox() {
		return this.aabb;
	}
	
}