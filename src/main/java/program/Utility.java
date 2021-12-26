package program;

import org.joml.Vector3f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import graphics.Colour;
import wadstuff.GraphicsLump;

public class Utility {
	public static String WkDir = System.getProperty("user.dir");
	public static String ResourcesDir = "\\src\\main\\resources\\";

	final static Vector3f Vector3fzero = new Vector3f(0f);
	final static Vector3f Vector3fone = new Vector3f(1f);
	final static Vector3f Vector3fhalf = new Vector3f(0.5f);
	final static Vector3f Vector3fFourTenths = new Vector3f(0.4f);

	public static void print(String s)
	{
		System.out.println(s);
	}
	public static void print(int i)
	{
		System.out.println(i);
	}

	public static Vector3f Cross(Vector3f a, Vector3f b)
	{
		return new Vector3f(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z,
			a.x * b.y - a.y * b.x);
	}

	public static long GetIntLittleEndian(byte[] buffer, int Offset, int NumExpectedBytes)
	{
		long result = 0;
		int indexer = NumExpectedBytes;
		int i = NumExpectedBytes;
		while (i > 0) {
			long b = buffer[Offset + --indexer];
			System.out.println(b);
			result |= b << --i * 8;
		}
		return result;
	}

	/**
	 * @param ByteArray
	 * @return -1 if it failed.
	 **/
	public static int GetInt32LittleEndian(byte[] ByteArray, int Offset)
	{
		ByteBuffer buffer = ByteBuffer.wrap(ByteArray, Offset, 4); // Int32 has 4 bytes
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if (!buffer.hasRemaining() || buffer.remaining() < 4) {
			return -1;
		}
		int i = buffer.getInt();
		return i;
	}

	/**
	 * @param ByteArray
	 * @return -1 if it failed.
	 **/
	public static short GetInt16LittleEndian(byte[] ByteArray, int Offset)
	{
		ByteBuffer buffer = ByteBuffer.wrap(ByteArray, Offset, 2); // Int16 has 2 bytes
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		if (!buffer.hasRemaining() || buffer.remaining() < 2) {
			return -1;
		}
		short i = buffer.getShort();
		return i;
	}

	public static GraphicsLump gfxlmp;

	public static byte[] img =
		new byte[] { 24, 0, 30, 0, -5, -1, -2, -1, 104, 0, 0, 0, 115, 0, 0, 0, -119, 0, 0, 0, -93, 0, 0, 0, -64, 0, 0, 0, -34, 0, 0, 0, -2, 0, 0,
			0, 31, 1, 0, 0, 65, 1, 0, 0, 99, 1, 0, 0, -122, 1, 0, 0, -87, 1, 0,
			0, -52, 1, 0, 0, -17, 1, 0, 0, 18, 2, 0, 0, 53, 2, 0, 0, 87, 2, 0, 0, 121, 2, 0, 0, -102, 2, 0, 0, -70, 2, 0, 0, -40, 2, 0, 0, -11, 2,
			0, 0, 15, 3, 0, 0, 37, 3, 0, 0, 12, 6, 65, 65, 65, 69, 73, 70, 69,
			69, -1, 4, 17, 79, 79, 79, 79, 79, 79, 79, 79, 79, 0, 77, 77, 74, 74, 74, 74, 70, 70, 70, -1, 2, 21, 78, 78, 78, 78, 79, 79, 78, 78,
			78, 76, 76, 76, 76, 76, 70, 68, 63, 63, 64, 66, 68, 73, 73, -1, 1,
			24, 78, 78, 76, 76, 74, 72, 74, 76, 76, 75, 74, 72, 72, 69, 65, 69,
			59, 57, 57, 61, 63, 63, 63, 67, 73, 73, -1, 1, 25, 74, 74, 73, 73,
			72, 70, 72, 75, 72, 72, 69, 68, 68, 65, 75, 77, 72, 59, 57, 59, 59,
			61, 61, 63, 66, 73, 73, -1, 0, 27, 76, 76, 75, 72, 70, 69, 72, 74,
			74, 72, 66, 66, 62, 62, 59, 75, 77, 97, 66, 59, 62, 67, 66, 63, 61,
			63, 66, 73, 73, -1, 0, 28, 74, 74, 74, 72, 70, 68, 69, 69, 69, 75,
			72, 65, 62, 58, 55, 75, 77, -51, 58, 59, 64, 107, 73, 68, 65, 65, 63, 62, 73, 73, -1, 0, 29, 74, 74, 73, 70, 70, 68, 68, 68, 75, 69,
			75, 67, 62, 59, 55, 67, 73, 0, 66, 59, 69, 73, 107, 101, 61, 66, 63,
			60, 66, 73, 73, -1, 0, 29, 73, 73, 72, 69, 69, 68, 70, 69, 68, 75,
			67, 67, 67, 62, 56, 60, 65, 103, 68, 57, 74, 74, 69, 104, 101, 61,
			69, 63, 60, 69, 69, -1, 0, 30, 72, 72, 69, 68, 68, 68, 70, 69, 73,
			73, 75, 65, 68, 64, 59, 55, 73, 0, 74, 61, 66, 72, 64, 98, 101, 101,
			65, 69, 63, 61, 74, 74, -1, 0, 30, 72, 72, 68, 68, 68, 70, 70, 69,
			73, 72, 72, 68, 72, 67, 62, 65, 76, 76, 73, 66, 61, 67, 70, 97, 90,
			98, 61, 74, 66, 59, 69, 69, -1, 0, 30, 72, 72, 68, 68, 68, 68, 69,
			69, 69, 73, 69, 68, 70, 72, 67, 62, 67, 75, 66, 57, 53, 59, 70, 92,
			83, 97, 55, 77, 70, 54, 65, 65, -1, 0, 30, 72, 72, 68, 68, 68, 68,
			67, 76, 73, 73, 67, 68, 70, 72, 67, 56, 67, 75, 66, 57, 53, 59, 70,
			92, 83, 97, 55, 77, 70, 54, 65, 65, -1, 0, 30, 72, 72, 68, 68, 68,
			68, 72, 69, 76, 69, 65, 68, 72, 67, 62, 65, 76, 76, 73, 66, 61, 67,
			70, 97, 90, 98, 61, 74, 66, 59, 69, 69, -1, 0, 30, 72, 72, 69, 68,
			68, 68, 72, 74, 73, 69, 68, 65, 68, 64, 59, 55, 73, 0, 74, 61, 66,
			72, 64, 98, 101, 101, 65, 69, 63, 61, 74, 74, -1, 0, 29, 73, 73, 72,
			69, 69, 69, 72, 76, 76, 69, 67, 67, 67, 62, 56, 60, 65, 103, 68, 57,
			74, 74, 69, 104, 101, 61, 69, 63, 60, 69, 69, -1, 0, 29, 74, 74, 73,
			72, 72, 69, 72, 74, 77, 70, 69, 67, 62, 59, 55, 67, 73, 0, 66, 59,
			69, 73, 107, 101, 61, 66, 63, 60, 66, 73, 73, -1, 0, 28, 74, 74, 74,
			72, 72, 72, 74, 76, 74, 70, 69, 65, 62, 58, 55, 75, 77, -51, 58, 59,
			64, 107, 73, 68, 65, 65, 63, 62, 73, 73, -1, 0, 27, 76, 76, 75, 72,
			72, 72, 75, 78, 79, 73, 69, 66, 62, 62, 59, 75, 77, 97, 66, 59, 62,
			67, 66, 63, 61, 63, 66, 73, 73, -1, 1, 25, 74, 74, 73, 73, 73, 74,
			76, 78, 75, 72, 69, 68, 68, 65, 75, 77, 72, 59, 57, 59, 59, 61, 61,
			63, 66, 73, 73, -1, 1, 24, 78, 78, 76, 76, 76, 78, 79, 78, 79, 74,
			74, 72, 72, 69, 65, 69, 59, 57, 57, 61, 63, 63, 63, 67, 73, 73, -1,
			2, 21, 78, 78, 79, 79, 78, 78, 78, 76, 76, 77, 76, 76, 76, 76, 70,
			68, 63, 63, 64, 66, 68, 73, 73, -1, 4, 17, 79, 79, 79, 79, 79, 79,
			79, 79, 79, 0, 77, 77, 74, 74, 74, 74, 70, 70, 70, -1, 12, 6, 65, 65, 65, 69, 73, 70, 69, 69, -1 };

	public static byte[] ColourArrayToByteArray(Colour[] ColourArray)
	{
		// convert Colour[] to byte[]
		byte[] ByteArray = new byte[ColourArray.length * 3];
		for (int i = 0; i < ColourArray.length; i += 3) {
			ByteArray[i] = ColourArray[i].r;
			ByteArray[i + 1] = ColourArray[i].g;
			ByteArray[i + 2] = ColourArray[i].b;
		}
		return ByteArray;
	}

	public static void CreatePNG(byte[] data, String fileName)
	{
		try {
			BufferedImage bImage2 = ImageIO.read(new ByteArrayInputStream(data));
			boolean success = ImageIO.write(bImage2, "png", new File(WkDir + fileName));

			//OutputStream out = new BufferedOutputStream(bImage2);
			//	out.write(data);

			print(fileName + ": " + success);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void PrintByteArray(byte[] ArrayToPrint)
	{
		for(int x = 0; x < ArrayToPrint.length; x++)
		{
			System.out.print(ArrayToPrint[x]+ ", ");
		}
	}

	public static void PrintByteBuffer(ByteBuffer BufferToPrint)
	{
		try
		{
			if(BufferToPrint.remaining() <= 0)
			{
				System.out.println("Buffer is empty!");
				return;
			}
		}
		catch (Exception e)
		{
				System.out.println("Buffer is null!");
				return;
		}
		int x;
		for(x = 0; x < BufferToPrint.remaining(); x++)
		{
			System.out.print(BufferToPrint.get(x) + ", ");
		}
		System.out.println("\nPrinted " + x + " bytes.");
	}

	/**
	 *
	 * @param buffer1
	 * @param buffer2
	 * @return Number of mismatches. -1 if the buffers differ in length.
	**/
	public static int CompareByteBuffers(ByteBuffer buffer1, ByteBuffer buffer2)
	{
		buffer1.rewind();
		buffer2.rewind();
		int length = buffer1.remaining();
		if(length != buffer2.remaining())
		{
			return -1;
		}

		int counter = 0;
		for(int i = 0; i < length; i++)
		{
			if(buffer1.get(i) != buffer2.get(i))
			{
				counter++;
				System.out.println("owie at: " + i);
			}
		}
		return counter;
	}

	public static String getlastNcharsIntoString(String s, int N)
	{
		return s.substring(s.length() - N, s.length());
	}

	public static String getFirstNcharsIntoString(String s, int N)
	{
		return s.substring(0, N);
	}
}
