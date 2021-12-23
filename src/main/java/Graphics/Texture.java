package Graphics;

import program.JGLDoom;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
	public static String FulltextureDirectory = JGLDoom.WkDir + "\\src\\main\\resources\\Textures\\";

	public int diffuseMap;
	public int specularMap;
	public int Width;
	public int Height;
	public int NumChannels;// = 4;
	public int DesiredChannels;

	// load and create a texture
	public Texture(Shader shader, String FilepathRelative, String FileName, String FileExtention, int TextureWrappingType, boolean FlipOnLoad, int ColourType)
	{
		diffuseMap = LoadTexture(shader, FilepathRelative, FileName + "_diffuse" + FileExtention, TextureWrappingType, FlipOnLoad, ColourType);
		specularMap = LoadTexture(shader, FilepathRelative, FileName + "_specular" + FileExtention, TextureWrappingType, FlipOnLoad, ColourType);
		// tell opengl for each sampler to which texture unit it belongs to (only has to be done once)
		// -------------------------------------------------------------------------------------------
		shader.use();
	}

	private int LoadTexture(Shader shader, String FilepathRelative, String FileName, int TextureWrappingType, boolean FlipOnLoad, int ColourType)
	{
		int ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, ID);

		// set the texture wrapping parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, TextureWrappingType);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, TextureWrappingType);

		// set texture filtering parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// load image, create texture and generate mipmaps
		stbi_set_flip_vertically_on_load(FlipOnLoad); // tell stb_image.h to flip loaded texture's on the y-axis.
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer c = BufferUtils.createIntBuffer(1);
		ByteBuffer data = stbi_load(FulltextureDirectory + FileName, w, h, c, 0);
		Width = w.get(0);
		Height = h.get(0);
		NumChannels = c.get(0);
		System.out.println(NumChannels);
		if (data != null && data.remaining() > 0)
		{
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Width, Height, 0, ColourType, GL_UNSIGNED_BYTE, data);
			glGenerateMipmap(GL_TEXTURE_2D);
		}
		else
		{
			System.out.println("Failed to load texture " + FileName);
		}
		if(data != null)
		{
			stbi_image_free(data);
		}

		return ID;
	}
}
