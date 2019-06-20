#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D colorTex;
uniform sampler2D depthTex;

void main() {
	fragColor = texture(colorTex, v_texCoords);
}