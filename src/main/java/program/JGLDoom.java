package program;

import graphics.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import wadstuff.WadData;
import wadstuff.WadReader;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class JGLDoom
{
	WadData wadData;
	public WadReader wadReader = new WadReader();

	// The window handle
	private long window;
	short WindowWidth = 800;
	short WindowHeight = 600;
	float WindowAspectRatio = ((float) WindowWidth) / ((float) WindowHeight);
	boolean firstMouse = true;
	double lastX, lastY;

	int VAO, VBO, EBO, lightCubeVAO;
	int sizeofFloat = 4;

	float rot = 0.0f;

	float[] CubeVertices =
	{
			// positions          // normals           // texture coords
			-0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,
			0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  0.0f,
			0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
			0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f,  1.0f,
			-0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  1.0f,
			-0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f,  0.0f,

			-0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,
			0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  0.0f,
			0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
			0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,
			-0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  1.0f,
			-0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f,  0.0f,

			-0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
			-0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
			-0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			-0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			-0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
			-0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

			0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,
			0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  1.0f,
			0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  1.0f,
			0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f,  0.0f,
			0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f,  0.0f,

			-0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,
			0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  1.0f,
			0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
			0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f,  0.0f,
			-0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  0.0f,
			-0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f,  1.0f,

			-0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f,
			0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  1.0f,
			0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
			0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f,  0.0f,
			-0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  0.0f,
			-0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f,  1.0f
	};

	// positions all containers
	Vector3f[] CubePositions =
	{
		new Vector3f( 0.0f,  0.0f,  0.0f),
		new Vector3f( 2.0f,  5.0f, -15.0f),
		new Vector3f(-1.5f, -2.2f, -2.5f),
		new Vector3f(-3.8f, -2.0f, -12.3f),
		new Vector3f( 2.4f, -0.4f, -3.5f),
		new Vector3f(-1.7f,  3.0f, -7.5f),
		new Vector3f( 1.3f, -2.0f, -2.5f),
		new Vector3f( 1.5f,  2.0f, -2.5f),
		new Vector3f( 1.5f,  0.2f, -1.5f),
		new Vector3f(-1.3f,  1.0f, -1.5f)
	};

	// positions of the point lights
    Vector3f[] pointLightPositions =
	{
		new Vector3f( 0.7f,  0.2f,  2.0f),
		new Vector3f( 2.3f, -3.3f, -4.0f),
		new Vector3f(-4.0f,  2.0f, -12.0f),
		new Vector3f( 0.0f,  0.0f, -3.0f)
	};

	Camera cam = new Camera(new Vector3f(0.0f, 0.0f, 3.0f));

	Shader DefaultShader;
	Shader LightingShader;
	//Vector3f LightPosition = new Vector3f(1.2f, 1.0f, 2.0f);
	Vector3f LightColour = Utility.Vector3fone;
	Texture Tex;

	public void run()
	{
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		//print(System.getProperty("user.dir"));
		init();
		loop();
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init()
	{
		initWindow();
		initMesh();
		initShaders();
		wadData = new WadData("DOOM.wad");
		wadReader.ReadWad(wadData);
		initTextures();
	}

	private void initWindow()
	{
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // set highest allowed OpenGL version
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3); // set lowest allowed OpenGL version
		//glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		//glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); //specify that we want to use core functionality. If you want to use and OpenGL version below 3.2 you have to use GLFW_OPENGL_ANY_PROFILE, which is the default value for this hint.
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE); //specifies if the OpenGL context should be forward compatible, if set to GLFW_TRUE it will deactivate all deprecated functionalities. If this is used with an OpenGL version below 3.0 this hint will get ignored.

		// Create the window
		window = glfwCreateWindow(WindowWidth, WindowHeight, "Slingnut Game Java", NULL, NULL);
		if (window == NULL)
		{
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwSetFramebufferSizeCallback(window, FrameBuffer_Size_CallBack);

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
		{
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
			{
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			}
		});
		glfwSetCursorPosCallback(window, mouse_callback);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush())
		{
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		// Enable vsync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

		// configure global opengl state
		glEnable(GL_DEPTH_TEST);
	}

	private void initShaders()
	{
		// SHADERS
		// -------
		DefaultShader = new Shader("3.3.shader"); //"6.3.coordinate_systems"
		//DefaultShader.use();
		LightingShader = new Shader("3.3.lightshader");
		//LightingShader.use();
	}

	private void initMesh()
	{
		// VERTEX BUFFER ARRAYS AND VERTEX BUFFER OBJECTS
		// ----------------------------------------------
		VAO = glGenVertexArrays();
		VBO = glGenBuffers();
		//EBO = glGenBuffers();

		glBindVertexArray(VAO);

		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, CubeVertices, GL_STATIC_DRAW);

		//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
		//glBufferData(GL_ELEMENT_ARRAY_BUFFER, CubeVertices, GL_STATIC_DRAW);

		// position attribute
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * sizeofFloat, 0);

		// normal attribute
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * sizeofFloat, 3 * sizeofFloat);

		// texture coord attribute
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * sizeofFloat, 6 * sizeofFloat);

		// second, configure the light's VAO (VBO stays the same; the vertices are the same for the light object which is also a 3D cube)
		lightCubeVAO = glGenVertexArrays();
		glBindVertexArray(lightCubeVAO);

		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		// note that we update the lamp's position attribute's stride to reflect the updated buffer data
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * sizeofFloat, 0);
		glEnableVertexAttribArray(0);
	}

	private void initTextures()
	{
		// TEXTURES
		// --------
		//Tex = new Texture(DefaultShader, "resources/textures", "PLAYPAL0" , ".png", GL_REPEAT, true, GL_RGBA);
		Tex = wadData.Graphics.get(24).texture;
		//DefaultShader.use();
		DefaultShader.setTexture(Tex);
		//Texture2 = new Texture(DefaultShader,"resources/textures", "awesomeface", ".png", GL_REPEAT, true, GL_RGBA);
		//DefaultShader.setInt("texture1", 0);
		//DefaultShader.setInt("texture2", 1);


	}

	public void loop()
	{
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		short targetFPS = 60;
		long targetTime = 1000L / targetFPS;
		float DeltaTime;
		float LastFrame = 0;

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(window))
		{
			// DeltaTime
			float CurrentTime = (float) (glfwGetTime() * 1000);
			DeltaTime = CurrentTime - LastFrame;
			LastFrame = CurrentTime;

			Input();
			Update(DeltaTime);
			RenderFrame();

			// Poll for window events. The key callback above will only be invoked during this call.
			glfwPollEvents();
		}
	}

	public void Input()
	{

	}

	public void Update(float DeltaTime)
	{
		cam.CameraControl(window, DeltaTime);
		rot += DeltaTime * 0.1f;
	}

	public void RenderFrame()
	{
		// Set the clear color
		glClearColor(0.0f, 0.43f, 0.45f, 1.0f);
		// clear depth buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


		// be sure to activate shader when setting uniforms/drawing objects
		DefaultShader.use();
		DefaultShader.setvec3("viewPos", cam.Position);
		DefaultShader.setFloat("material.shininess", 32.0f);

		/*// directional light
		DefaultShader.setvec3("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
		DefaultShader.setvec3("dirLight.ambient", Utility.Vector3fhalf);
		DefaultShader.setvec3("dirLight.diffuse", Utility.Vector3fFourTenths);
		DefaultShader.setvec3("dirLight.specular", Utility.Vector3fhalf);*/
		/*
           Here we set all the uniforms for the 5/6 types of lights we have. We have to set them manually and index 
           the proper PointLight struct in the array to set each uniform variable. This can be done more code-friendly
           by defining light types as classes and set their values in there, or by using a more efficient uniform approach
           by using 'Uniform buffer objects', but that is something we'll discuss in the 'Advanced GLSL' tutorial.
        */
		// directional light
		DefaultShader.setvec3("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
		DefaultShader.setvec3("dirLight.ambient", new Vector3f(0.05f, 0.05f, 0.05f));
		DefaultShader.setvec3("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
		DefaultShader.setvec3("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));
		// point light 1
		DefaultShader.setvec3("pointLights[0].position", pointLightPositions[0]);
		DefaultShader.setvec3("pointLights[0].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
		DefaultShader.setvec3("pointLights[0].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
		DefaultShader.setvec3("pointLights[0].specular", new Vector3f(1.0f, 1.0f, 1.0f));
		DefaultShader.setFloat("pointLights[0].constant", 1.0f);
		DefaultShader.setFloat("pointLights[0].linear", 0.09f);
		DefaultShader.setFloat("pointLights[0].quadratic", 0.032f);
		// point light 2
		DefaultShader.setvec3("pointLights[1].position", pointLightPositions[1]);
		DefaultShader.setvec3("pointLights[1].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
		DefaultShader.setvec3("pointLights[1].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
		DefaultShader.setvec3("pointLights[1].specular", new Vector3f(1.0f, 1.0f, 1.0f));
		DefaultShader.setFloat("pointLights[1].constant", 1.0f);
		DefaultShader.setFloat("pointLights[1].linear", 0.09f);
		DefaultShader.setFloat("pointLights[1].quadratic", 0.032f);
		// point light 3
		DefaultShader.setvec3("pointLights[2].position", pointLightPositions[2]);
		DefaultShader.setvec3("pointLights[2].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
		DefaultShader.setvec3("pointLights[2].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
		DefaultShader.setvec3("pointLights[2].specular",new Vector3f( 1.0f, 1.0f, 1.0f));
		DefaultShader.setFloat("pointLights[2].constant", 1.0f);
		DefaultShader.setFloat("pointLights[2].linear", 0.09f);
		DefaultShader.setFloat("pointLights[2].quadratic", 0.032f);
		// point light 4
		DefaultShader.setvec3("pointLights[3].position", pointLightPositions[3]);
		DefaultShader.setvec3("pointLights[3].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
		DefaultShader.setvec3("pointLights[3].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
		DefaultShader.setvec3("pointLights[3].specular",new Vector3f( 1.0f, 1.0f, 1.0f));
		DefaultShader.setFloat("pointLights[3].constant", 1.0f);
		DefaultShader.setFloat("pointLights[3].linear", 0.09f);
		DefaultShader.setFloat("pointLights[3].quadratic", 0.032f);
		// spotLight
		DefaultShader.setvec3("spotLight.position", cam.Position);
		DefaultShader.setvec3("spotLight.direction", cam.Forward);
		DefaultShader.setvec3("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
		DefaultShader.setvec3("spotLight.diffuse", new Vector3f(1.0f, 1.0f, 1.0f));
		DefaultShader.setvec3("spotLight.specular",new Vector3f( 1.0f, 1.0f, 1.0f));
		DefaultShader.setFloat("spotLight.constant", 1.0f);
		DefaultShader.setFloat("spotLight.linear", 0.09f);
		DefaultShader.setFloat("spotLight.quadratic", 0.032f);
		DefaultShader.setFloat("spotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
		DefaultShader.setFloat("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(15.0f)));


		// MATRIX TRANSFORMATIONS
		// ----------------------
		// create transformations
		//Matrix4f view = new Matrix4f(); // make sure to initialize matrix to identity matrix first
		Matrix4f projection = new Matrix4f();
		projection.perspective((float) Math.toRadians(cam.FOV), WindowAspectRatio, 0.1f, 100.0f);
		DefaultShader.setmat4("projection", projection); // note: currently we set the projection matrix each frame, but since the projection matrix rarely changes it's often best practice to set it outside the main loop only once.

		Matrix4f view = cam.GetViewMatrix();
		//view.translate(0.0f, 0.0f, -3.0f, view);
		DefaultShader.setmat4("view", view);

		// world transformation
		Matrix4f model = new Matrix4f();
		DefaultShader.setmat4("model", model);

		// bind diffuse map
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Tex.diffuseMap);
		// bind specular map
		//glActiveTexture(GL_TEXTURE1);
		//glBindTexture(GL_TEXTURE_2D, Tex.specularMap);

		// render containers

		for(int i = 0; i < 10; i++)
		{
			model = new Matrix4f();
			model.translate(CubePositions[i]);
			//float angle = rot + (20.0f * i);
			//model = model.rotate((float) Math.toRadians(angle), new Vector3f(1.0f, 0.3f, 0.5f));
			DefaultShader.setmat4("model", model);
			glBindVertexArray(VAO);
			glDrawArrays(GL_TRIANGLES, 0, 36);
			glBindVertexArray(0);
		}


		/*// also draw the lamp object(s)
		LightingShader.use();
		LightingShader.setmat4("projection", projection);
		LightingShader.setmat4("view", view);

		// we now draw as many light bulbs as we have point lights.
		glBindVertexArray(lightCubeVAO);
		for(int i = 0; i < 4; i++)
		{
			model = new Matrix4f();
			model.translate(pointLightPositions[i]);
			model.scale(new Vector3f(0.2f)); // Make it a smaller cube
			LightingShader.setmat4("model", model);
			glDrawArrays(GL_TRIANGLES, 0, 36);
		}*/

		glfwSwapBuffers(window); // swap the color buffers
	}

	// whenever the window size changed (by OS or user resize) this callback function executes
	// ---------------------------------------------------------------------------------------
	private static GLFWFramebufferSizeCallback FrameBuffer_Size_CallBack = new GLFWFramebufferSizeCallback()
	{
		@Override
		public void invoke(long window, int width, int height)
		{
			// make sure the viewport matches the new window dimensions; note that width and
			// height will be significantly larger than specified on retina displays.
			glViewport(0, 0, width, height);
			//update any other window vars you might have (aspect ratio, MVP matrices, etc)
		}
	};

	// whenever the window size changed (by OS or user resize) this callback function executes
	// ---------------------------------------------------------------------------------------
	private GLFWCursorPosCallback mouse_callback = new GLFWCursorPosCallback()
	{
		@Override
		public void invoke(long window, double xpos, double ypos)
		{
			if(firstMouse) // initially set to true
			{
				lastX = xpos;
				lastY = ypos;
				firstMouse = false;
			}

			double xoffset = xpos - lastX;
			double yoffset = lastY - ypos; // reversed since y-coordinates range from bottom to top

			lastX = xpos;
			lastY = ypos;

			xoffset *= cam.Sensitivity;
			yoffset *= cam.Sensitivity;

			cam.Yaw += xoffset;
			cam.Pitch += yoffset;

			if(cam.Pitch > 89.0f)
			{
				cam.Pitch = 89.0f;
			}
			if(cam.Pitch < -89.0f)
			{
				cam.Pitch = -89.0f;
			}

			// just in case yaw gets too big and causes a memory error or something
			if (cam.Yaw > 360.0f)
			{
				cam.Yaw = -360.0f;
			}
			if (cam.Yaw < -360.0f)
			{
				cam.Yaw = 360.0f;
			}

			cam.UpdateCameraVectors();
		}
	};

	public static void main(String[] args)
	{
		new JGLDoom().run();
	}
}
