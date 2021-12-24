package program;

import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utility
{
	public static String WkDir = System.getProperty("user.dir");
	public static String ResourcesDir = "\\src\\main\\resources\\";

	final static Vector3f Vector3fzero = new Vector3f(0f);
	final static Vector3f Vector3fone = new Vector3f(1f);
	final static Vector3f Vector3fhalf = new Vector3f(0.5f);
	final static Vector3f Vector3fFourTenths = new Vector3f(0.4f);

	public static Vector3f Cross(Vector3f a, Vector3f b)
	{
		return new Vector3f
		(
			a.y * b.z - a.z * b.y,
			a.z * b.x - a.x * b.z,
			a.x * b.y - a.y * b.x
		);
	}

	public static long GetIntLittleEndian(byte[] buffer, int Offset, int NumExpectedBytes)
	{
		long result = 0;
		int indexer = NumExpectedBytes;
		int i = NumExpectedBytes;
		while(i > 0)
		{
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
		if(!buffer.hasRemaining() || buffer.remaining() < 4)
		{
			return -1;
		}
		int i = buffer.getInt();
		return i;
	}
}
