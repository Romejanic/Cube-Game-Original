package com.mc.world.entities;

import com.mc.gl.Window;
import com.mc.utils.Mathf;
import com.mc.world.World;

public class EntityPig extends Entity {
	
	private float targetX = 0f;
	private float targetZ = 0f;
	private float timeToNext = 10f;
	
	public float moveSpeed = 2f;
	
	public EntityPig(World world) {
		super(world);
		this.timeToNext = Mathf.random(3f, 8f);
	}
	
	@Override
	public void update() {
		super.update();
		
		this.timeToNext -= Window.getDelta();
		if(this.timeToNext <= 0f) {
			this.timeToNext = Mathf.random(5f, 15f);
			this.targetX = this.position.x + Mathf.random(-10f, 10f);
			this.targetZ = this.position.z + Mathf.random(-10f, 10f);
		}
		
		if(this.targetX > 0f && this.targetZ > 0f) {
			float dirX = this.targetX - this.position.x;
			float dirZ = this.targetZ - this.position.z;
			float magD = Mathf.sqrt(dirX*dirX+dirZ*dirZ);
			if(magD > 0.1f) {
				magD = 1f / magD;
				dirX *= magD; dirZ *= magD;
				
				this.targetYaw = Mathf.deg(Mathf.atan2(dirZ, dirX));
				
				float speed = this.moveSpeed * Window.getDelta();
				this.position.add(dirX * speed, 0f, dirZ * speed);
			}
		}
		
		this.position.y = this.world.getHeight((int)this.position.x, (int)this.position.z);
	}

}
