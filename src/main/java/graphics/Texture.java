package graphics;

import jdk.jshell.execution.Util;
import program.JGLDoom;
import org.lwjgl.BufferUtils;
import program.Utility;
import wadstuff.GraphicsLump;
import wadstuff.WadData;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.stb.STBImage.*;
import static program.Utility.print;

import javax.imageio.ImageIO;

public class Texture
{
	//public static String FulltextureDirectory = Utility.WkDir + "\\src\\main\\resources\\textures\\";

	public int diffuseMap;
	public int specularMap;
	public int Width;
	public int Height;
	//public int NumChannels;// = 4;
	//public int DesiredChannels;

	// load and create a texture
	/*public Texture(Shader shader, String FilepathRelative, String FileName, String FileExtention, int TextureWrappingType, boolean FlipOnLoad, int ColourType)
	{
		diffuseMap = LoadTextureFromWad(shader, FilepathRelative, FileName + /*"_diffuse" + *///FileExtention, TextureWrappingType, FlipOnLoad, ColourType);
		//specularMap = LoadTextureExternally(shader, FilepathRelative, FileName +/* "_specular" + */FileExtention, TextureWrappingType, FlipOnLoad, ColourType);
		// tell opengl for each sampler to which texture unit it belongs to (only has to be done once)
		// -------------------------------------------------------------------------------------------
		//shader.use();
	//}

	public Texture(byte[] PixelData, int Width, int Height,  int TextureWrappingType)
	{
		diffuseMap = LoadTextureFromWad(PixelData, Width, Height, TextureWrappingType);
		specularMap = LoadTextureFromWad(PixelData, Width, Height, TextureWrappingType);
	}

	/*private int LoadTextureExternally(Shader shader, String FilepathRelative, String FileName, int TextureWrappingType, boolean FlipOnLoad, int ColourType)
	{

	}*/

	private int LoadTextureFromWad(byte[] PixelData, int width, int height, int TextureWrappingType)
	{
		int ID = glGenTextures();

		ByteBuffer data = BufferUtils.createByteBuffer(PixelData.length);
		data.put(PixelData);
		data.flip();

		Width = width;
		Height = height;
		if (data != null && data.remaining() > 0)
		{
			glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
			// https://stackoverflow.com/questions/51352183/empty-texture-when-using-glteximage2d-with-bytebuffer-in-lwjgl
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Width, Height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
			//glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, width, height);
			//glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, Width, Height, GL_BGRA, GL_UNSIGNED_BYTE, data);
			glGenerateMipmap(GL_TEXTURE_2D);
		}
		else
		{
			System.out.println("Failed to load texture!");
		}

		glBindTexture(GL_TEXTURE_2D, ID);
		// set the texture wrapping parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, TextureWrappingType);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, TextureWrappingType);

		// set texture filtering parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		return ID;
	}
}
