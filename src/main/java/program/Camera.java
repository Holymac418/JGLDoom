package program;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Camera
{
	// Default Values
	final static float DefaultYaw = 0.0f;
	final static float DefaultPitch = 0.0f;
	final static float DefaultRoll = 0.0f;
	final static float DefaultSpeed = 0.025f;
	final static float DefaultSensitivity = 0.1f;
	final static float DefaultFOV = 60.0f;
	final static float DefaultZoom = 45.0f;
	final static Vector3f CameraUp = new Vector3f(0f, 1f, 0f);
	final static Vector3f CameraForward = new Vector3f(0f, 0f, -1f);
	final static Vector3f CameraRight = new Vector3f(1f, 0f, 0f);
	final static Vector3f WorldCenter = Utility.Vector3fzero;

	// Settings
	public Vector3f Position;
	public float Speed = DefaultSpeed;
	public float Sensitivity = DefaultSensitivity;
	public float FOV = DefaultFOV;
	public float Zoom = DefaultZoom;
	public Vector3f FocusedWorldPosition = new Vector3f(0f, 0f, -3f);
	public boolean FocusObject = true;
	public boolean UsingCustomLookAt = false;

	// Direction Vectors
	public Vector3f Forward;
	public Vector3f Up;
	public Vector3f Right;

	// Euler Angles
	public float Yaw;
	public float Pitch;
	public float Roll;

	public Camera(Vector3f pos, float yaw, float pitch, float roll)
	{
		if(yaw == 0 && pitch == 0 && roll == 0)
		{
			new Camera(pos);
		}
		else
		{
			Position = pos;

			Yaw = yaw;
			Pitch = pitch;
			Roll = roll;
			UpdateCameraVectors();
		}
	}

	public Camera(Vector3f pos)
	{
		Position = pos;

		Yaw = DefaultYaw;
		Pitch = DefaultPitch;
		Roll = DefaultRoll;

		Forward = CameraForward;
		Up = CameraUp;
		Right = CameraRight;
		UpdateCameraVectors();
	}

	public void UpdateCameraVectors()
	{
		Forward.x = (float) (Math.sin(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
		Forward.y = (float) Math.sin(Math.toRadians(Pitch));
		Forward.z = (float) (-Math.cos(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
		Forward.normalize();

		Right = Utility.Cross(Forward, CameraUp);
		Right.normalize();

		Up = Utility.Cross(Right, Forward);
		Up.normalize();
	}

	public Matrix4f GetViewMatrix()
	{
		//Vector3f a = new Vector3f(Position);
		Vector3f d = new Vector3f();
		Position.add(Forward, d);
		Matrix4f f = new Matrix4f();
		//if(UsingCustomLookAt)
		//{
			//f = calculate_lookAt_matrix(Position, d);
		//}
		//else
		//{
			f.lookAt(Position, d, Up, f);
		//}

		return f;
	}

	public void CameraControl(long window, float deltaTime)
	{
		// camera input
		// ------------
		Vector3f v = new Vector3f();
		float velocity = Speed * deltaTime;
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
		{
			Forward.mul(velocity, v);
			Position.add(v);
		}
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
		{
			Forward.mul(-velocity, v);
			Position.add(v);
		}
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
		{
			Right.mul(-velocity, v);
			Position.add(v);
		}
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
		{
			Right.mul(velocity, v);
			Position.add(v);
		}
		if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS)
		{
			Up.mul(velocity, v);
			Position.add(v);
		}
		if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS)
		{
			Up.mul(-velocity, v);
			Position.add(v);
		}
	}

	/*// Custom implementation of the LookAt function
	public Matrix4f calculate_lookAt_matrix(Vector3f position, Vector3f target)
	{
		Vector3f zaxis = new Vector3f();
		Vector3f xaxis = new Vector3f();
		Vector3f yaxis = new Vector3f();

		// 1. Position = known
		// 2. Calculate cameraDirection
		position.sub(target, zaxis);
		zaxis.normalize();
		// 3. Get positive right axis vector
		xaxis = Utility.Cross(zaxis, CameraUp);
		//CameraUp.cross(zaxis, xaxis);
		xaxis.normalize();
		// 4. Calculate camera up vector
		yaxis = Utility.Cross(xaxis, zaxis);
		//zaxis.cross(xaxis, yaxis);

		// Create translation and rotation matrix
		// In glm it is column-major layout
		Matrix4f translation = new Matrix4f(); // Identity matrix by default
		translation.m00(-position.x);
		translation.m30(-position.x); // Third column, first row
		translation.m31(-position.y);
		translation.m32(-position.z);
		Matrix4f rotation = new Matrix4f();
		//rotation.setColumn(xaxis);
		rotation.m00(xaxis.x); // First column, first row
		rotation.m10(xaxis.y);
		rotation.m20(xaxis.z);
		rotation.m01(yaxis.x); // First column, second row
		rotation.m11(yaxis.y);
		rotation.m21(yaxis.z);
		rotation.m02(zaxis.x); // First column, third row
		rotation.m12(zaxis.y);
		rotation.m22(zaxis.z);
	
		// Return lookAt matrix as combination of translation and rotation matrix
		Matrix4f result = new Matrix4f();
		//return rotation * translation;
		rotation.mul(translation, result); // Remember to read from right to left (first translation then rotation)
	
		return result;
	}*/
}
