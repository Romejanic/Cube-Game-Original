#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D texA;
uniform sampler2D texB;

void main() {
	fragColor = texture(texA, v_texCoords) + texture(texB, v_texCoords);
	fragColor.w = 1.;
}