package com.mc.gl.objects;

import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class UniformVar {
	
	private static final FloatBuffer tempMat4 = BufferUtils.createFloatBuffer(16);

	private final Shader shader;
	private final int location;
	private Object value;

	private boolean isDirty = false;
	private int lastTexUnit = -1;

	protected UniformVar(Shader shader, int location) {
		this.shader = shader;
		this.location = location;
	}

	public void set(Object value) {
		this.value = value;
		if(this.shader.isActive()) {
			if(this.isTexture() && this.lastTexUnit < 0) {
				throw new IllegalStateException("No reference to previous texture unit!");
			}
			this.upload(this.lastTexUnit);
		} else {
			this.isDirty = value != null;
		}
	}

	public Object get() {
		return this.value;
	}

	public boolean willUpdate() {
		return this.isDirty || this.shader.isActive();
	}

	public boolean isValid() {
		return this.location != -1;
	}

	public boolean isTexture() {
		return get() instanceof Texture;
	}
	
	public void preloadTextureUnit(int unit) {
		this.lastTexUnit = unit;
	}

	protected void upload(int texUnit) {
		if(!isValid() || (!willUpdate() && !isTexture()) || get() == null) {
			return;
		}
		this.isDirty = false;
		
		Object val = get();
		if(val instanceof Float) {
			glUniform1f(this.location, (float)val);
		} else if(val instanceof Integer) {
			glUniform1i(this.location, (int)val);
		} else if(val instanceof Vector2f) {
			Vector2f v = (Vector2f)val;
			glUniform2f(this.location, v.x, v.y);
		} else if(val instanceof Vector3f) {
			Vector3f v = (Vector3f)val;
			glUniform3f(this.location, v.x, v.y, v.z);
		} else if(val instanceof Vector4f) {
			Vector4f v = (Vector4f)val;
			glUniform4f(this.location, v.x, v.y, v.z, v.w);
		} else if(val instanceof Matrix4f) {
			((Matrix4f)val).get(tempMat4);
			glUniformMatrix4fv(this.location, false, tempMat4);
		} else if(isTexture()) {
			glActiveTexture(GL_TEXTURE0 + texUnit);
			glUniform1i(this.location, texUnit);
			((Texture)get()).bind();
			
			this.lastTexUnit = texUnit;
		}
	}

}