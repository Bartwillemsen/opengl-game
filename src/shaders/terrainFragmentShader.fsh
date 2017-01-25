#version 330 core

in vec2 pass_textureCoords;

// The surface normal and the vector pointing at the light.
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;

void main(void)
{
	// We first normalize both vectors to set their length to 1, so only they direction matters.
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float brightness = max(dot(unitNormal, unitLightVector), 0.2);
	vec3 diffuse = brightness * lightColour;

	vec3 unitVectorToCamera = normalize(toCameraVector);

	// The light direction is the oposite of the vector pointing towards the light.
	vec3 reflectedLightDirection = reflect(-unitLightVector, unitNormal);

	// We do a dot calculation to see how much of the reflected light goes into the camera.
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);

	// Apply the damping of the material by raising the specularFactor to the power of the damping
	// value. This makes low specular factors even lower, but doesn't affect the higher values
	// as much.
	float dampedFactor = pow(specularFactor, shineDamper);

	// Make sure that the highlight is the same color as the material color.
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    out_Color = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular, 1.0);
}