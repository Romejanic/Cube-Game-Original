#version 330 core

in vec3 v_texCoords;
out vec4 fragColor;

uniform sampler2D colorTex;
uniform float intensity;
uniform vec3 refractiveIndex;

const vec3 normal = vec3(0., 0., -1.);

void main() {
	vec3 ri = mix(vec3(1.), refractiveIndex, intensity);
	vec3 rr = refract(v_texCoords, normal, ri.r);
	vec3 gr = refract(v_texCoords, normal, ri.g);
	vec3 br = refract(v_texCoords, normal, ri.b);
	
	vec2 rt = ((rr/rr.z).xy + 1.) * .5;
	vec2 gt = ((gr/gr.z).xy + 1.) * .5;
	vec2 bt = ((br/br.z).xy + 1.) * .5;
	
	fragColor = vec4(
		texture(colorTex, rt).r,
		texture(colorTex, gt).g,
		texture(colorTex, bt).b,
	1.);
}