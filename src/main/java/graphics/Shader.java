package graphics;

import program.Utility;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL30.*;

enum ShaderType
{
	Vertex,
	Fragment,
	Program
}

public class Shader
{
	public static String FullShaderDirectory = Utility.WkDir + "\\src\\main\\resources\\Shaders\\";

	public int ID;
	String VertexShaderCode;
	String FragmentShaderCode;
	Path VertexShaderSource;
	Path FragmentShaderSource;


	public Shader(String ShaderName)
	{
		VertexShaderSource = Path.of(FullShaderDirectory + ShaderName + ".vert");//"3.3.shader.vert");
		FragmentShaderSource = Path.of(FullShaderDirectory + ShaderName + ".frag");//"3.3.shader.frag");

		// get code from file
		VertexShaderCode = null;
		FragmentShaderCode = null;
		try
		{
			VertexShaderCode = Files.readString(VertexShaderSource, StandardCharsets.US_ASCII);
			FragmentShaderCode = Files.readString(FragmentShaderSource, StandardCharsets.US_ASCII);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Vertex Graphics.Shader
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, VertexShaderCode);
		glCompileShader(vertexShader);
		ShaderCheckCompileErrors(vertexShader, ShaderType.Vertex);

		// Fragment Graphics.Shader
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, FragmentShaderCode);
		glCompileShader(fragmentShader);
		ShaderCheckCompileErrors(fragmentShader, ShaderType.Fragment);

		// Graphics.Shader Program
		this.ID = glCreateProgram();
		glAttachShader(this.ID, vertexShader);
		glAttachShader(this.ID, fragmentShader);
		glLinkProgram(this.ID);
		ShaderCheckCompileErrors(this.ID, ShaderType.Program);
		// delete the shaders as they're linked into our program now and no longer necessary
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}

	int ShaderCheckCompileErrors(int Shader, ShaderType type)
	{
		int status;
		if(type != ShaderType.Program)
		{
			status = glGetShaderi(Shader, GL_COMPILE_STATUS);

		}
		else
		{
			status = glGetProgrami(Shader, GL_LINK_STATUS);
		}

		if (status != GL_TRUE)
		{
			throw new RuntimeException(glGetShaderInfoLog(Shader));
		}
		return 1;
	}

	public void use()
	{
		glUseProgram(ID);
	}

	// utility uniform functions
	// ------------------------------------------------------------------------
	public void setBool(String name, boolean value)
	{
		int v = value ? 1 : 0;
		glUniform1i(glGetUniformLocation(ID, name), v);
	}
	// ------------------------------------------------------------------------
	public void setInt(String name, int value)
	{
		glUniform1i(glGetUniformLocation(ID, name), value);
	}
	// ------------------------------------------------------------------------
	public void setFloat(String name, float value)
	{
		glUniform1f(glGetUniformLocation(ID, name), value);
	}
	public void setFloatArray(String name, float[] value)
	{
		glUniform3fv(glGetUniformLocation(ID, name), value);
	}
	public void setvec3(String name, Vector3f value)
	{
		float[] v = {value.x, value.y, value.z};
		glUniform3fv(glGetUniformLocation(ID, name), v);
	}
	public void setmat4(String name, Matrix4f value)
	{
		float[] v = new float[16];
		value.get(v);
		glUniformMatrix4fv(glGetUniformLocation(ID, name), false, v);
	}
	public void setTexture(Texture texture)
	{
		setInt("material.diffuse", 0);
		setInt("material.specular", 1);
	}
}