// very useful to learn from
// https://www.cyotek.com/blog/reading-doom-wad-files
// https://www.gamers.org/dhs/helpdocs/dmsp1666.html

package wadstuff;

import program.Utility;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static program.Utility.getFirstNcharsIntoString;
import static program.Utility.getlastNcharsIntoString;
import static program.Utility.print;
import static program.Utility.GetInt32LittleEndian;

public class WadReader
{
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

	// lumps biz
	// ---------
	private final byte _graphicsDataoffset = 8;
	private CurrentlyScanning _currentlyScanning;
	private String _currentMapName;

	public void getWadData(WadData Wad)
	{
		ReadWad(Wad);
	}

	public void getWadData(WadData Wad, String WadName)
	{
		Wad = new WadData(WadName);
		ReadWad(Wad);
	}

	public void ReadWad(WadData Wad)
	{
		// setup
		// -----
		RandomAccessFile WadFile = null;
		try
		{
			WadFile = new RandomAccessFile(Utility.WkDir + Utility.ResourcesDir + Wad.WadName, "r");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		// reading
		// -------
		ReadHeader(Wad, WadFile);
		ReadDirectory(Wad, WadFile);
		ReadLumps(Wad, WadFile);
		print("Done Reading " + Wad._Type + " " + Wad.WadName);
	}

	private void ReadHeader(WadData Wad, RandomAccessFile WadFile)
	{
		try
		{
			byte[] buffer = new byte[_HeaderLength];
			WadFile.read(buffer, 0, _HeaderLength);

			if(buffer[0] == 'I') { Wad._Type = WadType.IWAD; }
			else if(buffer[0] == 'P') { Wad._Type = WadType.PWAD; }
			//else if(buffer[1] == 'K') { _Type = WadType.PK3; }
			else { System.exit(-1); }

			Wad._NumLumps = GetInt32LittleEndian(buffer, _NumLumpsOffset); // appears to return int8 not int32
			Wad._DirectoryStart = GetInt32LittleEndian(buffer, _DirectoryStartOffset); // appears to return int8 not int32
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void ReadDirectory(WadData Wad, RandomAccessFile WadFile)
	{
		try
		{
			_currentlyScanning = CurrentlyScanning.OneOffLumps;
			WadFile.seek(Wad._DirectoryStart);
			byte[] buffer = new byte[_directoryHeaderLength];

			Wad.SortedLumps = new List[(int) Wad._NumLumps];
			for(int n = 0; n < Wad._NumLumps; n++)
			{
				Wad.SortedLumps[n] = new ArrayList<>();
			}

			for(int i = 0; i < Wad._NumLumps; i++)
			{
				int offset;
				int size;
				String name;

				WadFile.read(buffer, 0, _directoryHeaderLength);

				name = GetLumpName(buffer);
				offset = GetInt32LittleEndian(buffer, _lumpStartOffset);
				size = GetInt32LittleEndian(buffer, _lumpSizeOffset);

				if (name == null)
				{
					break;
				}

				LumpInfo lump = new LumpInfo(name, offset, size);
				Wad.SortedLumps[SortLump(name, size)].add(lump);
				if(name.equals("STFEVLO"))
				{
					print(i);
				}
				/*if(name.equals("PLAYPAL"))
				{
					System.out.println(name + " offset: " + offset + " size: " + size);
					Wad.Playpal = new PLAYPAL();
					Wad.Playpal.ReadPLAYPAL(WadFile, offset, size);
				}*/

				/*if(name.equals("COLORMAP"))
				{
					System.out.println(name + " offset: " + offset + " size: " + size);
					Wad.COLORMAP = ReadCOLORMAP(WadFile, buffer, offset, size);
				}*/

				/*if(name.equals("STFEVL0"))
				{
					//System.out.println(name + " offset: " + offset + " size: " + size);
					try
					{
						Utility.gfxlmp = new GraphicsLump();
						Utility.gfxlmp.GetGraphicsLump(WadFile, Wad, offset, size);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					break;
				}*/
				//
				//sBufferImage
			}

			int NumUnknownLumps = Wad.SortedLumps[Wad.SortedLumps.length - 1].size();
			if(NumUnknownLumps > 0)
			{
				String unknown = "";
				for(int u = 0; u < NumUnknownLumps; u++)
				{
					unknown += Wad.SortedLumps[Wad.SortedLumps.length - 1].get(u) + "\n";
				}
				throw new RuntimeException("Unknown lump(s) found. They are: " + unknown);
			}
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String GetLumpName(byte[] buffer)
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
		byte[] lumpname = Arrays.copyOfRange(buffer, _lumpNameOffset, _lumpNameOffset + length);

		return length > 0 ? new String(lumpname, StandardCharsets.UTF_8) : null;
	}

	/**
	 *
	 * @param name
	 * @param size
	 * @return returns the enum _currentlyScanning's current constant ordinal number.
	**/
	private int SortLump(String name, int size)
	{
		// MARKERS
		// -------
		if(size == 0)
		{
			_currentMapName = "";
			String s = getlastNcharsIntoString(name, 3);
			switch(s)
			{
				case "ART":
					switch(name)
					{
						case "S_START":
							_currentlyScanning = CurrentlyScanning.Sprites;
							print("Scanning Sprites.");
							break;
						case "P_START":
							print("Scanning Wall Patches.");
						case "P1_START":
						case "P2_START":
							_currentlyScanning = CurrentlyScanning.Walls;
							break;
						case "F_START":
							print("Scanning Flats.");
						case "F1_START":
						case "F2_START":
							_currentlyScanning = CurrentlyScanning.Flats;
							break;
						default:
							_currentlyScanning = CurrentlyScanning.Unknown;
							break;
					}
					return 0;
				case "END":
					_currentlyScanning = CurrentlyScanning.OneOffLumps;
					return 0;
				default:
					print("Scanning Map " + name);
					_currentMapName = name;
					_currentlyScanning = CurrentlyScanning.Maps;
					return 1;
			}
		}
		String s = getFirstNcharsIntoString(name, 2);
		switch(s)
		{
			case "D_":
				_currentlyScanning = CurrentlyScanning.Music;
				return 3;
			case "DP":
				_currentlyScanning = CurrentlyScanning.SoundEffects;
				return 2;
			default:
				if(_currentlyScanning == CurrentlyScanning.Music)
				{
					_currentlyScanning = CurrentlyScanning.OneOffLumps;
				}
				return _currentlyScanning.ordinal();
		}
	}

	private void ReadLumps(WadData Wad, RandomAccessFile WadFile)
	{
		// ONE-OFF LUMPS
		// -------------
		for(LumpInfo lump : Wad.SortedLumps[0])
		{
			Wad.SortOneOffLump(lump, Wad, WadFile);
		}

		// MAPS
		// ----
		for(LumpInfo lump : Wad.SortedLumps[1])
		{

		}

		// SOUND EFFECTS
		// -------------
		for(LumpInfo lump : Wad.SortedLumps[2])
		{

		}

		// MUSIC
		// -----
		for(LumpInfo lump : Wad.SortedLumps[3])
		{

		}

		// SPRITES
		// -------
		for(LumpInfo lump : Wad.SortedLumps[4])
		{
			Wad.Graphics.add(new GraphicsLump(WadFile, Wad, lump.Offset, lump.Size));
		}


		// WALLS
		// -----
		for(LumpInfo lump : Wad.SortedLumps[5])
		{

		}

		// FLATS
		// -----
		for(LumpInfo lump : Wad.SortedLumps[6])
		{

		}
	}
}
