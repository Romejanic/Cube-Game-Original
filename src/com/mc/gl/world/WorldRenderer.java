package com.mc.gl.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.Arrays;
import java.util.Comparator;

import com.mc.camera.Camera;
import com.mc.camera.Frustum;
import com.mc.gl.ShadowRenderer;
import com.mc.gl.Window;
import com.mc.gl.objects.Shader;
import com.mc.gl.objects.Texture;
import com.mc.world.World;
import com.mc.world.WorldListener;
import com.mc.world.block.Facing;
import com.mc.world.block.RenderQueue;

public class WorldRenderer implements WorldListener {

	public static final String[] ATLAS_NAMES = { "atlas_default", "atlas_minecraft", "atlas_eden" };
	public static final int DEFAULT_ATLAS = 0;
	public static final float GRASS_LOD_DISTANCE = 100f;
	public static final boolean RENDER_CHUNK_ORDER = false;

	private Chunk[] chunks;
	private final int xChunks;
	private final int zChunks;
	private final int chunkHeight;
	private final ChunkSorter chunkSorter;

	private final Shader terrainShader;
	private final Shader shadowShader;

	public WorldRenderer(World world) {
		if(world.getWidth() % 16 != 0 || world.getHeight() % 16 != 0 || world.getDepth() % 16 != 0) {
			throw new IllegalArgumentException("World dimensions not divisible by 16!");
		}
		world.addWorldListener(this);
		this.xChunks = world.getWidth() / 16;
		this.zChunks = world.getDepth() / 16;
		this.chunkHeight = world.getHeight();
		this.chunks = new Chunk[this.xChunks * this.zChunks];
		for(int x = 0; x < this.xChunks; x++) {
			for(int z = 0; z < this.zChunks; z++) {
				this.chunks[z*this.xChunks+x] = new Chunk(world, x, z);
			}
		}
		this.chunkSorter = new ChunkSorter();

		Texture terrainTexture = Texture.get(ATLAS_NAMES[DEFAULT_ATLAS]);

		this.terrainShader = Shader.get("terrain");
		this.terrainShader.uniform("diffuseTex").set(terrainTexture);
		for(Facing face : Facing.values()) {
			this.terrainShader.uniform("faceNormal[" + face.id + "]").set(face.normal);
		}

		this.shadowShader = Shader.get("terrain_shadow");
		this.shadowShader.uniform("tex").set(terrainTexture);
	}

	public void preRender(Camera camera, SunLight sun) {
		for(Chunk chunk : this.chunks) {
			chunk.distFromCameraSqr = camera.position.distanceSquared(chunk.getCenter());
		}
		this.chunkSorter.furthestFirst = true;
		Arrays.sort(this.chunks, this.chunkSorter);
		camera.uploadUniforms(this.terrainShader);
		sun.uploadUniforms(this.terrainShader);
		this.terrainShader.uniform("time").set(Window.getTime());
		this.terrainShader.uniform("fogColor").set(Skybox.BOTTOM_COLOR);
		this.shadowShader.uniform("time").set(Window.getTime());

		for(int i = 0; i < ATLAS_NAMES.length; i++) {
			if(Window.isKeyPressed(Window.KEY_1 + i)) {
				Texture atlas = Texture.get(ATLAS_NAMES[i]);
				this.terrainShader.uniform("diffuseTex").set(atlas);
				this.shadowShader.uniform("tex").set(atlas);
				break;
			}
		}
	}

	public void render(Camera camera, ShadowRenderer shadowmap, boolean isShadow) {
		Shader shader = isShadow ? this.shadowShader : this.terrainShader;
		shadowmap.uploadUniforms(shader, isShadow);
		shader.bind();
		float grassLodSqr = GRASS_LOD_DISTANCE * GRASS_LOD_DISTANCE;
		this.renderOpaqueChunks(grassLodSqr);
		if(!isShadow) {
			this.renderTransparentChunks();
		}
		glBindVertexArray(0);
		shader.unbind();
	}

	private void renderOpaqueChunks(float grassLodSqr) {
		for(int i = 0; i < this.chunks.length; i++) {
			Chunk chunk = this.chunks[i];
			if(chunk == null || chunk.isCulled()) {
				continue;
			}
			chunk.preRender();
			chunk.render(RenderQueue.NORMAL);
			if(chunk.distFromCameraSqr <= grassLodSqr) {
				glDisable(GL_CULL_FACE);
				chunk.render(RenderQueue.NO_CULL);
				glEnable(GL_CULL_FACE);
			}
		}
	}
	
	private void renderTransparentChunks() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		for(int i = 0; i < this.chunks.length; i++) {
			Chunk chunk = this.chunks[i];
			if(chunk == null || chunk.isCulled()) {
				continue;
			}
			chunk.preRender();
			chunk.render(RenderQueue.TRANSPARENT);
		}
		glDisable(GL_BLEND);
	}

	private void renderQueueOrder(boolean isShadow, float grassLodSqr) {
		for(RenderQueue queue : RenderQueue.values()) {
			if(isShadow && !queue.shouldDrawShadows()) {
				continue;
			}
			if(queue == RenderQueue.NO_CULL) {
				glDisable(GL_CULL_FACE);
			}
			if(queue == RenderQueue.TRANSPARENT) {
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}
			for(int i = 0; i < this.chunks.length; i++) {
				Chunk chunk = this.chunks[i];
				if(chunk == null || chunk.isCulled()) {
					continue;
				}
				if(queue == RenderQueue.NO_CULL && chunk.distFromCameraSqr > grassLodSqr) {
					continue; // cull grass/flowers at far distance
				}
				chunk.preRender();
				chunk.render(queue);
			}
			if(queue == RenderQueue.NO_CULL) {
				glEnable(GL_CULL_FACE);
			}
			if(queue == RenderQueue.TRANSPARENT) {
				glDisable(GL_BLEND);
			}
		}
	}

	private void renderChunkOrder(boolean isShadow, float grassLodSqr) {
		for(Chunk chunk : this.chunks) {
			if(chunk == null || chunk.isCulled()) {
				continue;
			}
			chunk.preRender();
			for(RenderQueue queue : RenderQueue.values()) {
				if(isShadow && !queue.shouldDrawShadows()) {
					continue;
				}
				if(queue == RenderQueue.NO_CULL) {
					if(chunk.distFromCameraSqr > grassLodSqr) {
						continue;
					}
					glDisable(GL_CULL_FACE);
				}
				if(queue == RenderQueue.TRANSPARENT) {
					glEnable(GL_BLEND);
					glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				}
				chunk.render(queue);
				if(queue == RenderQueue.NO_CULL) {
					glEnable(GL_CULL_FACE);
				}
				if(queue == RenderQueue.TRANSPARENT) {
					glDisable(GL_BLEND);
				}
			}
		}
	}

	public void cullChunks(Frustum frustum) {
		for(Chunk chunk : this.chunks) {
			chunk.setCulled(frustum.cubeOutsideFrustum(chunk.getCenter(), 8f, (float)chunkHeight / 2f, 8f));
		}
	}

	public void delete() {
		for(Chunk chunk : this.chunks) {
			if(chunk == null) {
				continue;
			}
			chunk.delete();
		}
	}

	private void updateChunk(int x, int y, int z) {
		if(x < 0 || y < 0 || z < 0 || x >= xChunks || z >= zChunks) {
			return;
		}
		for(Chunk chunk : this.chunks) {
			if(chunk.chunkX == x && chunk.chunkZ == z) {
				chunk.markDirty();
				return;
			}
		}
	}

	@Override
	public void allBlocksUpdated() {
		for(Chunk chunk : this.chunks) {
			chunk.markDirty();
		}
	}

	@Override
	public void onBlockUpdate(int x, int y, int z, byte oldID, byte newID) {
		updateChunk(x/16, y/16, z/16);
		updateChunk((x-1)/16, y/16, z/16);
		updateChunk((x+1)/16, y/16, z/16);
		updateChunk(x/16, (y-1)/16, z/16);
		updateChunk(x/16, (y+1)/16, z/16);
		updateChunk(x/16, y/16, (z-1)/16);
		updateChunk(x/16, y/16, (z+1)/16);
	}

	public Chunk getChunk(int x, int z) {
		return this.chunks[z*this.xChunks+x];
	}

	class ChunkSorter implements Comparator<Chunk> {

		private boolean furthestFirst = true;

		@Override
		public int compare(Chunk o1, Chunk o2) {
			if(this.furthestFirst) {
				return (int)(o2.distFromCameraSqr - o1.distFromCameraSqr);
			} else {
				return (int)(o1.distFromCameraSqr - o2.distFromCameraSqr);
			}
		}

	}

}