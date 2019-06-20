#version 330 core
#define KERNEL_SIZE 11
#define KERNEL_HALF 5

layout(location = 0) in vec2 vertex;
out vec2 v_texCoords[KERNEL_SIZE];

uniform vec2 screenSize;
uniform vec2 axis;

void main() {
	gl_Position = vec4(vertex, 0., 1.);
	vec2 texCoords = vertex * .5 + .5;
	
	vec2 pixelSize = (1./screenSize) * axis;
	for(int i = -KERNEL_HALF; i <= KERNEL_HALF; i++) {
		v_texCoords[i+KERNEL_HALF] = texCoords + pixelSize * float(i);
	}
}