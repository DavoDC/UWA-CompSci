
#include "Angel.h"

// Number of points
const int NumPoints = 36;

// Params shared with shader
GLint rotMatrixP;


//----------------------------------------------------------------------------

void
init(void) {

    // Points for cube
    vec3 points[NumPoints] = {
        vec3(-0.5, -0.5, 0.5), vec3(0.5, -0.5, 0.5), vec3(-0.5, 0.5, 0.5),
        vec3(0.5, 0.5, 0.5), vec3(-0.5, 0.5, 0.5), vec3(0.5, -0.5, 0.5),
        vec3(-0.5, -0.5, -0.5), vec3(0.5, -0.5, -0.5), vec3(-0.5, 0.5, -0.5),
        vec3(0.5, 0.5, -0.5), vec3(-0.5, 0.5, -0.5), vec3(0.5, -0.5, -0.5),
        vec3(0.5, -0.5, -0.5), vec3(0.5, 0.5, -0.5), vec3(0.5, -0.5, 0.5),
        vec3(0.5, 0.5, 0.5), vec3(0.5, -0.5, 0.5), vec3(0.5, 0.5, -0.5),
        vec3(-0.5, -0.5, -0.5), vec3(-0.5, 0.5, -0.5), vec3(-0.5, -0.5, 0.5),
        vec3(-0.5, 0.5, 0.5), vec3(-0.5, -0.5, 0.5), vec3(-0.5, 0.5, -0.5),
        vec3(-0.5, 0.5, -0.5), vec3(-0.5, 0.5, 0.5), vec3(0.5, 0.5, -0.5),
        vec3(0.5, 0.5, 0.5), vec3(0.5, 0.5, -0.5), vec3(-0.5, 0.5, 0.5),
        vec3(-0.5, -0.5, -0.5), vec3(-0.5, -0.5, 0.5), vec3(0.5, -0.5, -0.5),
        vec3(0.5, -0.5, 0.5), vec3(0.5, -0.5, -0.5), vec3(-0.5, -0.5, 0.5),
    };

    // Array of random colors
    vec3 colors[NumPoints];
    srand((unsigned) time(NULL));
    for (int i = 0; i < NumPoints; i++) {
        float r = (float) rand() / RAND_MAX;
        float g = (float) rand() / RAND_MAX;
        float b = (float) rand() / RAND_MAX;
        vec3 col = vec3(r, g, b);
        colors[i] = col;
    }

    // Create a vertex array object
    GLuint vao;
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);

    // Create and initialize a buffer object
    GLuint buffer;
    glGenBuffers(1, &buffer);
    glBindBuffer(GL_ARRAY_BUFFER, buffer);

    // First, we create an empty buffer of the size we need by passing
    //   a NULL pointer for the data values
    glBufferData(GL_ARRAY_BUFFER, sizeof (points) + sizeof (colors),
            NULL, GL_STATIC_DRAW);

    // Next, we load the real data in parts.  We need to specify the
    //   correct byte offset for placing the color data after the point
    //   data in the buffer.  Conveniently, the byte offset we need is
    //   the same as the size (in bytes) of the points array, which is
    //   returned from "sizeof(points)".
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof (points), points);
    glBufferSubData(GL_ARRAY_BUFFER, sizeof (points), sizeof (colors), colors);

    // Load shaders and use the resulting shader program
    GLuint program = InitShader("vshader6.glsl", "../fshader.glsl");
    glUseProgram(program);

    // Initialize the vertex position attribute from the vertex shader    
    GLuint vPosition = glGetAttribLocation(program, "vPosition");
    glEnableVertexAttribArray(vPosition);
    glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0,
            BUFFER_OFFSET(0));

    // Likewise, initialize the vertex color attribute.  Once again, we
    //    need to specify the starting offset (in bytes) for the color
    //    data.  Just like loading the array, we use "sizeof(points)"
    //    to determine the correct value.
    GLuint vColor = glGetAttribLocation(program, "vColor");
    glEnableVertexAttribArray(vColor);
    glVertexAttribPointer(vColor, 3, GL_FLOAT, GL_FALSE, 0,
            BUFFER_OFFSET(sizeof (points)));


    // Register params with shader variables
    rotMatrixP = glGetUniformLocation(program, "rotMatrix");

    //  Enable depth testing during drawing
    glEnable(GL_DEPTH_TEST);

    // Set white background
    glClearColor(1.0, 1.0, 1.0, 1.0);
}
//----------------------------------------------------------------------------

void
display(void) {

    // Clear colours and depths
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Angle
    float angle = 0.001 * glutGet(GLUT_ELAPSED_TIME);

    // Trig values
    float cosV = cos(angle);
    float sinV = sin(angle);

    // Add X rot matrix
    mat3 xRot = mat3(vec3(1.0, 0.0, 0.0),
            vec3(0.0, cosV, -sinV),
            vec3(0.0, sinV, cosV));

    // Add Y rot matrix
    mat3 yRot = mat3(vec3(cosV, 0.0, sinV),
            vec3(0.0, 1.0, 0.0),
            vec3(-sinV, 0.0, cosV));

    // Add Z rot matrix
    mat3 zRot = mat3(vec3(cosV, -sinV, 0.0),
            vec3(sinV, cosV, 0.0),
            vec3(0.0, 0.0, 1.0));

    // Rotate first around the y-axis and then around the x-axis, i.e.: rotX * rotY
    // Since the rotX happens second, the front face consistently rotates 
    // towards the bottom, with the rotY adding some rotation 
    // towards the left or right depending on the current x-axis rotation.
    // Give multiplied matrices to shader (Y FIRST THEN X)
    glUniformMatrix3fv(rotMatrixP, 1, GL_TRUE, xRot * yRot);

    // Draw cube
    glDrawArrays(GL_TRIANGLES, 0, NumPoints);

    // Double buffering
    glutSwapBuffers();
}

//----------------------------------------------------------------------------

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

void
keyboard(unsigned char key, int x, int y) {
    switch (key) {
        case 033:
            exit(EXIT_SUCCESS);
            break;
    }
}

//----------------------------------------------------------------------------

int
main(int argc, char** argv) {

    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
    glutInitWindowSize(600, 600);

    // If you are using freeglut, the next two lines will check if 
    // the code is truly 3.2. Otherwise, comment them out

    glutInitContextVersion(4, 0);
    glutInitContextProfile(GLUT_CORE_PROFILE);

    glutCreateWindow("Scene");

    glewInit();

    init();

    glutIdleFunc(idle);
    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);

    glutMainLoop();
    return 0;
}
