#version 330 core
uniform vec3 LightColour;

out vec4 FragColor;

void main()
{
    FragColor = vec4(LightColour, 1.0);
}