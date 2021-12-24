#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoords;

out vec3 FragPos;
out vec3 Normal;
out vec2 TexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    FragPos = vec3(model * vec4(aPos, 1.0));

    TexCoords = aTexCoords;
    
    gl_Position = projection * view * vec4(FragPos, 1.0);
	
	//Inversing matrices is a costly operation for shaders, 
	//so wherever possible try to avoid doing inverse operations 
	//since they have to be done on each vertex of your scene. 
	//For learning purposes this is fine, but for an efficient 
	//application you'll likely want to calculate the normal 
	//matrix on the CPU and send it to the shaders via a uniform 
	//before drawing (just like the model matrix). 
    Normal = mat3(transpose(inverse(model))) * aNormal;  
}