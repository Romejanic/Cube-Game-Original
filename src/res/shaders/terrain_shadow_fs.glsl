#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D tex;

void main() {
	if(texture(tex, v_texCoords).w < 0.5) {
		discard;
	}
	fragColor = vec4(1.);
}