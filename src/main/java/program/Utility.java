package program;

import org.joml.Vector3f;

public class Utility
{
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
}
