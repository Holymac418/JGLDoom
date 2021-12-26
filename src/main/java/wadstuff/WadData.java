package wadstuff;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import graphics.Colour;

public class WadData
{
	// Wad Info
	// --------
	public String WadName;
	public WadType _Type;
	public long _NumLumps;
	public long _DirectoryStart;

	// Lump Info
	// ---------
	public List<LumpInfo>[] SortedLumps;

	// Graphics
	// --------
	public PLAYPAL Playpal;
	public COLORMAP Colormap;

	public List<GraphicsLump> Graphics = new ArrayList<>();


	public WadData(String wadName)
	{
		WadName = wadName;
	}

	public void SortOneOffLump(LumpInfo lump, WadData Wad, RandomAccessFile WadFile)
	{
		switch(lump.Name)
		{
			case "PLAYPAL":
				Playpal = new PLAYPAL(WadFile, lump.Offset, lump.Size);
				break;
			case "COLORMAP": // NOT IMPLEMENTED
				break;
		}
	}
}
