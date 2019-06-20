package com.mc.camera;

import org.joml.Vector3fc;

import com.mc.physics.AABB;

// Source: http://www.java-gaming.org/index.php?topic=29136.0
public class Frustum {

	public static final int RIGHT   = 0;
	public static final int LEFT    = 1;
	public static final int BOTTOM  = 2;
	public static final int TOP     = 3;
	public static final int BACK    = 4;
	public static final int FRONT   = 5;

	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;

	float[][] frustum = new float[6][4];

	public void normalizePlane(float[][] frustum, int side) {
		float magnitude = (float) Math.sqrt(frustum[side][A] * frustum[side][A] + frustum[side][B] * frustum[side][B] + frustum[side][C] * frustum[side][C]);

		frustum[side][A] /= magnitude;
		frustum[side][B] /= magnitude;
		frustum[side][C] /= magnitude;
		frustum[side][D] /= magnitude;
	}

	public void calculateFrustum(IView view) {
		float[] projectionMatrix = new float[16];
		float[] viewMatrix = new float[16];
		float[] clipMatrix = new float[16];

		view.getProjectionMatrix().get(projectionMatrix);
		view.getViewMatrix().get(viewMatrix);

		clipMatrix[ 0] = viewMatrix[ 0] * projectionMatrix[ 0] + viewMatrix[ 1] * projectionMatrix[ 4] + viewMatrix[ 2] * projectionMatrix[ 8] + viewMatrix[ 3] * projectionMatrix[12];
		clipMatrix[ 1] = viewMatrix[ 0] * projectionMatrix[ 1] + viewMatrix[ 1] * projectionMatrix[ 5] + viewMatrix[ 2] * projectionMatrix[ 9] + viewMatrix[ 3] * projectionMatrix[13];
		clipMatrix[ 2] = viewMatrix[ 0] * projectionMatrix[ 2] + viewMatrix[ 1] * projectionMatrix[ 6] + viewMatrix[ 2] * projectionMatrix[10] + viewMatrix[ 3] * projectionMatrix[14];
		clipMatrix[ 3] = viewMatrix[ 0] * projectionMatrix[ 3] + viewMatrix[ 1] * projectionMatrix[ 7] + viewMatrix[ 2] * projectionMatrix[11] + viewMatrix[ 3] * projectionMatrix[15];

		clipMatrix[ 4] = viewMatrix[ 4] * projectionMatrix[ 0] + viewMatrix[ 5] * projectionMatrix[ 4] + viewMatrix[ 6] * projectionMatrix[ 8] + viewMatrix[ 7] * projectionMatrix[12];
		clipMatrix[ 5] = viewMatrix[ 4] * projectionMatrix[ 1] + viewMatrix[ 5] * projectionMatrix[ 5] + viewMatrix[ 6] * projectionMatrix[ 9] + viewMatrix[ 7] * projectionMatrix[13];
		clipMatrix[ 6] = viewMatrix[ 4] * projectionMatrix[ 2] + viewMatrix[ 5] * projectionMatrix[ 6] + viewMatrix[ 6] * projectionMatrix[10] + viewMatrix[ 7] * projectionMatrix[14];
		clipMatrix[ 7] = viewMatrix[ 4] * projectionMatrix[ 3] + viewMatrix[ 5] * projectionMatrix[ 7] + viewMatrix[ 6] * projectionMatrix[11] + viewMatrix[ 7] * projectionMatrix[15];

		clipMatrix[ 8] = viewMatrix[ 8] * projectionMatrix[ 0] + viewMatrix[ 9] * projectionMatrix[ 4] + viewMatrix[10] * projectionMatrix[ 8] + viewMatrix[11] * projectionMatrix[12];
		clipMatrix[ 9] = viewMatrix[ 8] * projectionMatrix[ 1] + viewMatrix[ 9] * projectionMatrix[ 5] + viewMatrix[10] * projectionMatrix[ 9] + viewMatrix[11] * projectionMatrix[13];
		clipMatrix[10] = viewMatrix[ 8] * projectionMatrix[ 2] + viewMatrix[ 9] * projectionMatrix[ 6] + viewMatrix[10] * projectionMatrix[10] + viewMatrix[11] * projectionMatrix[14];
		clipMatrix[11] = viewMatrix[ 8] * projectionMatrix[ 3] + viewMatrix[ 9] * projectionMatrix[ 7] + viewMatrix[10] * projectionMatrix[11] + viewMatrix[11] * projectionMatrix[15];

		clipMatrix[12] = viewMatrix[12] * projectionMatrix[ 0] + viewMatrix[13] * projectionMatrix[ 4] + viewMatrix[14] * projectionMatrix[ 8] + viewMatrix[15] * projectionMatrix[12];
		clipMatrix[13] = viewMatrix[12] * projectionMatrix[ 1] + viewMatrix[13] * projectionMatrix[ 5] + viewMatrix[14] * projectionMatrix[ 9] + viewMatrix[15] * projectionMatrix[13];
		clipMatrix[14] = viewMatrix[12] * projectionMatrix[ 2] + viewMatrix[13] * projectionMatrix[ 6] + viewMatrix[14] * projectionMatrix[10] + viewMatrix[15] * projectionMatrix[14];
		clipMatrix[15] = viewMatrix[12] * projectionMatrix[ 3] + viewMatrix[13] * projectionMatrix[ 7] + viewMatrix[14] * projectionMatrix[11] + viewMatrix[15] * projectionMatrix[15];

		// This will extract the LEFT side of the frustum
		frustum[LEFT][A] = clipMatrix[ 3] + clipMatrix[ 0];
		frustum[LEFT][B] = clipMatrix[ 7] + clipMatrix[ 4];
		frustum[LEFT][C] = clipMatrix[11] + clipMatrix[ 8];
		frustum[LEFT][D] = clipMatrix[15] + clipMatrix[12];
		normalizePlane(frustum, LEFT);

		// This will extract the RIGHT side of the frustum
		frustum[RIGHT][A] = clipMatrix[ 3] - clipMatrix[ 0];
		frustum[RIGHT][B] = clipMatrix[ 7] - clipMatrix[ 4];
		frustum[RIGHT][C] = clipMatrix[11] - clipMatrix[ 8];
		frustum[RIGHT][D] = clipMatrix[15] - clipMatrix[12];
		normalizePlane(frustum, RIGHT);

		// This will extract the BOTTOM side of the frustum
		frustum[BOTTOM][A] = clipMatrix[ 3] + clipMatrix[ 1];
		frustum[BOTTOM][B] = clipMatrix[ 7] + clipMatrix[ 5];
		frustum[BOTTOM][C] = clipMatrix[11] + clipMatrix[ 9];
		frustum[BOTTOM][D] = clipMatrix[15] + clipMatrix[13];
		normalizePlane(frustum, BOTTOM);

		// This will extract the TOP side of the frustum
		frustum[TOP][A] = clipMatrix[ 3] - clipMatrix[ 1];
		frustum[TOP][B] = clipMatrix[ 7] - clipMatrix[ 5];
		frustum[TOP][C] = clipMatrix[11] - clipMatrix[ 9];
		frustum[TOP][D] = clipMatrix[15] - clipMatrix[13];
		normalizePlane(frustum, TOP);

		// This will extract the FRONT side of the frustum
		frustum[FRONT][A] = clipMatrix[ 3] + clipMatrix[ 2];
		frustum[FRONT][B] = clipMatrix[ 7] + clipMatrix[ 6];
		frustum[FRONT][C] = clipMatrix[11] + clipMatrix[10];
		frustum[FRONT][D] = clipMatrix[15] + clipMatrix[14];
		normalizePlane(frustum, FRONT);

		// This will extract the BACK side of the frustum
		frustum[BACK][A] = clipMatrix[ 3] - clipMatrix[ 2];
		frustum[BACK][B] = clipMatrix[ 7] - clipMatrix[ 6];
		frustum[BACK][C] = clipMatrix[11] - clipMatrix[10];
		frustum[BACK][D] = clipMatrix[15] - clipMatrix[14];
		normalizePlane(frustum, BACK);
	}

	public boolean cubeOutsideFrustum(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
		for(int i = 0; i < 6; i++ ) {
			if(frustum[i][A] * (x - sizeX) + frustum[i][B] * (y - sizeY) + frustum[i][C] * (z - sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x + sizeX) + frustum[i][B] * (y - sizeY) + frustum[i][C] * (z - sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x - sizeX) + frustum[i][B] * (y + sizeY) + frustum[i][C] * (z - sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x + sizeX) + frustum[i][B] * (y + sizeY) + frustum[i][C] * (z - sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x - sizeX) + frustum[i][B] * (y - sizeY) + frustum[i][C] * (z + sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x + sizeX) + frustum[i][B] * (y - sizeY) + frustum[i][C] * (z + sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x - sizeX) + frustum[i][B] * (y + sizeY) + frustum[i][C] * (z + sizeZ) + frustum[i][D] > 0)
				continue;
			if(frustum[i][A] * (x + sizeX) + frustum[i][B] * (y + sizeY) + frustum[i][C] * (z + sizeZ) + frustum[i][D] > 0)
				continue;
			return true;
		}
		return false;
	}
	
	public boolean cubeOutsideFrustum(Vector3fc position, float sizeX, float sizeY, float sizeZ) {
		return cubeOutsideFrustum(position.x(), position.y(), position.z(), sizeX, sizeY, sizeZ);
	}
	
	public boolean cubeOutsideFrustum(Vector3fc position, Vector3fc size) {
		return cubeOutsideFrustum(position, size.x(), size.y(), size.z());
	}
	
	public boolean cubeOutsideFrustum(AABB aabb) {
		return cubeOutsideFrustum(aabb.getCenter(), aabb.getSize());
	}

}