#version 330 core

layout(location = 0) in vec2 vertex;
out vec3 v_texCoords;

void main() {
	gl_Position = vec4(vertex, 0., 1.);
	v_texCoords = vec3(vertex, 1.);
}