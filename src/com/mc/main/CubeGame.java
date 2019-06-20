package com.mc.main;

import com.mc.camera.Camera;
import com.mc.camera.hit.CameraHit;
import com.mc.gl.MasterRenderer;
import com.mc.gl.Window;
import com.mc.world.World;

public class CubeGame {

	public static final CubeGame instance = new CubeGame();

	public final MasterRenderer renderer = new MasterRenderer();
	public World world;

	public final Camera camera = new Camera(0f, 0.5f, 3f);
	//private final VoxelTracer tracer = new VoxelTracer();
	public CameraHit hit;

	private void init() throws Throwable {
		Window.setTitle("Cube Game");
		Window.create();

		if((this.world = World.load()) == null) {
			this.world = new World(256, 128, 256);
			this.world.generateNewWorld();
		}
		this.renderer.init(this.world);
		
		int camX = this.world.getWidth() / 2;
		int camZ = this.world.getDepth() / 2;
		int camY = this.world.getHeight(camX, camZ) + 3;
		this.camera.position.set((float)camX, (float)camY, (float)camZ);
	}

	private void update() {
		int screenW = Window.getWidth();
		int screenH = Window.getHeight();

		this.camera.update(screenW, screenH);
		this.world.tickWorld(Window.getDelta());
//		this.hit = this.tracer.trace(this.camera, this.world);
//		if(this.hit != null) {
//			this.world.setBlock(this.hit.x, this.hit.y, this.hit.z, Block.dirt.id);
//		}
		
		this.renderer.render(this.camera, this.world, screenW, screenH);
		Window.update();
	}

	private void destroy() {
		this.world.save();
		this.renderer.destroy();
		Window.destroy();
	}

	public static void main(String[] args) {
		try {
			instance.init();
			while(!Window.isCloseRequested()) {
				instance.update();
			}
			instance.destroy();
		} catch(Throwable e) {
			System.err.println("!! CRASHED !!");
			e.printStackTrace(System.err);
			instance.destroy();
		}
	}

}