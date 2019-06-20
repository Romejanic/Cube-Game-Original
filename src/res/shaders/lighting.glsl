#ifdef __VERTEX_SHADER__
out vec4 v_shadowCoords;
uniform mat4 shadowProj;
uniform mat4 shadowView;
uniform float shadowDist;

const float shadowFade = 5.;

void calcShadowCoords(in vec4 worldPos, in float camDist) {
	vec4 shadowCoords  = shadowProj * shadowView * worldPos;
	v_shadowCoords.xyz = (shadowCoords.xyz/shadowCoords.w) * .5 + .5;
	v_shadowCoords.w   = 1. - clamp((camDist-(shadowDist-shadowFade))/shadowFade,0.,1.);
}
#endif
#ifdef __FRAGMENT_SHADER__
#define SHADOW_FILTERING_POISSON
#define SHADOW_BIAS .0005

#ifdef SHADOW_FILTERING_POISSON
vec2 poissonDisk[9] = vec2[]
(
   vec2(0.95581, -0.18159), vec2(0.50147, -0.35807), vec2(0.69607, 0.35559),
   vec2(-0.003682, -0.5915), vec2(0.1593, 0.08975), vec2(-0.6503, 0.05818),
   vec2(0.11915, 0.78449), vec2(-0.34296, 0.51575), vec2(-0.6038, -0.41527)
);
#endif
in vec4 v_shadowCoords;

uniform vec3 sunColor;
uniform vec3 sunDirection;
uniform sampler2DShadow shadowmap;
uniform int shadowmapSize;

float sampleShadowMap(vec4 shadowCoords) {
	#ifdef SHADOW_FILTERING_POISSON
		float shadow = 1.;
		float shadowWeight = (1./9.) * shadowCoords.w;
		float texelSize = 2./float(shadowmapSize);
		for(int i = 0; i < 9; i++) {
			shadow -= shadowWeight * (1.-texture(shadowmap, vec3(shadowCoords.xy+poissonDisk[i]*texelSize,shadowCoords.z-SHADOW_BIAS)));
		}
		return shadow;
	#else
		return texture(shadowmap, vec3(shadowCoords.xy,shadowCoords.z-SHADOW_BIAS));
	#endif
}

vec3 calcDiffuseLighting(vec3 normal, float shadow) {
	return sunColor * max(dot(sunDirection,normal)*shadow,.2);
}

vec3 calcSpecularLighting(vec3 refl, float shadow, float intensity, float shininess) {
	return sunColor * pow(max(dot(sunDirection,refl),0.),shininess) * shadow * intensity; 
}
#endif