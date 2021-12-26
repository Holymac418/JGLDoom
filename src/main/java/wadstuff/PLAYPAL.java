package wadstuff;

import static program.Utility.print;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PLAYPAL
{
	public static int _NumBytesPerPalette = 256 * 3;
	public int _NumPalettes;
	public byte[][] Data;
	public static final int _transparencyColourIndex = 247; // usually 247

	public PLAYPAL(RandomAccessFile WadFile, int offset, int size)
	{
		try
		{
			// preparation
			// -----------
			WadFile.seek(offset);
			_NumPalettes = size / 768; // 256 colour palette with 3 bytes per colour
			Data = new byte[_NumPalettes][];

			// reading palettes
			// ----------------
			for(int p = 0; p < _NumPalettes; p++)
			{
				// reading colours
				// ---------------
				Data[p] = new byte[_NumBytesPerPalette];
				WadFile.read(Data[p], 0, _NumBytesPerPalette);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		print("Done Reading PLAYPAL.");
	}
}
