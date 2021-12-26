package graphics;

public class Colour
{
	// storing as bytes from 0 to 255
	public byte r;
	public byte g;
	public byte b;
	public boolean a;

	public Colour(byte Red, byte Green, byte Blue)
	{
		r = Red;
		g = Green;
		b = Blue;
		a = true;
	}

	public Colour(byte Red, byte Green, byte Blue, boolean Alpha)
	{
		r = Red;
		g = Green;
		b = Blue;
		a = Alpha;
	}
}
