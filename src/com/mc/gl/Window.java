package com.mc.gl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window {
	
	public static final int KEY_W = GLFW_KEY_W;
	public static final int KEY_A = GLFW_KEY_A;
	public static final int KEY_S = GLFW_KEY_S;
	public static final int KEY_D = GLFW_KEY_D;
	public static final int KEY_Q = GLFW_KEY_Q;
	public static final int KEY_E = GLFW_KEY_E;
	public static final int KEY_F = GLFW_KEY_F;
	public static final int KEY_LEFT = GLFW_KEY_LEFT;
	public static final int KEY_RIGHT = GLFW_KEY_RIGHT;
	public static final int KEY_UP = GLFW_KEY_UP;
	public static final int KEY_DOWN = GLFW_KEY_DOWN;
	public static final int KEY_LSHIFT = GLFW_KEY_LEFT_SHIFT;
	public static final int KEY_SPACE = GLFW_KEY_SPACE;
	public static final int KEY_R = GLFW_KEY_R;
	public static final int KEY_1 = GLFW_KEY_1;
	public static final int KEY_2 = GLFW_KEY_2;
	public static final int KEY_3 = GLFW_KEY_3;
	public static final int KEY_4 = GLFW_KEY_4;
	public static final int KEY_5 = GLFW_KEY_5;
	public static final int KEY_6 = GLFW_KEY_6;
	public static final int KEY_7 = GLFW_KEY_7;
	public static final int KEY_8 = GLFW_KEY_8;
	public static final int KEY_9 = GLFW_KEY_9;
	public static final int KEY_0 = GLFW_KEY_0;
	private static final int[] KEY_CODES = { KEY_W, KEY_A, KEY_S, KEY_D, KEY_Q, KEY_E, KEY_F, KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN, KEY_LSHIFT, KEY_SPACE, KEY_R, KEY_1, KEY_2, KEY_3 };
	private static final ArrayList<Integer> keysDown = new ArrayList<Integer>();
	
	private static double mouseX  = 0d;
	private static double mouseY  = 0d;
	private static double mouseDX = 0d;
	private static double mouseDY = 0f;
	
	private static final DoubleBuffer mouseBX = BufferUtils.createDoubleBuffer(1);
	private static final DoubleBuffer mouseBY = BufferUtils.createDoubleBuffer(1);
	
	private static long window  = NULL;
	private static int width    = 1024;
	private static int height   = 640;
	private static String title = "GLFW Window";
	private static boolean hasFocus = true;
	
	private static float lastFrame = 0f;
	private static float delta = 1f;
	
	public static void create() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(!glfwInit()) {
			throw new RuntimeException("GLFW failed to initialize!");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if(window == NULL) {
			throw new RuntimeException("Window couldn't be created!");
		}
		glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
			width = w; height = h;
		});
		glfwSetWindowFocusCallback(window, (win, focused) -> {
			hasFocus = focused;
		});
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
	
	public static void update() {
		float time = getTime();
		delta = (time - lastFrame);
		lastFrame = time;
		
		for(int i : KEY_CODES) {
			if(isKeyDown(i) && !keysDown.contains(i)) {
				keysDown.add(i);
			} else if(!isKeyDown(i) && keysDown.contains(i)) {
				keysDown.remove(keysDown.indexOf(i));
			}
		}
		
		mouseBX.clear(); mouseBY.clear();
		glfwGetCursorPos(window, mouseBX, mouseBY);
		mouseDX = mouseBX.get(0) - mouseX;
		mouseDY = mouseBY.get(0) - mouseY;
		mouseX = mouseBX.get(0);
		mouseY = mouseBY.get(0);
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public static void destroy() {
		if(window != NULL) {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			Callbacks.glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
		}
		glfwTerminate();
	}
	
	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
	}
	
	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static float getTime() {
		return (float)glfwGetTime();
	}
	
	public static float getDelta() {
		return delta;
	}
	
	public static void setTitle(String t) {
		title = t;
		if(window != NULL) {
			glfwSetWindowTitle(window, t);
		}
	}
	
	public static boolean hasFocus() {
		return hasFocus;
	}
	
	public static boolean isKeyDown(int keyCode) {
		return hasFocus() && glfwGetKey(window, keyCode) == GLFW_PRESS;
	}
	
	public static boolean isKeyPressed(int keyCode) {
		return hasFocus() && isKeyDown(keyCode) && !keysDown.contains(keyCode);
	}
	
	public static double getMouseX() {
		return mouseX;
	}
	
	public static double getMouseY() {
		return mouseY;
	}
	
	public static double getMouseDX() {
		return mouseDX;
	}
	
	public static double getMouseDY() {
		return mouseDY;
	}
	
}