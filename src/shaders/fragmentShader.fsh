#version 400 core

// The colour sent from the vertex shader.
// The color is an interpolated color value of the vertex shader
// output color.
in vec3 colour;

out vec4 out_Color;

void main(void)
{
    out_Color = vec4(colour, 1.0);
}