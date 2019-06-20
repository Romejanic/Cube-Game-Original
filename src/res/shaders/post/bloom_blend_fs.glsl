#version 330 core
#define BLEND_FUNC_ADD // replace with BLEND_FUNC_SCREEN for screen blending

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D colorTex;
uniform sampler2D blurTex;
uniform sampler2D lensDirt;
uniform sampler2D dirtTex;

void main() {
	vec4 baseColor = texture(colorTex, v_texCoords);
	vec4 glowColor = texture(blurTex, v_texCoords);
	vec4 bloomColor = glowColor + texture(lensDirt, v_texCoords) * texture(dirtTex, v_texCoords);

#ifdef BLEND_FUNC_SCREEN
	fragColor = 1.-(1.-baseColor)*(1.-(bloomColor));
#else
	fragColor = baseColor + bloomColor;
#endif
	fragColor.w = 1.;
}