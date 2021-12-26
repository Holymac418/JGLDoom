package wadstuff;

import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static program.Utility.ColourArrayToByteArray;
import static program.Utility.GetInt16LittleEndian;
import static program.Utility.GetInt32LittleEndian;
import static program.Utility.WkDir;
import static program.Utility.print;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import graphics.Colour;
import graphics.Texture;
import program.UnsignedByte;
import program.Utility;

public class GraphicsLump
{
	// Header
	// ------
	private int _graphicsHeaderlength = 8;
	public short Width;
	public short Height;
	//public short LeftOffset;
	//public short TopOffset;
	public int[] ColumnOffsets;

	// Data
	// ----
	private static final byte _transparentPixel = 0;
	private int _graphicsDatalength;
	public byte[] PixelData;
	public Texture texture;

	/**
	 * https://doomwiki.org/wiki/Picture_format
	 **/
	public GraphicsLump(RandomAccessFile WadFile, WadData Wad, int offset, int size) // reads by column but needs to be read by row
	{
		try
		{
			WadFile.seek(offset);

			// Read Header
			// -----------
			byte[] HeaderBuffer = new byte[_graphicsHeaderlength];
			WadFile.read(HeaderBuffer, 0, _graphicsHeaderlength);
			Width = GetInt16LittleEndian(HeaderBuffer, 0);
			Height = GetInt16LittleEndian(HeaderBuffer, 2);
			//LeftOffset = GetInt16LittleEndian(HeaderBuffer, 4);
			//TopOffset = GetInt16LittleEndian(HeaderBuffer, 6);

			byte[] ColumnofsBuffer = new byte[Width * 4];
			WadFile.read(ColumnofsBuffer, 0, ColumnofsBuffer.length);
			ColumnOffsets = new int[Width];
			for(int c = 0; c < Width; c++)
			{
				ColumnOffsets[c] = GetInt32LittleEndian(ColumnofsBuffer, c * 4);
			}

			// read Data
			// ---------
			_graphicsDatalength = size - _graphicsHeaderlength - (ColumnofsBuffer.length * 4);
			PixelData = new byte[Width * Height * 4]; // inits to 0

			for(int column = 0; column < Width; column++)
			{
				int PostLength;
				WadFile.seek(offset + ColumnOffsets[column]);

				int poststart = 0; // how many transparent bytes to skip until getting to an opaque pixel
				while(poststart != 255) // exits this loop with a break
				{
					poststart = WadFile.readUnsignedByte(); // how many pixels in this post

					if(poststart == 255) // if true, go to the next column
					{
						break; // exits the loop with this break
					}

					PostLength = WadFile.readUnsignedByte();

					WadFile.skipBytes(1); // dummy value

					for(int j = 0; j < PostLength; j++) // every post in this column
					{
						int row = j + poststart;
						int index = WadFile.readUnsignedByte();

						for(int b = 0; b < 3; b++)
						{
							PixelData[(row * Width) + column + b] = Wad.Playpal.Data[0][index + b];
						}
						PixelData[(row * Width) + column + 3] = 0; // alpha
					}

					WadFile.skipBytes(1); // dummy value
				}
			}

			/*byte[] DataBuffer = new byte[_graphicsDatalength];
			WadFile.read(DataBuffer, 0, _graphicsDatalength);

			for(int i = 0; i < PixelData.length; i++)
			{
				PixelData[i] = _transparentPixel;
			}

			for(int col = 0; col < Width; col++)
			{
				int ptr = ColumnofsBuffer[col]; //GetInt32LittleEndian(ColumnofsBuffer, col * 4);

				do
				{
					int row = DataBuffer[ptr];
					int postHeight;

					if(row != 255 && (postHeight = DataBuffer[++ptr]) != 255)
					{
						ptr++; // dummy value

						for(int i = 0; i < postHeight; i++)
						{
							if(row + i < Height && ptr < DataBuffer.length - 1)
							{
								int index = DataBuffer[++ptr];
								for(int b = 0; b < 4; b++)
								{
									PixelData[((row + i) * Width) + col + b] = Wad.Playpal.Data[0][index + b];
								}

							}
						}

						ptr++; // dummy value
					}
					else
					{
						break;
					}
				}
				while(ptr < DataBuffer.length - 1 && DataBuffer[++ptr] != 255);
			}*/
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		texture = new Texture(PixelData, Width, Height, GL_REPEAT);

		//byte[] imagedata = ColourArrayToByteArray(PixelData);
		//Utility.CreatePNG(imagedata, "STFEVL0.png");


		/*DataBuffer dbuffer = new DataBufferByte(PixelData, PixelData.length);
		WritableRaster raster = Raster.createInterleavedRaster(dbuffer, Width, Height, Width, 3, new int[] {0, 1, 2}, (Point)null);
		ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		BufferedImage image = new BufferedImage(cm, raster, true, null);

		ImageIO.write(image, "png", new File(WkDir + "STFEVL0.png"));
		print("Done");*/
	}
}
