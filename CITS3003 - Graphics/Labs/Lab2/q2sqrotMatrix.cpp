/* File name: q2sqrotate.cpp
 * Rotates a square by defining two triangles. 
 * Each vertex has a different colour.
 * MODIFIED BY DC
 * USES 4x4 MATRIX FOR ROTATION
 */

// Include textbook header file
#include "Angel.h"

// Change C++ namespace to standard
using namespace std;

// Constants
const int NumTriangles = 2;
const int NumVertices  = 3 * NumTriangles;

// Points array
vec3 points[NumVertices] = {
    vec3(-0.5, -0.5, 0.0), vec3(-0.5, 0.5, 0.0), vec3(0.5, -0.5, 0.0),
    vec3(0.5, 0.5, 0.0),   vec3(0.5, -0.5, 0.0), vec3(-0.5, 0.5, 0.0)
};

// Colors array
vec3 colors[NumVertices] = {
    vec3(0.0, 1.0, 0.0), vec3(0.0, 0.0, 1.0), vec3(1.0, 0.0, 0.0),
    vec3(0.0, 1.0, 0.0), vec3(0.0, 0.0, 1.0), vec3(1.0, 0.0, 0.0),
};

// Step 0 - Create global reference/identifier int
// Matrix reference int
GLint matrix;



//----------------------------------------------------------------------------
// Initialization function
//----------------------------------------------------------------------------
void init(void)
{
    // Create a vertex array object
    GLuint vao;
    glGenVertexArrays( 1, &vao );
    glBindVertexArray( vao );

    // Create and initialize a buffer object
    GLuint buffer;
    glGenBuffers( 1, &buffer );
    glBindBuffer( GL_ARRAY_BUFFER, buffer );

    // First, we create an empty buffer of the size we need by passing
    //   a NULL pointer for the data values
    glBufferData( GL_ARRAY_BUFFER, sizeof(points) + sizeof(colors),
		  NULL, GL_STATIC_DRAW );

    // Next, we load the real data in parts.  We need to specify the
    //   correct byte offset for placing the color data after the point
    //   data in the buffer.  Conveniently, the byte offset we need is
    //   the same as the size (in bytes) of the points array, which is
    //   returned from "sizeof(points)".
    glBufferSubData( GL_ARRAY_BUFFER, 0, sizeof(points), points );
    glBufferSubData( GL_ARRAY_BUFFER, sizeof(points), sizeof(colors), colors );

    // Load shaders and use the resulting shader program
    GLuint program = InitShader("shaders/vrotate2dMatrix.glsl", "shaders/fshader24.glsl" );
    glUseProgram( program );

    // Initialize the vertex position attribute from the vertex shader    
    GLuint vPosition = glGetAttribLocation( program, "vPosition" );
    glEnableVertexAttribArray( vPosition );
    glVertexAttribPointer( vPosition, 3, GL_FLOAT, GL_FALSE, 0,
                           BUFFER_OFFSET(0) );

    // Likewise, initialize the vertex color attribute.  Once again, we
    //    need to specify the starting offset (in bytes) for the color
    //    data.  Just like loading the array, we use "sizeof(points)"
    //    to determine the correct value.
    GLuint vColor = glGetAttribLocation( program, "vColor" );
    glEnableVertexAttribArray( vColor );
    glVertexAttribPointer( vColor, 3, GL_FLOAT, GL_FALSE, 0,
                           BUFFER_OFFSET(sizeof(points)) );

    // Enable depth testing
    //glEnable( GL_DEPTH_TEST );

    // Step 1 - Register shared variable in init()
	// Register matrix variable to rotMatrix shader variable
	// Documentation = https://docs.gl/gl4/glGetUniformLocation
    matrix = glGetUniformLocation(program, "rotMatrix");

	// Set white background
    glClearColor( 1.0, 1.0, 1.0, 1.0 ); 
}



//----------------------------------------------------------------------------
// Display function
//----------------------------------------------------------------------------
void display(void)
{
	// Make angle from time
    float angle = 0.001 * glutGet(GLUT_ELAPSED_TIME);
	
	// Calculate trig values
    float cosV = cos(angle);
    float sinV = sin(angle);
	
	// Matrices are based off image:
	// https://drive.google.com/file/d/1CPvvedGYrH9FkDgDNf6S8fxCiMq-dTOI/view
	
	// 4x4 matrix for rotation on X-axis
    mat4 rotXm = mat4(1.0, 0.0,   0.0,  0.0,
                      0.0, cosV, -sinV, 0.0,
                      0.0, sinV, cosV,  0.0,
                      0.0, 0.0,   0.0,  1.0);
							
	// 4x4 matrix for rotation on Y-axis
    mat4 rotYm = mat4(cosV,  0.0, sinV, 0.0,
                      0.0,   1.0, 0.0,  0.0,
                      -sinV, 0.0, cosV, 0.0,
                      0.0,   0.0, 0.0,  1.0);
					 
	// 4x4 matrix for rotation on Z-axis
    mat4 rotZm = mat4(cosV, -sinV, 0.0, 0.0,
                      sinV, cosV,  0.0, 0.0,
                      0.0,   0.0,  1.0, 0.0,
                      0.0,   0.0,  0.0, 1.0);
							
	
	// Set which matrix to use
	mat4 currrentMatrix = rotXm;
	//mat4 currrentMatrix = rotYm;
	//mat4 currrentMatrix = rotZm;
	
	
	// Step 2 - Update the value of the shared variable
	// See all functions here: https://docs.gl/gl4/glUniform
	// Each value:
	// - matrix = GLint location = the reference int
    // - 1 = GLsizei count = the number of matrices
    // - GL_FALSE = GLboolean transpose = Transpose matrix values?
    // - currentMatrix = const GLfloat *value = the new matrix data
	glUniformMatrix4fv(matrix, 1, GL_FALSE, currentMatrix);
		
	// Clear color buffer
    glClear(GL_COLOR_BUFFER_BIT);
    
	// Draw vertices as triangles
    glDrawArrays(GL_TRIANGLES, 0, NumVertices);
	
	// Flush
    glFlush();
}


//----------------------------------------------------------------------------
// Keyboard callback function
//----------------------------------------------------------------------------
void keyboard(unsigned char key, int x, int y)
{
    switch (key) {
		// ASCII code for Esc
		case 033:
			// Press Escape to exit
			exit(EXIT_SUCCESS);
			break;
    }
}


//----------------------------------------------------------------------------
// Idle Function
//----------------------------------------------------------------------------
void idle(void)
{
/*
Calling glutPostRedisplay tells GLUT that the window needs to be redisplayed. Here we call
it in the idle function because there is constant motion, so we want to redisplay whenever
GLUT/OpenGL has nothing else to do and is idle. The actual redisplay will happen at some point
after the idle function returns, when GLUT is ready to redraw the window.
More generally, if you only require redrawing when a mouse event causes an object to move, or
similar, you should only call glutPostRedisplay() when such a change occurs. Calling it
multiple times is fine if multiple changes occur before a redraw happens - it just sets a variable
that GLUT uses to remember that a redraw is required.]
*/
    glutPostRedisplay();
}


//----------------------------------------------------------------------------
// Main function
//----------------------------------------------------------------------------
int main(int argc, char **argv)
{
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutInitWindowSize(256, 256);
    glutInitContextVersion(3, 2);
    glutInitContextProfile(GLUT_CORE_PROFILE);
    glutCreateWindow("CITS3003 Lab 2 Rotate Square - 4x4 Matrix by DC");

    glewInit(); // optional for Linux

    init();

    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);
    glutIdleFunc(idle);

    glutMainLoop();
    return 0;
}
