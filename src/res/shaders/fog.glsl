#ifdef __VERTEX_SHADER__
out float v_fog;

const float fogDensity = .007;
const float fogGradient = 1.5;

void applyFog(float camDist) {
	v_fog = clamp(exp(-pow((camDist*fogDensity),fogGradient)),0.,1.);
}
#endif
#ifdef __FRAGMENT_SHADER__
in float v_fog;
uniform vec3 fogColor;

void applyFog(inout vec4 fragColor) {
	fragColor = mix(vec4(fogColor,1.), fragColor, v_fog);
}
#endif