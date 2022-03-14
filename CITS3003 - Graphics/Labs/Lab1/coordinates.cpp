
#include "Angel.h"

const int NumPoints = 4;

//----------------------------------------------------------------------------
// OPENGL Co-ordinates testing
// Results:
// Center is  0,0
// Top is     0,+1
// Bottom is  0,-1
// Right is  +1,0
// Left is   -1,0
//----------------------------------------------------------------------------

void
init(void) {
    // Points array
    vec2 points[NumPoints];

    // Test 1 - Creates small square at center
    // Conclusion: 0.0 is centre
    points[0] = vec2(0.0, 0.0);
    points[1] = vec2(0.0, 0.1);
    points[2] = vec2(0.1, 0.1);
    points[3] = vec2(0.1, 0.0);

    // Test 2 - Modify points above and print
    printf("\n");
    for (int i = 0; i < 4; i++) {
        // Test 2.1 - Add 0.5 to Y component
        // Result: Moved up but not to top, so max probs 1.0
        //points[i].y = points[i].y + 0.5;

        // Test 2.2 - Add 1.0 to Y
        // Result: Nothing on screen
        //points[i].y = points[i].y + 1.0;

        // Test 2.3 - Add 0.9 to Y
        // Result: Shows at top
        // Conclusion: Top is defs 1.0
        //points[i].y = points[i].y + 0.9;

        // Test 2.4 - Take 0.9 from Y
        // Result: Shows at bottom
        // Conclusion: Bottom is -1.0
        // points[i].y = points[i].y - 0.9;

        // Test 2.5 - Add 0.9 to X
        // Result: Square on right
        // Conclusion: Right is 1.0
        // points[i].x = points[i].x + 0.9;

        // Test 2.6 - Take 0.9 from X
        // Result: Square on left
        // Conclusion: Left is -1.0
        // points[i].x = points[i].x - 0.9;

        // Print new points
        printf("\nnew_point: %f, %f", points[i].x, points[i].y);
    }
    printf("\n");


    // Create a vertex array object
    GLuint vao; // Holder
    glGenVertexArrays(1, &vao); // Type
    glBindVertexArray(vao); // Bind

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
    // Clear the window
    glClear(GL_COLOR_BUFFER_BIT);

    // Draw point as points
    // glDrawArrays(GL_POINTS, 0, NumPoints);

    // Draw points as line strip
    // glDrawArrays(GL_LINE_STRIP, 0, NumPoints);

    // Draw points as line loop
    glDrawArrays(GL_LINE_LOOP, 0, NumPoints);

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
    // Message
    printf("\n######## coordinates.cpp ########\n");

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
