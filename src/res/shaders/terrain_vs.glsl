#version 330 core
#include <vertex_waving.glsl>
#include <lighting.glsl>
#include <fog.glsl>

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 blockData;

uniform mat4 projMat;
uniform mat4 viewMat;
uniform vec3 cameraPos;

out vec2 v_texCoords;
out vec3 v_eye;
out vec3 v_blockData;
out vec3 v_worldPos;

void main() {
	vec4 worldPos = vec4(vertex, 1.);
	v_eye = cameraPos - worldPos.xyz;
	float camDist = length(v_eye);
	if(blockData.z > 0.) {
		worldPos.xyz = applyDistortion(worldPos.xyz, blockData.z, camDist);
		v_eye = cameraPos - worldPos.xyz;
	}
	gl_Position = projMat * viewMat * worldPos;
	v_texCoords = texCoords;
	v_blockData = blockData;
	v_worldPos = worldPos.xyz;
	
	calcShadowCoords(worldPos, camDist);
	applyFog(camDist);
}