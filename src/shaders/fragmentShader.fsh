#version 330 core

in vec2 pass_textureCoords;

// The surface normal and the vector pointing at the light.
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D textureSampler;

uniform vec3 lightColour;

void main(void)
{
	// We first normalize both vectors to set their length to 1, so only they direction
	// matters.
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.0);
	vec3 diffuse = brightness * lightColour;

    out_Color = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
}