#version 330 core
#include <lighting.glsl>
#include <fog.glsl>

layout(location = 0) in vec3 vertex;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 normal;

uniform mat4 projMat;
uniform mat4 viewMat;
uniform mat4 modelMat;
uniform vec3 cameraPos;

out vec2 v_texCoords;
out vec3 v_normal;
out vec3 v_eye;

void main() {
	vec4 worldPos = modelMat * vec4(vertex, 1.);
	vec4 eyePos = viewMat * worldPos;
	gl_Position = projMat * eyePos;
	
	v_texCoords = texCoords;
	v_normal = (modelMat * vec4(normal, 0.)).xyz;
	v_eye = cameraPos - worldPos.xyz;
	
	float camDist = length(eyePos);
	calcShadowCoords(worldPos, camDist);
	applyFog(camDist);
}