#version 330 core

// The position of the vertex currently handled.
in vec3 position;

// The texture coordinates.
in vec2 textureCoords;

// The normal vectors.
in vec3 normal;

out vec2 pass_textureCoords;

// The surface normal and the normal pointing directly at the light.
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;

void main(void)
{
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    // We pass the incoming texture coordinates straight to the fragment shader.
    pass_textureCoords = textureCoords;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
}