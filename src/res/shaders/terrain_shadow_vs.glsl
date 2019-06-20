#version 330 core
#include <vertex_waving.glsl>

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 blockData;

uniform mat4 shadowProj;
uniform mat4 shadowView;
out vec2 v_texCoords;

void main() {
	vec4 worldPos = vec4(vertex, 1.);
	if(blockData.z > 0.) {
		worldPos.xyz = applyDistortion(worldPos.xyz, blockData.z, 0.);
	}
	gl_Position = shadowProj * shadowView * worldPos;
	v_texCoords = texCoords;
}