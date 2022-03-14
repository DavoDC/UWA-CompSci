#version 150

// Current vertex from program
in  vec3 vPosition;

// Color from program
in  vec3 vColor;

// Color going out to fragment shader
out vec4 color;

// Step 3 - Make variable that matches string in Step 1
// Rotation matrix from program
uniform mat4 rotMatrix;

void main()
{
    // Set GL vertex to Matrix times current Vertex
    gl_Position = rotMatrix * vec4(vPosition, 1.0);
	
	// Transfer colour
    color = vec4( vColor, 1.0 );
}
