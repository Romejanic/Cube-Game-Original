#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D colorTex;

void main() {
	fragColor = texture(colorTex, v_texCoords);
	fragColor.xyz = 1. - fragColor.xyz;
}