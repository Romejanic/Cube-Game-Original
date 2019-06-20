package com.mc.world.entities;

public class EntityModelData {

	public final String modelName;
	public final String modelTexture;
	public final float modelOffset;
	
	public EntityModelData(String modelName, String modelTexture, float modelOffset) {
		this.modelName = modelName;
		this.modelTexture = modelTexture;
		this.modelOffset = modelOffset;
	}
	
	public String getModel() {
		return this.modelName;
	}
	
	public String getTexture() {
		return this.modelTexture;
	}
	
	public float getYOffset() {
		return this.modelOffset;
	}
	
}