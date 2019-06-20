package com.mc.gl.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mc.gl.objects.GlJobs;
import com.mc.utils.Async;
import com.mc.utils.Utils;
import com.mc.world.World;
import com.mc.world.block.Block;
import com.mc.world.block.RenderQueue;

public class Chunk {

	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_DEPTH = 16;

	public static int chunkUpdates = 0;

	private final int vao;
	private final int vbo;
	private final int[] renderOffsets = new int[RenderQueue.values().length];
	private final int[] renderLengths = new int[RenderQueue.values().length];

	private boolean isDirty = false;
	private boolean isCulled = false;
	private int blockCount = 0;

	private final World world;
	public final int chunkX;
	public final int chunkZ;
	private final Vector3fc center;
	public float distFromCameraSqr = 0f;

	public Chunk(World world, int x, int z) {
		this.world = world;
		this.chunkX = x;
		this.chunkZ = z;

		float minX = (float)chunkX * (float)CHUNK_WIDTH;
		float minY = 0f;
		float minZ = (float)chunkZ * (float)CHUNK_DEPTH;
		float maxX = minX + (float)CHUNK_WIDTH;
		float maxY = (float)world.getHeight();
		float maxZ = minZ + (float)CHUNK_DEPTH;
		float aveX = (minX+maxX)/2f;
		float aveY = (minY+maxY)/2f;
		float aveZ = (minZ+maxZ)/2f;
		this.center = new Vector3f(aveX, aveY, aveZ).toImmutable();

		this.vao = glGenVertexArrays();
		this.vbo = glGenBuffers();
		glBindVertexArray(this.vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
		glBufferData(GL_ARRAY_BUFFER, 33000 * 4, GL_DYNAMIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 8 * 4, 3 * 4);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 8 * 4, 5 * 4);

		this.markDirty();
		this.updateChunk();
	}

	public Vector3fc getCenter() {
		return this.center;
	}

	public boolean isDirty() {
		return this.isDirty;
	}

	public void markDirty() {
		this.isDirty = true;
	}

	public boolean isCulled() {
		return this.isCulled || this.blockCount <= 0;
	}

	public void setCulled(boolean culled) {
		this.isCulled = culled;
	}

	private void updateChunk() {
		if(!this.isDirty) {
			return;
		}
		this.isDirty = false;
		this.generateMesh();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void generateMesh() {
		Async.execute(() -> {
			ArrayList[] vertices = new ArrayList[RenderQueue.values().length];
			for(int i = 0; i < vertices.length; i++) {
				vertices[i] = new ArrayList<Float>();
			}

			int xMin = chunkX * CHUNK_WIDTH;
			int xMax = chunkX * CHUNK_WIDTH + CHUNK_WIDTH;
			int yMin = 0;
			int yMax = world.getHeight();
			int zMin = chunkZ * CHUNK_DEPTH;
			int zMax = chunkZ * CHUNK_DEPTH + CHUNK_DEPTH;
			this.blockCount = 0;
			for(int x = xMin; x < xMax; x++) {
				for(int y = yMin; y < yMax; y++) {
					for(int z = zMin; z < zMax; z++) {
						byte block = this.world.getBlock(x, y, z);
						if(block >= 0 && Block.blocks[block] != null && block != Block.air.id) {
							ArrayList list = vertices[Block.blocks[block].getRenderQueue().ordinal()];							
							if(BlockMesher.generateBlockMesh(world, Block.blocks[block], x, y, z, list)) {
								this.blockCount++;
							}
						}
					}
				}
			}

			ArrayList<Float> allGeometry = new ArrayList<Float>();
			for(int i = 0; i < vertices.length; i++) {
				allGeometry.addAll(vertices[i]);
			}

			if(!allGeometry.isEmpty()) {
				GlJobs.getQueue().queueJob(() -> {
					glBindVertexArray(this.vao);
					glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
					glBufferData(GL_ARRAY_BUFFER, allGeometry.size() * 4, GL_STREAM_DRAW);
					glBufferSubData(GL_ARRAY_BUFFER, 0, Utils.storeFloatList(allGeometry));
					glBindBuffer(GL_ARRAY_BUFFER, 0);
					glBindVertexArray(0);

					int offset = 0;
					for(RenderQueue queue : RenderQueue.values()) {
						renderOffsets[queue.ordinal()] = offset;
						renderLengths[queue.ordinal()] = vertices[queue.ordinal()].size() / 8;
						offset += renderLengths[queue.ordinal()];
					}
				});
			}
		});	

		chunkUpdates++;
	}

	public void preRender() {
		if(this.isDirty) {
			this.updateChunk();
		}
		glBindVertexArray(this.vao);
	}

	public void render(RenderQueue queue) {
		glDrawArrays(GL_TRIANGLES, this.renderOffsets[queue.ordinal()], this.renderLengths[queue.ordinal()]);
	}

	//	public void render(RenderQueue queue) {
	//		if(this.isCulled()) {
	//			return;
	//		}
	//		if(this.isDirty) {
	//			this.updateChunk();
	//		}
	//		glBindVertexArray(this.vao);
	//		glDrawArrays(GL_TRIANGLES, this.renderOffsets[queue.ordinal()], this.renderLengths[queue.ordinal()]);
	//	}

	public void delete() {
		glDeleteVertexArrays(this.vao);
		glDeleteBuffers(this.vbo);
	}

}