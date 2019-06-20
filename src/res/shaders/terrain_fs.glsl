#version 330 core
#include <lighting.glsl>
#include <fog.glsl>

in vec2 v_texCoords;
in vec3 v_eye;
in vec3 v_blockData;
in vec3 v_worldPos;

uniform vec3 faceNormal[6];
uniform sampler2D diffuseTex;

out vec4 fragColor;

void main() {
	vec4 tex = texture(diffuseTex, v_texCoords);
	if(tex.w < 0.5) {
		discard;
		return;
	}

	vec3 normal = faceNormal[int(v_blockData.y)];
	if(v_blockData.z > 0.) {
		normal = normalize(-cross(dFdy(v_worldPos), dFdx(v_worldPos)));
	}
	vec3 eye  = normalize(v_eye);
	vec3 refl = normalize(-reflect(eye, normal));
	
	float shadow = sampleShadowMap(v_shadowCoords);
	vec3 diffuse = calcDiffuseLighting(normal, shadow);
	vec3 specular = calcSpecularLighting(refl, shadow, 1., 15.);
	
	fragColor.xyz = tex.xyz * diffuse + specular;
	fragColor.w = tex.w;
	applyFog(fragColor);
	
	//fragColor.xyz = mix(fragColor.xyz, vec3(1.,.2,.2), .5 * (1.-v_shadowCoords.w));
}