// very useful to learn from
// https://www.cyotek.com/blog/reading-doom-wad-files

package wadstuff;

import program.Utility;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static program.Utility.GetInt32LittleEndian;

enum WadType
{
	IWAD,
	PWAD,
	PK3 // not implemented yet
}

public class WadReader
{
	// info
	// ----
	private String WadName;
	private WadType _Type;
	private long _NumLumps;
	private long _DirectoryStart;


	// header biz
	// ----------
	private final byte _HeaderLength = 12;
	private final byte _NumLumpsOffset = 4;
	private final byte _DirectoryStartOffset = 8;


	// directory biz
	// -------------
	private final byte _directoryHeaderLength = 16;
	private final byte _lumpStartOffset = 0;
	private final byte _lumpSizeOffset = 4;
	private final byte _lumpNameOffset = 8;


	public void ReadWad(String wadName)
	{
		// setup
		// -----
		WadName = wadName;
		RandomAccessFile WadFile = null;
		try
		{
			WadFile = new RandomAccessFile(Utility.WkDir + Utility.ResourcesDir + WadName, "r");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		// reading
		// -------
		ReadHeader(WadFile);
		ReadDirectory(WadFile);
	}

	private void ReadHeader(RandomAccessFile WadFile)
	{
		try
		{
			byte[] buffer = new byte[_HeaderLength];
			WadFile.read(buffer, 0, _HeaderLength);

			if(buffer[0] == 'I') { _Type = WadType.IWAD; }
			else if(buffer[0] == 'P') { _Type = WadType.PWAD; }
			//else if(buffer[1] == 'K') { _Type = WadType.PK3; }
			else { System.exit(-1); }

			_NumLumps = GetInt32LittleEndian(buffer, _NumLumpsOffset); // appears to return int8 not int32
			_DirectoryStart = GetInt32LittleEndian(buffer, _DirectoryStartOffset); // appears to return int8 not int32
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void ReadDirectory(RandomAccessFile WadFile)
	{
		try
		{
			WadFile.seek(_DirectoryStart);
			byte[] buffer = new byte[_directoryHeaderLength];

			for(int i = 0; i < _NumLumps; i++)
			{
				int offset;
				int size;
				String name;

				WadFile.read(buffer, 0, _directoryHeaderLength);

				offset = GetInt32LittleEndian(buffer, _lumpStartOffset);
				size = GetInt32LittleEndian(buffer, _lumpSizeOffset);
				name = GetSafeLumpName(buffer);

				System.out.println(name);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String GetSafeLumpName(byte[] buffer)
	{
		int length = 0;

		for(int i = _directoryHeaderLength; i > _lumpNameOffset; i--)
		{
			if(buffer[i - 1] != '\0') // null terminating character
			{
				length = i - _lumpNameOffset;
				break;
			}
		}

		return length > 0 ? new String(buffer, StandardCharsets.UTF_8) : null;
	}
}
