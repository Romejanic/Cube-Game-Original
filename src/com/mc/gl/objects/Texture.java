package com.mc.gl.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;

import com.mc.utils.Utils;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {

	public static final boolean ENABLE_MIPMAPPING = false;
	public static final boolean ENABLE_FILTER_ANISOTROPIC = true;

	private static final ArrayList<Integer> textures = new ArrayList<Integer>();
	private static final HashMap<String, Texture> cache = new HashMap<String, Texture>();

	private final String name;
	private int target;
	private int texture;

	private Texture(String name, int target, int texture) {
		this.name = name;
		this.target = target;
		this.texture = texture;
	}

	public String getName() {
		return this.name;
	}
	
	public void bind() {
		glBindTexture(this.target, this.texture);
	}

	public void unbind() {
		glBindTexture(this.target, 0);
	}

	public static Texture get(String name) {
		if(cache.containsKey(name)) {
			return cache.get(name);
		}
		Texture tex = new Texture(name, GL_TEXTURE_2D, 0);
		try {
			InputStream in = Utils.getResource("textures/" + name + ".png");
			PNGDecoder png = new PNGDecoder(in);
			ByteBuffer buf = BufferUtils.createByteBuffer(png.getWidth()*png.getHeight()*4);
			png.decode(buf, png.getWidth() * 4, Format.RGBA);
			buf.flip();
			in.close();

			tex.texture = glGenTextures();
			tex.bind();
			glTexParameteri(tex.target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(tex.target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(tex.target, 0, GL_RGBA, png.getWidth(), png.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
			if(ENABLE_MIPMAPPING) {
				glTexParameteri(tex.target, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
				glTexParameterf(tex.target, GL_TEXTURE_LOD_BIAS, -1f);
				if(ENABLE_FILTER_ANISOTROPIC) {
					if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
						float anisoLevel = Math.min(8f, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
						glTexParameterf(tex.target, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisoLevel);
					} else {
						System.out.println("GLContext: Anisotropic filtering not supported!");
					}
				}
				glGenerateMipmap(tex.target);
			}
			tex.unbind();

			textures.add(tex.texture);
		} catch(Exception e) {
			System.err.println("Failed to load texture: " + name);
			e.printStackTrace(System.err);
			if(tex.texture > 0) {
				glDeleteTextures(tex.texture);
				tex.texture = 0;
			}
		}
		cache.put(name, tex);
		return tex;
	}
	
	public static Texture generateEmpty(String name, int width, int height) {
		int textureID = 0;
		try {
			ByteBuffer buf = BufferUtils.createByteBuffer(width*height*3);
			for(int i = 0; i < width * height; i++) {
				buf.put((byte)0).put((byte)0).put((byte)0);
			}
			buf.flip();
			
			textureID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureID);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buf);
			glBindTexture(GL_TEXTURE_2D, 0);
		} catch(Exception e) {
			System.err.println("Failed to generate empty texture: " + name);
			e.printStackTrace(System.err);
			if(textureID > 0) {
				glDeleteTextures(textureID);
				textureID = 0;
			}
		}
		return from2D(name, textureID);
	}
	
	public static Texture from2D(String name, int textureID) {
		Texture t = new Texture(name, GL_TEXTURE_2D, textureID);
		cache.put(name, t); textures.add(textureID);
		return t;
	}

	public static void deleteTexture(String textureName) {
		if(!cache.containsKey(textureName)) {
			return;
		}
		int textureID = Texture.get(textureName).texture;
		glDeleteTextures(textureID);
		cache.remove(textureName);
		textures.remove(textures.indexOf(textureID));
	}
	
	public static void deleteAll() {
		glDeleteTextures(Utils.storeIntList(textures));
		textures.clear();
		cache.clear();
	}

}