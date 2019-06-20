#version 330 core
#define KERNEL_SIZE 11

const float[KERNEL_SIZE] kernel = float[KERNEL_SIZE](
	0.0093, 0.028002, 0.065984, 0.121703, 0.175713,
	0.198596, 0.175713, 0.121703, 0.065984, 0.028002,
	0.0093
);

in vec2 v_texCoords[11];
out vec4 fragColor;

uniform sampler2D colorTex;

void main() {
	fragColor = vec4(0., 0., 0., 1.);
	for(int i = 0; i < KERNEL_SIZE; i++) {
		fragColor.xyz += texture(colorTex, v_texCoords[i]).xyz * kernel[i];
	}
}