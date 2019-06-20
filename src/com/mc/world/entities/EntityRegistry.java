package com.mc.world.entities;

import java.util.HashMap;
import java.util.Iterator;

import com.mc.world.World;

public class EntityRegistry {

	private static final HashMap<String, Class<? extends Entity>> entities = new HashMap<String, Class<? extends Entity>>();
	private static final HashMap<Class<? extends Entity>, EntityModelData> modelData = new HashMap<Class<? extends Entity>, EntityModelData>();
	
	public static boolean isEntityRegistered(String name) {
		return entities.containsKey(name);
	}
	
	public static boolean isClassRegistered(Class<? extends Entity> clazz) {
		return entities.containsValue(clazz);
	}
	
	public static Iterator<Class<? extends Entity>> getClassIterator() {
		return entities.values().iterator();
	}
	
	public static Iterator<EntityModelData> getModelDataIterator() {
		return modelData.values().iterator();
	}
	
	public static EntityModelData getModelData(Class<? extends Entity> clazz) {
		if(!modelData.containsKey(clazz)) {
			throw new IllegalArgumentException("Model data does not exist for entity " + clazz);
		}
		return modelData.get(clazz);
	}
	
	public static Entity getEntityInstance(String name, World world) {
		if(!isEntityRegistered(name)) {
			return null;
		}
		try {
			Class<? extends Entity> clazz = entities.get(name);
			return clazz.getConstructor(World.class).newInstance(world);
		} catch(Exception e) {
			System.err.println("Failed to create entity instance: " + name);
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	private static void registerEntity(String name, Class<? extends Entity> clazz, EntityModelData modelData) {
		if(EntityRegistry.entities.containsKey(name) || EntityRegistry.modelData.containsKey(modelData)) {
			throw new IllegalArgumentException("Entity " + name + " is already registered!");
		}
		EntityRegistry.entities.put(name, clazz);
		EntityRegistry.modelData.put(clazz, modelData);
	}
	
	static {
		registerEntity("pig", EntityPig.class, new EntityModelData("pig", "entities/pig_d", 0.5f));
	}
	
}