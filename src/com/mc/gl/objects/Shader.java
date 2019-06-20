package com.mc.gl.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.mc.utils.Utils;

public class Shader {

	private static final boolean DEBUG_SOURCE_ON_ERROR = false;
	
	private static final HashMap<String, Shader> programs = new HashMap<String, Shader>();
	private static final HashMap<String, String> includes = new HashMap<String, String>();
	
	private final String name;
	private int program;
	
	private HashMap<String, UniformVar> uniforms = new HashMap<String, UniformVar>();

	public Shader(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void bind() {
		glUseProgram(this.program);
		uploadUniforms();
	}
	
	public boolean isActive() {
		return glGetInteger(GL_CURRENT_PROGRAM) == this.program;
	}
	
	private void uploadUniforms() {
		int texUnit = 0;
		for(UniformVar var : uniforms.values()) {
			var.upload(texUnit);
			if(var.isTexture()) {
				texUnit++;
			}
		}
	}

	public void unbind() {
		glUseProgram(0);
	}

	public UniformVar uniform(String name) {
		if(uniforms.containsKey(name)) {
			return uniforms.get(name);
		}
		UniformVar var = new UniformVar(this, glGetUniformLocation(this.program, name));
		if(!var.isValid()) {
			System.err.println("Uniform variable " + name + " not found in " + getName());
		}
		uniforms.put(name, var);
		return var;
	}
	
	public static Shader get(String name) {
		if(programs.containsKey(name)) {
			return programs.get(name);
		}
		Shader s = new Shader(name);
		int vs = -1, fs = -1;
		try {
			s.program = glCreateProgram();
			vs = load(name + "_vs", GL_VERTEX_SHADER);
			fs = load(name + "_fs", GL_FRAGMENT_SHADER);

			glAttachShader(s.program, vs);
			glAttachShader(s.program, fs);
			glLinkProgram(s.program);
			if(glGetProgrami(s.program, GL_LINK_STATUS) == GL_FALSE) {
				String log = glGetProgramInfoLog(s.program, glGetProgrami(s.program, GL_INFO_LOG_LENGTH));
				throw new RuntimeException("Program link failed!\n" + log);
			}

			glDetachShader(s.program, vs);
			glDetachShader(s.program, fs);
			glDeleteShader(vs);
			glDeleteShader(fs);
		} catch(Exception e) {
			System.err.println("Failed to load shader program: " + name);
			e.printStackTrace(System.err);

			if(s.program > 0) {
				glDeleteProgram(s.program);
				s.program = 0;
			}
			if(vs > -1) {
				glDeleteShader(vs);
			}
			if(fs > -1) {
				glDeleteShader(fs);
			}
		}
		programs.put(name, s);
		return s;
	}

	private static int load(String src, int type) throws Exception {
		int sh = -1;
		try {
			ArrayList<String> shaderSrc = Utils.readLines("shaders/" + src + ".glsl");
			shaderSrc.add(1, "#define " + (type == GL_VERTEX_SHADER ? "__VERTEX_SHADER__" : type == GL_FRAGMENT_SHADER ? "__FRAGMENT_SHADER__" : "__UNKNOWN_SHADER__"));
			String finalShaderSrc = replaceIncludes(shaderSrc);
			
			sh = glCreateShader(type);
			glShaderSource(sh, finalShaderSrc);
			glCompileShader(sh);
			if(glGetShaderi(sh, GL_COMPILE_STATUS) == GL_FALSE) {
				String log = glGetShaderInfoLog(sh, glGetShaderi(sh, GL_INFO_LOG_LENGTH));
				if(DEBUG_SOURCE_ON_ERROR) {
					log += "\n" + finalShaderSrc;
				}
				throw new RuntimeException("Failed to compile shader " + src + "!\n" + log);
			}
			return sh;
		} catch(Exception e) {
			if(sh > -1) {
				glDeleteShader(sh);
			}
			throw e;
		}
	}
	
	private static String replaceIncludes(ArrayList<String> src) throws Exception {		
		boolean acceptIncludes = true;
		for(int i = 0; i < src.size(); i++) {
			String ln = src.get(i);
			if(ln.startsWith("#include") && ln.contains("<") && ln.contains(">")) {
				if(!acceptIncludes) {
					throw new RuntimeException("Ln " + i + ": Shader include not declared in header");
				}
				int start = ln.indexOf("<") + 1;
				int end = ln.indexOf(">");
				ln = getInclude(ln.substring(start, end));
				src.remove(i);
				src.add(i, ln);
			} else if(ln.isEmpty() || !ln.startsWith("#")) {
				acceptIncludes = false;
			}
		}
		return Utils.joinList(src, "\n");
	}
	
	private static String getInclude(String name) throws Exception {
		if(includes.containsKey(name)) {
			return includes.get(name);
		}
		ArrayList<String> src = Utils.readLines("shaders/" + name);
		String finalSrc = replaceIncludes(src);
		includes.put(name, finalSrc);
		return finalSrc;
	}

	public static void deleteAll() {
		for(Shader shader : programs.values()) {
			glDeleteProgram(shader.program);
		}
		programs.clear();
		includes.clear();
		System.gc();
	}

}