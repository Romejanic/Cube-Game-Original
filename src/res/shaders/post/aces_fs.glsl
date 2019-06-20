#version 330 core

in vec2 v_texCoords;
out vec4 fragColor;

uniform sampler2D colorTex;
uniform sampler2D depthTex;

// Source: https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/
vec3 ACESFilm(vec3 x) {
    return clamp((x*(2.51*x+0.03))/(x*(2.43*x+0.59)+0.14), 0., 1.);
}

void main() {
	fragColor = texture(colorTex, v_texCoords);
	fragColor.xyz = ACESFilm(fragColor.xyz);
}