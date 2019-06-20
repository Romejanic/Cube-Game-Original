package com.mc.gl.world;

import org.joml.Vector3f;

import com.mc.gl.objects.Shader;
import com.mc.utils.Mathf;
import com.mc.world.World;

public class SunLight {

	public Vector3f color;
	public Vector3f direction;
	
	public SunLight(Vector3f color) {
		this(color, new Vector3f());
	}
	
	public SunLight(Vector3f color, Vector3f direction) {
		this.color = color;
		this.direction = direction;
	}
	
	public void updateSunLight(World world) {
		float t = world.getTime() / world.getMaxTime();
		float theta = t * 2f * Mathf.PI;
		float dx = Mathf.cos(theta);
		float dy = Mathf.sin(theta);
		float dz = 0.45f; // TODO: add spin later
		
		// TODO: calculate color
		this.direction.set(dx, dy, dz).normalize();
	}
	
	public void uploadUniforms(Shader shader) {
		shader.uniform("sunColor").set(this.color);
		shader.uniform("sunDirection").set(this.direction);
	}
	
}