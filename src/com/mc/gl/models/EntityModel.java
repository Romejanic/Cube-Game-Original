package com.mc.gl.models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.MatrixStackf;
import org.joml.Vector3f;

import com.mc.gl.objects.Shader;
import com.mc.utils.Mathf;
import com.mc.utils.Utils;

public class EntityModel {

	private static final HashMap<String, EntityModel> models = new HashMap<String, EntityModel>();

	private final HashMap<String, EntityModelVAO> modelParts = new HashMap<String, EntityModelVAO>();
	private final MatrixStackf modelMat = new MatrixStackf(5);

	public EntityModelVAO getModelPart(String name) {
		if(this.modelParts.containsKey(name)) {
			return this.modelParts.get(name);
		}
		return null;
	}

	public void render(Vector3f position, Vector3f rotation, Vector3f scale, Shader shader, float yOffset) {
		this.modelMat.clear();
		this.modelMat.translate(position).translate(0f, yOffset, 0f).rotateXYZ(Mathf.rad(rotation)).scale(scale);
		for(EntityModelVAO vao : this.modelParts.values()) {
			this.modelMat.pushMatrix();
			vao.render(this, shader, this.modelMat);
			this.modelMat.popMatrix();
		}
	}

	public void delete() {
		for(EntityModelVAO vao : this.modelParts.values()) {
			vao.delete();
		}
		this.modelParts.clear();
	}

	public static EntityModel get(String name) {
		if(models.containsKey(name)) {
			return models.get(name);
		}
		EntityModel model = new EntityModel();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Utils.getResource("models/" + name + ".obj")));
			String line;

			String currentObject = null;
			ArrayList<Float> modelData = new ArrayList<Float>();
			ArrayList<Float> vertices = new ArrayList<Float>();
			ArrayList<Float> texCoords = new ArrayList<Float>();
			ArrayList<Float> normals = new ArrayList<Float>();

			while((line = reader.readLine()) != null) {
				if(line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] parts = line.split(" ");
				if(parts[0].equals("v")) {
					vertices.add(Float.parseFloat(parts[1]));
					vertices.add(Float.parseFloat(parts[2]));
					vertices.add(Float.parseFloat(parts[3]));
				} else if(parts[0].equals("vt")) {
					texCoords.add(1f - Float.parseFloat(parts[1]));
					texCoords.add(1f - Float.parseFloat(parts[2]));
				} else if(parts[0].equals("vn")) {
					normals.add(Float.parseFloat(parts[1]));
					normals.add(Float.parseFloat(parts[2]));
					normals.add(Float.parseFloat(parts[3]));
				} else if(parts[0].equals("o")) {
					appendModelPart(model, currentObject, modelData);
					currentObject = parts[1];
				} else if(parts[0].equals("f")) {
					for(int i = 1; i < parts.length; i++) {
						String[] face = parts[i].split("/");
						int vi = Integer.parseInt(face[0]) - 1;
						int ti = Integer.parseInt(face[1]) - 1;
						int ni = Integer.parseInt(face[2]) - 1;

						modelData.add(vertices.get(vi*3)); modelData.add(vertices.get(vi*3+1)); modelData.add(vertices.get(vi*3+2));
						modelData.add(texCoords.get(ti*2)); modelData.add(texCoords.get(ti*2+1));
						modelData.add(normals.get(ni*3)); modelData.add(normals.get(ni*3+1)); modelData.add(normals.get(ni*3+2));
					}
				}
			}
			appendModelPart(model, currentObject, modelData); // append last object
		} catch(Exception e) {
			System.err.println("Failed to load model: " + e.toString());
			e.printStackTrace(System.err);
		}

		models.put(name, model);
		return model;
	}

	private static void appendModelPart(EntityModel model, String currentObject, ArrayList<Float> modelData) {
		if(!modelData.isEmpty()) {
			String partName = currentObject != null ? currentObject : "unnamed_" + System.currentTimeMillis();
			model.modelParts.put(partName, new EntityModelVAO(modelData));
			modelData.clear();
		}
	}

	public static void deleteAll() {
		for(EntityModel model : models.values()) {
			model.delete();
		}
		models.clear();
	}

}