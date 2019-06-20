#version 330 core
#include <lighting.glsl>
#include <fog.glsl>

in vec2 v_texCoords;
in vec3 v_normal;
in vec3 v_eye;

uniform sampler2D diffuseTex;
out vec4 fragColor;

void main() {
	vec4 tex = texture(diffuseTex, v_texCoords);
	
	vec3 normal = normalize(v_normal);
	vec3 eye    = normalize(v_eye);
	vec3 refl   = normalize(-reflect(eye, normal));
	
	float shadow = sampleShadowMap(v_shadowCoords);
	vec3 diffuse = calcDiffuseLighting(normal, shadow);
	vec3 specular = calcSpecularLighting(refl, shadow, 1., 15.);
	
	fragColor.xyz = tex.xyz * diffuse + specular;
	fragColor.w = tex.w;
	applyFog(fragColor);
}