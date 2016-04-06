#version 400 core

// The input is the position of the vertex currently handled.
in vec3 position;

// The output is the colour. (see below)
out vec3 colour;

void main(void)
{
    gl_Position = vec4(position, 1.0);
    colour = vec3(position.x + 0.5, 1.0, position.y + 0.5);
}