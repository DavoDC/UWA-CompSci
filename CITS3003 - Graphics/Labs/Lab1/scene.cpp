
#include "Angel.h"

const int NumPoints = 4;

//----------------------------------------------------------------------------

void
init(void) {
    // Points array
    vec2 points[NumPoints];

    // Equilateral Triangle
    //points[0] = vec2(-1.0, -1.0);
    //points[1] = vec2(0.0, 1.0);
    //points[2] = vec2(1.0, -1.0);

    // Strange triangle/line strip
    points[0] = vec2(-0.5f, 1.0f);
    points[1] = vec2(0.0f, -0.5f);
    points[2] = vec2(1.0f, 1.0f);
    points[3] = vec2(1.5f, 0.0f);

    // Create a vertex array object
    GLuint vao;
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);

    // Create and initialize a buffer object
    GLuint buffer;
    glGenBuffers(1, &buffer);
    glBindBuffer(GL_ARRAY_BUFFER, buffer);
    glBufferData(GL_ARRAY_BUFFER, sizeof (points), points, GL_STATIC_DRAW);

    // Load shaders and use the resulting shader program
    GLuint program = InitShader("shaders/vshader21.glsl", "shaders/fshader21.glsl");
    glUseProgram(program);

    // Initialize the vertex position attribute from the vertex shader
    GLuint loc = glGetAttribLocation(program, "vPosition");
    glEnableVertexAttribArray(loc);
    glVertexAttribPointer(loc, 2, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0));

    glClearColor(1.0, 1.0, 1.0, 1.0); // white background
}

//----------------------------------------------------------------------------

void
display(void) {
    glClear(GL_COLOR_BUFFER_BIT); // clear the window
    glDrawArrays(GL_LINE_STRIP, 0, NumPoints); // draw the points
    glFlush();
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
    glutInitDisplayMode(GLUT_RGBA);
    glutInitWindowSize(800, 600);

    // If you are using freeglut, the next two lines will check if 
    // the code is truly 3.2. Otherwise, comment them out

    glutInitContextVersion(4, 0);
    glutInitContextProfile(GLUT_CORE_PROFILE);

    glutCreateWindow("Scene");


    glewInit();

    init();

    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);

    glutMainLoop();
    return 0;
}
