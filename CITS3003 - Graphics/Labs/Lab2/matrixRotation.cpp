/*
 * Original file name: q2sqrotate.cpp
 * Rotates a square by defining two triangles.
 * Each vertex has a different colour.
 * MODIFIED BY DC
 * USES 4x4 MATRIX FOR ROTATION
 */

// Include textbook header file
#include "Angel.h"

// Include C++ libraries
#include <iostream>
#include <cctype>
#include <ctype.h>

// Change C++ namespace to standard
using namespace std;

// Constants
const int NumTriangles = 2;
const int NumVertices = 3 * NumTriangles;

// Points array
vec3 points[NumVertices] = {
    vec3(-0.5, -0.5, 0.0), vec3(-0.5, 0.5, 0.0), vec3(0.5, -0.5, 0.0),
    vec3(0.5, 0.5, 0.0), vec3(0.5, -0.5, 0.0), vec3(-0.5, 0.5, 0.0)
};

// Colors array
vec3 colors[NumVertices] = {
    vec3(0.0, 1.0, 0.0), vec3(0.0, 0.0, 1.0), vec3(1.0, 0.0, 0.0),
    vec3(0.0, 1.0, 0.0), vec3(0.0, 0.0, 1.0), vec3(1.0, 0.0, 0.0),
};

// Step 0 - Create global reference/identifier int
// Matrix reference int
GLint matrix;

// Matrix selection character
char matrixChar = 'x';

/**
 * Initialization Function
 */
void init(void) {

    // Create a vertex array object (VAO)
    GLuint vao; // Holder
    // https://docs.gl/gl4/glGenVertexArrays
    glGenVertexArrays(1, &vao); // Type
    // https://docs.gl/gl4/glBindVertexArray
    glBindVertexArray(vao); // Bind

    // Create and initialize a vertex buffer object (VBO)
    GLuint buffer; // Holder
    // https://docs.gl/gl4/glGenBuffers
    glGenBuffers(1, &buffer); // Type
    // https://docs.gl/gl4/glBindBuffer
    glBindBuffer(GL_ARRAY_BUFFER, buffer); // Bind

    // Initialise vertex buffer
    // - Will hold points and colours
    // - At this stage, we just pass a NULL pointer for the data values
    // https://docs.gl/gl4/glBufferData
    glBufferData(GL_ARRAY_BUFFER, sizeof (points) + sizeof (colors), NULL, GL_STATIC_DRAW);

    // Now we load the real data in parts.
    // We need to specify the correct byte offset for placing the color data after the point
    // data in the buffer.  Conveniently, the byte offset we need is the same as the size
    // (in bytes) of the points array, which is returned from "sizeof(points)".
    // https://docs.gl/gl4/glBufferSubData
    // We load the points in first
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof (points), points);
    // We load the colors in after the points
    glBufferSubData(GL_ARRAY_BUFFER, sizeof (points), sizeof (colors), colors);

    // Load shaders and use the resulting shader program
    // The shaders are located in my shaders folder
    GLuint program = InitShader("shaders/vrotate2dMatrix.glsl", "shaders/fshader24.glsl");
    // https://docs.gl/gl4/glUseProgram
    glUseProgram(program);

    // Initialize the vertex position attribute from the vertex shader
    // https://docs.gl/gl4/glGetAttribLocation
    GLuint vPosition = glGetAttribLocation(program, "vPosition");
    // https://docs.gl/gl4/glEnableVertexAttribArray
    glEnableVertexAttribArray(vPosition);
    // https://docs.gl/gl4/glVertexAttribPointer
    // - vPosition = GLuint index = vPosition identifier/index
    // - 3 = GLint size = number of components per vertex = 3 because vec3 in use
    // - GL_FLOAT = GLenum type = data type of array elements = Float because points are floats
    // - GL_FALSE = GLboolean normalized = whether fixed-point data values should be normalized
    // - 0 = GLsizei stride = byte offset between consecutive generic vertex attributes
    // - BUFFER_OFFSET(0) = const GLvoid* pointer = offset of the first vertex in the buffer
    glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0));

    // Likewise, initialize the vertex color attribute.
    // Once again, we need to specify the starting offset (in bytes) for the color data.
    // Just like loading the array, we use "sizeof(points)" to determine the correct value.
    //
    // Same as before but buffer pointer moves up
    GLuint vColor = glGetAttribLocation(program, "vColor");
    glEnableVertexAttribArray(vColor);
    glVertexAttribPointer(vColor, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(sizeof (points)));

    // Enable depth testing
    // glEnable( GL_DEPTH_TEST );

    // Step 1 - Register shared variable in init()
    // Register matrix variable to rotMatrix shader variable
    // https://docs.gl/gl4/glGetUniformLocation
    matrix = glGetUniformLocation(program, "rotMatrix");

    // Set white background
    // https://docs.gl/gl4/glClearColor
    glClearColor(1.0, 1.0, 1.0, 1.0);
}

/**
 * Display Callback Function
 */
void display(void) {

    // Make angle from time
    float angle = 0.001 * glutGet(GLUT_ELAPSED_TIME);

    // Calculate trig values
    float cosV = cos(angle);
    float sinV = sin(angle);

    // Matrices are based off image:
    // https://github.com/DavoDC/UWA-CompSci/blob/main/CITS3003%20-%20Graphics/Labs/Lab2/Images%20-%20Matrix%20Rotation/RotationMatrices.png
    // https://drive.google.com/file/d/1CPvvedGYrH9FkDgDNf6S8fxCiMq-dTOI/view


    // ### 4x4 Rotation Matrices
    // X-Axis
    mat4 rotXm = mat4(1.0, 0.0, 0.0, 0.0,
            0.0, cosV, -sinV, 0.0,
            0.0, sinV, cosV, 0.0,
            0.0, 0.0, 0.0, 1.0);

    // Y-axis
    mat4 rotYm = mat4(cosV, 0.0, sinV, 0.0,
            0.0, 1.0, 0.0, 0.0,
            -sinV, 0.0, cosV, 0.0,
            0.0, 0.0, 0.0, 1.0);

    // Z-axis
    mat4 rotZm = mat4(cosV, -sinV, 0.0, 0.0,
            sinV, cosV, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0);

    // Do X and Y rotation at the same time
    //mat4 currentMatrix = rotXm * rotYm;
 
    // Set which matrix to use
    mat4 currentMatrix = rotXm;
    if (matrixChar == 'x') {
        currentMatrix = rotXm;
    } else if (matrixChar == 'y') {
        currentMatrix = rotYm;
    } else if (matrixChar == 'z') {
        currentMatrix = rotZm;
    }


    // Step 2 - Update the value of the shared variable
    // See all functions here: https://docs.gl/gl4/glUniform
    // Each value:
    // - matrix = GLint location = the reference int
    // - 1 = GLsizei count = the number of matrices
    // - GL_FALSE = GLboolean transpose = Transpose matrix values?
    // - currentMatrix = const GLfloat *value = the new matrix data
    glUniformMatrix4fv(matrix, 1, GL_FALSE, currentMatrix);

    // Clear color buffer
    // https://docs.gl/gl4/glClear
    glClear(GL_COLOR_BUFFER_BIT);

    // Draw vertices as triangles
    // https://docs.gl/gl4/glDrawArrays
    glDrawArrays(GL_TRIANGLES, 0, NumVertices);

    // Flush
    // https://docs.gl/gl4/glFlush
    glFlush();
}

/*
 * Helper function to notify about rotation change
 */
void notifyAboutRotChange() {
    printf("\nSwitched to %c rotation!", toupper(matrixChar));
    printf("\n");
}

/**
 * Keyboard Callback Function. Called every time a key is pressed.
 * @param key Code of key pressed
 * @param x
 * @param y
 */
void keyboard(unsigned char key, int x, int y) {

    // Act based on key pressed
    switch (key) {
        case 033:
            // Press Escape to exit
            exit(EXIT_SUCCESS);
            break;

        case 120:
            // Press X for X rotation and Notify
            matrixChar = 'x';
            notifyAboutRotChange();
            break;

        case 121:
            // Press Y for Y rotation and Notify
            matrixChar = 'y';
            notifyAboutRotChange();
            break;

        case 122:
            // Press Z for Z rotation and Notify
            matrixChar = 'z';
            notifyAboutRotChange();
            break;
    }
}

/**
 * Idle Callback Function. Called every time window is idle.
 */
void idle(void) {
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

int main(int argc, char **argv) {

    // Starting message
    printf("\n$$$ CITS3003 Lab2 - Rotation using 4x4 Matrices $$$");
    printf("\nCurrent Rotation: %c", toupper(matrixChar));
    printf("\nPress X, Y or Z to switch rotational axis.");
    printf("\n");

    // Set up program
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA);
    glutInitWindowSize(256, 256);
    glutInitContextVersion(3, 2);
    glutInitContextProfile(GLUT_CORE_PROFILE);
    glutCreateWindow("CITS3003 Lab 2 Rotate Square - 4x4 Matrix by DC");
    glewInit();

    // Initialize program
    init();

    // Register callback functions
    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);
    glutIdleFunc(idle);

    // Enter main event loop
    glutMainLoop();
    return 0;
}
