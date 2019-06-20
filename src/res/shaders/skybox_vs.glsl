#version 330 core

layout(location = 0) in vec3 vertex;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec3 v_eyeDir;

void main() {
	gl_Position = projMat * viewMat * vec4(vertex, 1.);
	v_eyeDir = vertex;
}