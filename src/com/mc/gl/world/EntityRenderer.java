package com.mc.gl.world;

import java.util.ArrayList;
import java.util.Iterator;

import org.joml.Vector3f;

import com.mc.camera.Camera;
import com.mc.camera.Frustum;
import com.mc.gl.ShadowRenderer;
import com.mc.gl.models.EntityModel;
import com.mc.gl.objects.Shader;
import com.mc.gl.objects.Texture;
import com.mc.utils.Utils;
import com.mc.world.World;
import com.mc.world.entities.Entity;
import com.mc.world.entities.EntityModelData;
import com.mc.world.entities.EntityRegistry;

public class EntityRenderer {
	
	private static final Vector3f ONE = new Vector3f(1f);
	private static final float ENTITY_DRAW_DISTANCE = 100f;
	
	private final World world;
	private final Shader shader;
	private final Shader shadowShader;
	
	public EntityRenderer(World world) {
		this.world = world;
		this.shader = Shader.get("entity");
		this.shadowShader = Shader.get("entity_shadow");
		
		this.shader.uniform("diffuseTex").preloadTextureUnit(0);
		Iterator<EntityModelData> iterator = EntityRegistry.getModelDataIterator();
		while(iterator.hasNext()) {
			EntityModelData data = iterator.next();
			EntityModel.get(data.getModel());
			Texture.get(data.getTexture());
		}
	}
	
	public void preRender(Camera camera, SunLight sun) {
		camera.uploadUniforms(this.shader);
		sun.uploadUniforms(this.shader);
		this.shader.uniform("fogColor").set(Skybox.BOTTOM_COLOR);
	}
	
	public void renderEntities(Camera camera, Frustum frustum, ShadowRenderer shadowmap, boolean isShadow) {
		ArrayList<Entity> entities = this.world.getEntities();
		if(entities.isEmpty()) {
			return;
		}
		float entityDst = isShadow ? ShadowRenderer.SHADOW_DST : ENTITY_DRAW_DISTANCE;
		float entityDistSqr = entityDst * entityDst;
		
		Shader shader = isShadow ? this.shadowShader : this.shader;
		shadowmap.uploadUniforms(shader, isShadow);
		shader.bind();
		for(Entity entity : entities) {
			if(frustum.cubeOutsideFrustum(entity.getBoundingBox()) || entity.position.distanceSquared(camera.position) > entityDistSqr) {
				continue;
			}
			EntityModelData modelData = EntityRegistry.getModelData(entity.getClass());
			EntityModel model = EntityModel.get(modelData.getModel());
			if(!isShadow) {
				shader.uniform("diffuseTex").set(Texture.get(modelData.getTexture()));
			}
			Utils.TEMP_VEC3.set(0f, entity.yaw, 0f);
			model.render(entity.position, Utils.TEMP_VEC3, ONE, shader, modelData.getYOffset());
		}
		shader.unbind();
	}
	
}