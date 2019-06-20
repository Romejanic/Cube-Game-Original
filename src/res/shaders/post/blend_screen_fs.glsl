#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D texA;
uniform sampler2D texB;

void main() {
	fragColor = 1.-(1.-texture(texA, v_texCoords))*(1.-texture(texB, v_texCoords));
}