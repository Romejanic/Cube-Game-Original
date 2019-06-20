package com.mc.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mc.gl.Window;
import com.mc.gl.objects.Shader;
import com.mc.main.CubeGame;
import com.mc.utils.Mathf;
import com.mc.utils.Utils;
import com.mc.world.World;
import com.mc.world.block.Block;
import com.mc.world.entities.EntityPig;

public class Camera implements IView {

	private static final float FOV  = 70f;
	private static final float NEAR = 0.1f;
	private static final float FAR  = 1000f;

	public Vector3f position = new Vector3f();
	public Vector3f rotation = new Vector3f();

	private Matrix4f projMat = new Matrix4f();
	private Matrix4f viewMat = new Matrix4f();
	private Vector3f forward = new Vector3f();
	
	public Camera(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public void update(int w, int h) {
		float moveSpeed = 10f * Window.getDelta();
		float rotSpeed = 20f * Window.getDelta();
		if(Window.isKeyDown(Window.KEY_LSHIFT)) {
			moveSpeed *= 2f;
		}

		this.rotation.x += (float)Window.getMouseDY() * rotSpeed;
		this.rotation.y += (float)Window.getMouseDX() * rotSpeed;
		this.rotation.x = Mathf.clamp(this.rotation.x, -90f, 90f);
		this.rotation.y %= 360f;

		float sinX   = Mathf.sin(Mathf.rad(this.rotation.x));
		float cosX   = Mathf.cos(Mathf.rad(this.rotation.x));
		float sinY   = Mathf.sin(Mathf.rad(this.rotation.y));
		float cosY   = Mathf.cos(Mathf.rad(this.rotation.y));
		float sinY90 = Mathf.sin(Mathf.rad(this.rotation.y+90f));
		float cosY90 = Mathf.cos(Mathf.rad(this.rotation.y+90f));
		this.forward.set(sinY * cosX, -sinX, -cosY * cosX).normalize().mul(moveSpeed);
		
		if(Window.isKeyDown(Window.KEY_W)) {
			this.position.add(this.forward);
		}
		if(Window.isKeyDown(Window.KEY_S)) {
			this.position.add(this.forward.negate(Utils.TEMP_VEC3));
		}
		if(Window.isKeyDown(Window.KEY_A)) {
			this.position.x -= moveSpeed * sinY90;
			this.position.z += moveSpeed * cosY90;
		}
		if(Window.isKeyDown(Window.KEY_D)) {
			this.position.x += moveSpeed * sinY90;
			this.position.z -= moveSpeed * cosY90;
		}
		if(Window.isKeyDown(Window.KEY_Q)) {
			this.position.y += moveSpeed;
		}
		if(Window.isKeyDown(Window.KEY_E)) {
			this.position.y -= moveSpeed;
		}

		this.forward.div(moveSpeed);
		
		if(Window.isKeyDown(Window.KEY_SPACE)) {
			int blockX = (int)(this.position.x + this.forward.x * 10f);
			int blockY = (int)(this.position.y + this.forward.y * 10f);
			int blockZ = (int)(this.position.z + this.forward.z * 10f);
			World world = CubeGame.instance.world;
			world.setBlock(blockX, blockY, blockZ, Block.dirt.id);
		}
		
		if(Window.isKeyPressed(Window.KEY_F)) {
			EntityPig pig = (EntityPig)CubeGame.instance.world.spawnEntity("pig");
			pig.position.set(this.position);
		}

		this.projMat
		.identity()
		.perspective(Mathf.rad(FOV), (float)w/(float)h, NEAR, FAR);

		this.viewMat
		.identity()
		.rotateXYZ(Mathf.rad(this.rotation))
		.translate(this.position.negate(Utils.TEMP_VEC3));
	}

	public Matrix4f getProjectionMatrix() {
		return this.projMat;
	}

	public Matrix4f getViewMatrix() {
		return this.viewMat;
	}
	
	public Vector3f getForward() {
		return this.forward;
	}

	public void uploadUniforms(Shader shader) {
		shader.uniform("projMat").set(this.getProjectionMatrix());
		shader.uniform("viewMat").set(this.getViewMatrix());
		shader.uniform("cameraPos").set(this.position);
	}

}