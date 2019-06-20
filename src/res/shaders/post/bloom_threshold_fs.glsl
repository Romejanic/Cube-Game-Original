#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D colorTex;

float luma(vec3 col) {
	return col.x * .2126 + col.y * .7152 + col.z * .0722;
}

void main() {
	fragColor = texture(colorTex, v_texCoords);
	if(luma(fragColor.xyz) < 1.) {
		fragColor = vec4(0.);
	}
}