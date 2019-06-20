#version 330 core

in vec3 v_eyeDir;
out vec4 fragColor;

uniform vec3 sunColor;
uniform vec3 sunDirection;
uniform vec3 bottomColor;
uniform vec3 topColor;

void main() {
	vec3 eyeDir = normalize(v_eyeDir);
	vec3 skyColor = mix(bottomColor, topColor, clamp(eyeDir.y, 0., 1.));
	
	float sunDist = length(eyeDir-sunDirection);
	float sunFact = 1. - smoothstep(0., .1, sunDist);
	
	vec3 final = skyColor + sunColor * sunFact * 1.5;
	fragColor  = vec4(final, 1.);
}