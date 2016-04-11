#version 400 core

// The position of the vertex currently handled.
in vec3 position;

// The texture coordinates.
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;

void main(void)
{
    gl_Position = transformationMatrix * vec4(position, 1.0);

    // We pass the incoming texture coordinates straight to the fragment shader.
    pass_textureCoords = textureCoords;
}