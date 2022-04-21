
// THIS IS DAVID'S SCENE START FILE

// Include header files
#include "Angel.h"
#include <stdlib.h>
#include <dirent.h>
#include <time.h>
#include <string.h>
#include <assimp/cimport.h> // Open Asset Importer header files
#include <assimp/scene.h>
#include <assimp/postprocess.h>

// Set window dimensions
GLint windowHeight = 640, windowWidth = 960;

// gnatidread.cpp is the CITS3003 "Graphics n Animation Tool Interface & Data
// Reader" code.  This file contains parts of the code that you shouldn't need
// to modify (but, you can).
#include "gnatidread.h"

// Import the C++ standard functions (e.g., min)
using namespace std;

// IDs for the GLSL program and GLSL variables.
GLuint shaderProgram; // The number identifying the GLSL shader program
GLuint vPosition, vNormal, vTexCoord; // IDs for vshader input vars (from glGetAttribLocation)
GLuint projectionU, modelViewU; // IDs for uniform variables (from glGetUniformLocation)

// Camera variables
static float viewDist = 1.5; // Distance from the camera to the centre of the scene
static float camRotSidewaysDeg = 0; // rotates the camera sideways around the centre
static float camRotUpAndOverDeg = 20; // rotates the camera up and over the centre.

// Projection matrix - set in the reshape function
mat4 projection;

// View matrix - set in the display function.
mat4 view;

// Window title variables
char lab[] = "Project1";
char *programName = NULL; // Set in main
int numDisplayCalls = 0; // Used to calculate the number of frames per second

// Directory paths

// DC edited first one
char dirDefault1[] = "res/models-textures";
char dirDefault3[] = "/tmp/models-textures";
char dirDefault4[] = "/d/models-textures";
char dirDefault2[] = "/cslinux/examples/CITS3003/project-files/models-textures";

//------Meshes----------------------------------------------------------------
// Uses the type aiMesh from ../../assimp--3.0.1270/include/assimp/mesh.h
// (numMeshes is defined in gnatidread.h)
aiMesh* meshes[numMeshes]; // For each mesh we have a pointer to the mesh to draw
GLuint vaoIDs[numMeshes]; // and a corresponding VAO ID from glGenVertexArrays

// -----Textures--------------------------------------------------------------
// (numTextures is defined in gnatidread.h)
texture* textures[numTextures]; // An array of texture pointers - see gnatidread.h
GLuint textureIDs[numTextures]; // Stores the IDs returned by glGenTextures

/**
 * Scene Object Structure
 * For each object in a scene we store the following
 * Note: the following is exactly what the sample solution uses,
 * you can do things differently if you want.
 */
typedef struct {
    vec4 loc;
    float scale;
    float angles[3]; // Rotations around X, Y and Z axes.
    float diffuse, specular, ambient; // Amount of each light component
    float shine;
    vec3 rgb;
    float brightness; // Multiplies all colours
    int meshId;
    int texId;
    float texScale;
} SceneObject;

// Scene variables
// Scenes with more than 1024 objects seem unlikely
const int maxObjects = 1024;
// An array storing the objects currently in the scene.
SceneObject sceneObjs[maxObjects];
// How many objects are currently in the scene.
int nObjects = 0;
// The current object
int currObject = -1;
// The object currently being modified
int toolObj = -1;

//----------------------------------------------------------------------------

/**
 * Loads a texture by number, and binds it for later use.
 * @param i
 */
void loadTextureIfNotAlreadyLoaded(int i) {

    // Stop if the texture is already loaded.
    if (textures[i] != NULL) return;

    textures[i] = loadTextureNum(i);
    CheckError();
    glActiveTexture(GL_TEXTURE0);
    CheckError();

    // Based on: http://www.opengl.org/wiki/Common_Mistakes
    glBindTexture(GL_TEXTURE_2D, textureIDs[i]);
    CheckError();

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, textures[i]->width, textures[i]->height,
            0, GL_RGB, GL_UNSIGNED_BYTE, textures[i]->rgbData);
    CheckError();
    glGenerateMipmap(GL_TEXTURE_2D);
    CheckError();

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    CheckError();
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    CheckError();
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    CheckError();
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    CheckError();

    glBindTexture(GL_TEXTURE_2D, 0);
    CheckError(); // Back to default texture
}

/**
 * Mesh loading
 * The following uses the Open Asset Importer library via loadMesh in
 * gnatidread.h to load models in .x format, including vertex positions,
 * normals, and texture coordinates.
 * You shouldn't need to modify this - it's called from drawMesh below.
 * @param meshNumber
 */
void loadMeshIfNotAlreadyLoaded(int meshNumber) {
    if (meshNumber >= numMeshes || meshNumber < 0) {
        printf("Error - no such model number");
        exit(1);
    }

    if (meshes[meshNumber] != NULL)
        return; // Already loaded

    aiMesh* mesh = loadMesh(meshNumber);
    meshes[meshNumber] = mesh;

    glBindVertexArray(vaoIDs[meshNumber]);

    // Create and initialize a buffer object for positions and texture coordinates, initially empty.
    // mesh->mTextureCoords[0] has space for up to 3 dimensions, but we only need 2.
    GLuint buffer[1];
    glGenBuffers(1, buffer);
    glBindBuffer(GL_ARRAY_BUFFER, buffer[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof (float)*(3 + 3 + 3) * mesh->mNumVertices,
            NULL, GL_STATIC_DRAW);

    int nVerts = mesh->mNumVertices;
    // Next, we load the position and texCoord data in parts.
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof (float)*3 * nVerts, mesh->mVertices);
    glBufferSubData(GL_ARRAY_BUFFER, sizeof (float)*3 * nVerts, sizeof (float)*3 * nVerts, mesh->mTextureCoords[0]);
    glBufferSubData(GL_ARRAY_BUFFER, sizeof (float)*6 * nVerts, sizeof (float)*3 * nVerts, mesh->mNormals);

    // Load the element index data
    GLuint elements[mesh->mNumFaces * 3];
    for (GLuint i = 0; i < mesh->mNumFaces; i++) {
        elements[i * 3] = mesh->mFaces[i].mIndices[0];
        elements[i * 3 + 1] = mesh->mFaces[i].mIndices[1];
        elements[i * 3 + 2] = mesh->mFaces[i].mIndices[2];
    }

    GLuint elementBufferId[1];
    glGenBuffers(1, elementBufferId);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof (GLuint) * mesh->mNumFaces * 3, elements, GL_STATIC_DRAW);

    // vPosition it actually 4D - the conversion sets the fourth dimension (i.e. w) to 1.0
    glVertexAttribPointer(vPosition, 3, GL_FLOAT, GL_FALSE, 0, BUFFER_OFFSET(0));
    glEnableVertexAttribArray(vPosition);

    // vTexCoord is actually 2D - the third dimension is ignored (it's always 0.0)
    glVertexAttribPointer(vTexCoord, 3, GL_FLOAT, GL_FALSE, 0,
            BUFFER_OFFSET(sizeof (float)*3 * mesh->mNumVertices));
    glEnableVertexAttribArray(vTexCoord);
    glVertexAttribPointer(vNormal, 3, GL_FLOAT, GL_FALSE, 0,
            BUFFER_OFFSET(sizeof (float)*6 * mesh->mNumVertices));
    glEnableVertexAttribArray(vNormal);
    CheckError();
}

/**
 * Applies current tool when mouse is activated
 * @param button
 * @param state
 * @param x
 * @param y
 */
static void mouseClickOrScroll(int button, int state, int x, int y) {

    // Process each type of input
    if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN) {
        // Left mouse button down
        if (glutGetModifiers() != GLUT_ACTIVE_SHIFT) activateTool(button);
        else activateTool(GLUT_LEFT_BUTTON);
    } else if (button == GLUT_LEFT_BUTTON && state == GLUT_UP) {
        // Left mouse button released
        deactivateTool();
    } else if (button == GLUT_MIDDLE_BUTTON && state == GLUT_DOWN) {
        // Middle mouse button down
        activateTool(button);
    } else if (button == GLUT_MIDDLE_BUTTON && state == GLUT_UP) {
        // Middle mouse button up
        deactivateTool();
    } else if (button == 3) {
        // Scroll up
        viewDist = (viewDist < 0.0 ? viewDist : viewDist * 0.8) - 0.05;
    } else if (button == 4) {
        // Scroll down
        viewDist = (viewDist < 0.0 ? viewDist : viewDist * 1.25) + 0.05;
    }
}

/**
 * Passive mouse motion
 * @param x
 * @param y
 */
static void mousePassiveMotion(int x, int y) {
    mouseX = x;
    mouseY = y;
}

/**
 * Do Z rotation for camera
 * @return
 */
mat2 camRotZ() {
    return rotZ(-camRotSidewaysDeg) * mat2(10.0, 0, 0, -10.0);
}

/**
 * Adjust view distance and sideways degree
 * @param cv
 */
static void adjustCamrotsideViewdist(vec2 cv) {
    // Print mouse coordinates
    // DC - Disabled
    // cout << cv << endl;

    // Use mouse X to change sideways degree
    camRotSidewaysDeg += cv[0];

    // Use mouse Y to modify view distance
    viewDist += cv[1];
}

/**
 * Adjust camera angles
 * @param su
 */
static void adjustcamSideUp(vec2 su) {
    camRotSidewaysDeg += su[0];
    camRotUpAndOverDeg += su[1];
}

/**
 * Adjust X and Z coordinates of current object
 * @param xz
 */
static void adjustLocXZ(vec2 xz) {
    sceneObjs[toolObj].loc[0] += xz[0];
    sceneObjs[toolObj].loc[2] += xz[1];
}

/**
 * Adjust Y scale of current object
 * @param sy
 */
static void adjustScaleY(vec2 sy) {
    sceneObjs[toolObj].scale += sy[0];
    sceneObjs[toolObj].loc[1] += sy[1];
}

/**
 * Set the mouse buttons to rotate the camera around the centre of the scene.
 */
static void doRotate() {
    setToolCallbacks(adjustCamrotsideViewdist, mat2(400, 0, 0, -2),
            adjustcamSideUp, mat2(400, 0, 0, -90));
}

/**
 * Add an object to the scene
 * @param id
 */
static void addObject(int id) {

    vec2 currPos = currMouseXYworld(camRotSidewaysDeg);
    sceneObjs[nObjects].loc[0] = currPos[0];
    sceneObjs[nObjects].loc[1] = 0.0;
    sceneObjs[nObjects].loc[2] = currPos[1];
    sceneObjs[nObjects].loc[3] = 1.0;

    if (id != 0 && id != 55) {
        sceneObjs[nObjects].scale = 0.005 * 2; // DC - Bigger objects
    }

    sceneObjs[nObjects].rgb[0] = 0.7;
    sceneObjs[nObjects].rgb[1] = 0.7;
    sceneObjs[nObjects].rgb[2] = 0.7;
    sceneObjs[nObjects].brightness = 1.0;

    sceneObjs[nObjects].diffuse = 1.0;
    sceneObjs[nObjects].specular = 0.5;
    sceneObjs[nObjects].ambient = 0.7;
    sceneObjs[nObjects].shine = 10.0;

    sceneObjs[nObjects].angles[0] = 0.0;
    sceneObjs[nObjects].angles[1] = 180.0;
    sceneObjs[nObjects].angles[2] = 0.0;

    sceneObjs[nObjects].meshId = id;
    sceneObjs[nObjects].texId = rand() % numTextures;
    sceneObjs[nObjects].texScale = 2.0;

    toolObj = currObject = nObjects++;
    setToolCallbacks(adjustLocXZ, camRotZ(),
            adjustScaleY, mat2(0.05, 0, 0, 10.0));
    glutPostRedisplay();
}

/**
 * The init function
 */
void init(void) {

    // Initialize the Open Asset Importer toolkit
    aiInit();

    // Allocate vertex array objects for meshes
    glGenVertexArrays(numMeshes, vaoIDs);
    CheckError();

    // Allocate texture objects
    glGenTextures(numTextures, textureIDs);
    CheckError();

    // Load shaders and use the resulting shader program
    // ### DC - Part F and G
    // - There are two sets of shaders - one for Part F, one for Part G
    // - Modify "ACTIVE" to choose which pair of shaders you want to use
    char ACTIVE = 'G'; // Can be 'F' or "G"
    char vertexShader[300];
    char fragmentShader[300];
    sprintf(vertexShader, "res/shaders/Part%cShaders/vShader%c.glsl", ACTIVE, ACTIVE);
    sprintf(fragmentShader, "res/shaders/Part%cShaders/fShader%c.glsl", ACTIVE, ACTIVE);
    shaderProgram = InitShader(vertexShader, fragmentShader);

    glUseProgram(shaderProgram);
    CheckError();

    // Initialize the vertex position attribute from the vertex shader
    vPosition = glGetAttribLocation(shaderProgram, "vPosition");
    vNormal = glGetAttribLocation(shaderProgram, "vNormal");
    CheckError();

    // Likewise, initialize the vertex texture coordinates attribute.
    vTexCoord = glGetAttribLocation(shaderProgram, "vTexCoord");
    CheckError();

    // Get project and model view matrix IDs
    projectionU = glGetUniformLocation(shaderProgram, "Projection");
    modelViewU = glGetUniformLocation(shaderProgram, "ModelView");

    // Initialize Object 0 - The ground
    addObject(0); // Square shape
    sceneObjs[0].loc = vec4(0.0, 0.0, 0.0, 1.0);
    sceneObjs[0].scale = 10.0; // Very large
    sceneObjs[0].angles[0] = 90.0; // Rotate it
    sceneObjs[0].texScale = 5.0; // Repeat the texture

    // Initialize Object 1 - The first light source
    addObject(55); // Sphere shape
    sceneObjs[1].loc = vec4(2.0, 1.0, 1.0, 1.0);
    sceneObjs[1].scale = 0.1; // Small
    sceneObjs[1].texId = 0; // Plain texture
    sceneObjs[1].brightness = 0.2 * 8; // Very bright
    sceneObjs[1].ambient = 0.2; // Lower

    // ### DC - Part I
    // Initialize Object 2 - The second light source
    addObject(55); // Sphere shape
    sceneObjs[2].loc = vec4(2.0, 1.0, 2.0, 1.0);
    sceneObjs[2].scale = 0.25; // Larger size
    sceneObjs[2].texId = 0; // Plain texture
    sceneObjs[2].brightness = 0.2 * 2; // Medium bright

    // ### DC - Part J
    // Initialize Object 3 - The spotlight
    addObject(55); // Sphere shape
    sceneObjs[3].loc = vec4(2.0, 1.0, 3.0, 1.0);
    sceneObjs[3].scale = 0.1; // Small size
    sceneObjs[3].texId = 0; // Plain texture
    sceneObjs[3].brightness = 0.2 * 10; // Very bright
    // Make spotlight shine downward by default
    sceneObjs[3].angles[0] = -120;
    sceneObjs[3].angles[1] = 90.0;
    sceneObjs[3].angles[2] = 45.0;


    // Seed RNG to vary starting scene
    srand(time(NULL));

    // Add random test mesh
    addObject(rand() % numMeshes); // A test mesh

    // We need to enable the depth test to discard fragments that
    // are behind previously drawn fragments for the same pixel.
    glEnable(GL_DEPTH_TEST);

    // Start in camera rotate mode.
    doRotate();

    // Set black background
    glClearColor(0.0, 0.0, 0.0, 1.0);
}

/**
 * Draw the mesh of a given scene object
 * @param sceneObj
 */
void drawMesh(SceneObject sceneObj) {

    // Activate a texture, loading if needed.
    loadTextureIfNotAlreadyLoaded(sceneObj.texId);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, textureIDs[sceneObj.texId]);

    // Texture 0 is the only texture type in this program, and is for the rgb
    // colour of the surface but there could be separate types for, e.g.,
    // specularity and normals.
    glUniform1i(glGetUniformLocation(shaderProgram, "texture"), 0);

    // Set the texture scale for the shaders
    glUniform1f(glGetUniformLocation(shaderProgram, "texScale"), sceneObj.texScale);

    // Set the projection matrix for the shaders
    glUniformMatrix4fv(projectionU, 1, GL_TRUE, projection);

    // Set the model matrix - this should combine translation, rotation and scaling based on what's
    // in the sceneObj structure (see near the top of the program).

    // ### DC - Part B - Added rotation matrices
    mat4 tranM = Translate(sceneObj.loc);
    mat4 rotM = RotateZ(sceneObj.angles[2]) * RotateY(sceneObj.angles[1])
            * RotateX(sceneObj.angles[0]);
    mat4 scalM = Scale(sceneObj.scale);
    mat4 model = tranM * rotM * scalM;

    // Set the model-view matrix for the shaders
    glUniformMatrix4fv(modelViewU, 1, GL_TRUE, view * model);

    // Activate the VAO for a mesh, loading if needed.
    loadMeshIfNotAlreadyLoaded(sceneObj.meshId);
    CheckError();
    glBindVertexArray(vaoIDs[sceneObj.meshId]);
    CheckError();

    // Draw triangles
    glDrawElements(GL_TRIANGLES, meshes[sceneObj.meshId]->mNumFaces * 3,
            GL_UNSIGNED_INT, NULL);
    CheckError();
}

/**
 * The display callback function
 */
void display(void) {

    // Increase number of calls to this function
    numDisplayCalls++;

    // Clear the buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    // May report a harmless GL_INVALID_OPERATION with GLEW on the first frame
    CheckError();

    // Set the view matrix. To start with this just moves the camera
    // backwards.  You'll need to add appropriate rotations.

    // ### DC - Part A - Added rotation matrices
    mat4 transM = Translate(0.0, 0.0, -viewDist);
    mat4 rotM = RotateX(camRotUpAndOverDeg) * RotateY(camRotSidewaysDeg);
    view = transM * rotM;

    // Give position of first light source to shader
    SceneObject lightObj1 = sceneObjs[1];
    vec4 lightPosition1 = view * lightObj1.loc;
    glUniform4fv(glGetUniformLocation(shaderProgram, "LightPosition1"),
            1, lightPosition1);
    CheckError();

    // ### DC - Part I
    // Give position of second light source to shader
    SceneObject lightObj2 = sceneObjs[2];
    vec4 lightPosition2 = view * lightObj2.loc;
    glUniform4fv(glGetUniformLocation(shaderProgram, "LightPosition2"),
            1, lightPosition2);
    CheckError();

    // ### DC - Part J - Spotlight
    // Position
    SceneObject lightObj3 = sceneObjs[3];
    vec4 lightPosition3 = view * lightObj3.loc;
    glUniform4fv(glGetUniformLocation(shaderProgram, "LightPosition3"),
            1, lightPosition3);
    CheckError();
    // Spotlight conesize (where -1 is large and 1 is small)
    float spotSize = -0.8;
    glUniform1f(glGetUniformLocation(shaderProgram, "spotSize"), spotSize);
    CheckError();
    // Spotlight direction
    vec4 spotDir = view
            * RotateZ(sceneObjs[3].angles[2])
            * RotateY(sceneObjs[3].angles[1])
            * RotateX(sceneObjs[3].angles[0]) * vec4(0.0, 1.0, 0.0, 0.0);
    glUniform4fv(glGetUniformLocation(shaderProgram, "spotDir"), 1, spotDir);
    CheckError();

    // For all scene objects
    for (int i = 0; i < nObjects; i++) {

        // Retrieve scene object
        SceneObject so = sceneObjs[i];

        // Calculate color
        // - Multiply RGB values
        vec3 rgb = so.rgb * lightObj1.rgb * lightObj2.rgb;
        // - Apply brightness
        rgb = rgb * so.brightness * lightObj1.brightness * lightObj2.brightness;
        // - Apply constant
        rgb = rgb * 2.0;

        // Update ambient component
        glUniform4fv(glGetUniformLocation(shaderProgram, "AmbientProduct"), 1,
                so.ambient * rgb);

        // Update diffuse component
        glUniform4fv(glGetUniformLocation(shaderProgram, "DiffuseProduct"), 1,
                so.diffuse * rgb);
        CheckError();

		// ### DC - Part H
		// The first half of the marks for Part H comes from separating the colour from the TEXTURE.		
		// This the part that requires changes to the (fragment) shader as stated in the question.
		//
		// The second part of the marks comes from separating the colour from the OBJECT.
		// The scene object colour is removed below: 
        // 
		// Calculate color WITHOUT OBJECT
        // - Multiply RGB values
		vec3 rgbSpecial = lightObj1.rgb * lightObj2.rgb; // REMOVED: so.rgb *
        // - Apply brightness
        rgbSpecial = rgbSpecial * so.brightness * lightObj1.brightness * lightObj2.brightness;
        // - Apply constant
        rgbSpecial = rgbSpecial * 2.0;
		
		// Update specular component
		// OLD:
        // glUniform4fv(glGetUniformLocation(shaderProgram, "SpecularProduct"), 1,
        //        so.specular * rgb);
		// NEW:
		glUniform4fv(glGetUniformLocation(shaderProgram, "SpecularProduct"), 1,
                  so.specular * rgbSpecial);

        // Update shininess
        glUniform1f(glGetUniformLocation(shaderProgram, "Shininess"), so.shine);
        CheckError();

        // ### DC - Part B - Update texture scale
        glUniform1f(glGetUniformLocation(shaderProgram, "texScale"), so.texScale);
        CheckError();

        // Draw mesh
        drawMesh(sceneObjs[i]);
    }

    // Swap double buffers
    glutSwapBuffers();
}

//----------------------------------------------------------------------------
//------Menus-----------------------------------------------------------------
//----------------------------------------------------------------------------

/**
 * Object menu
 * @param id
 */
static void objectMenu(int id) {
    deactivateTool();
    addObject(id);
}

/**
 * Texture menu
 * @param id
 */
static void texMenu(int id) {
    deactivateTool();
    if (currObject >= 0) {
        sceneObjs[currObject].texId = id;
        glutPostRedisplay();
    }
}

/**
 * Ground menu
 * @param id
 */
static void groundMenu(int id) {
    deactivateTool();
    sceneObjs[0].texId = id;
    glutPostRedisplay();
}

/**
 * Adjust Y and X angle
 * @param angle_yx
 */
static void adjustAngleYX(vec2 angle_yx) {
    sceneObjs[currObject].angles[1] += angle_yx[0];
    sceneObjs[currObject].angles[0] += angle_yx[1];
}

/**
 * Adjust Z angle and texture scale
 * @param az_ts
 */
static void adjustAngleZTexscale(vec2 az_ts) {
    sceneObjs[currObject].angles[2] += az_ts[0];
    sceneObjs[currObject].texScale += az_ts[1];
}

/**
 * Adjust Y and X angle of tool object
 * @param angle_yx
 */
static void adjustToolAngleYX(vec2 angle_yx) {
    sceneObjs[toolObj].angles[1] += angle_yx[0];
    sceneObjs[toolObj].angles[0] += angle_yx[1];
}

/**
 * Adjust Z angle of tool object
 * @param az_ts
 */
static void adjustToolAngleZ(vec2 az_ts) {
    sceneObjs[toolObj].angles[2] += az_ts[0];
}

/**
 * Adjust brightness callback
 * @param by
 */
static void adjustBrightnessY(vec2 by) {
    sceneObjs[toolObj].brightness += by[0];
    sceneObjs[toolObj].loc[1] += by[1];
}

/**
 * Adjust red and green
 * @param rg
 */
static void adjustRedGreen(vec2 rg) {
    sceneObjs[toolObj].rgb[0] += rg[0];
    sceneObjs[toolObj].rgb[1] += rg[1];
}

/**
 * Adjust blue and brightness
 * @param bl_br
 */
static void adjustBlueBrightness(vec2 bl_br) {
    sceneObjs[toolObj].rgb[2] += bl_br[0];
    sceneObjs[toolObj].brightness += bl_br[1];
}

/**
 * ### DC - Helper method for part C
 * Adjust ambient and diffuse
 * @param input
 */
static void adjustAmbientDiffuse(vec2 input) {
    sceneObjs[toolObj].ambient += input[0];
    sceneObjs[toolObj].diffuse += input[1];
}

/**
 * ### DC - Helper method for part C
 * Adjust specular and shine
 * @param input
 */
static void adjustSpecularShine(vec2 input) {
    sceneObjs[toolObj].specular += input[0];
    sceneObjs[toolObj].shine += input[1];

    // Do extra validity checks for shininess
    if (sceneObjs[toolObj].shine > 100) {
        sceneObjs[toolObj].shine = 100.0;
    } else if (sceneObjs[toolObj].shine < 0) {
        sceneObjs[toolObj].shine = 0.0;
    }
}

/**
 * Light menu callback
 * @param id
 */
static void lightMenu(int id) {
    deactivateTool();
    if (id == 70) {
        // Set object to be modified as sceneObject1 (Light Source 1)
        toolObj = 1;
        // Attach callbacks to menu item
        setToolCallbacks(adjustLocXZ, camRotZ(),
                adjustBrightnessY, mat2(1.0, 0.0, 0.0, 10.0));

    } else if (id == 71) {
        // Set object to be modified as sceneObject1 (Light Source 1)
        toolObj = 1;
        // Attach callbacks to menu item
        setToolCallbacks(adjustRedGreen, mat2(1.0, 0, 0, 1.0),
                adjustBlueBrightness, mat2(1.0, 0, 0, 1.0));
    } else if (id == 80) {
        // ### DC - Part I
        // Set object to be modified as sceneObject2 (Light Source 2)
        toolObj = 2;
        // Attach callbacks to menu item
        setToolCallbacks(adjustLocXZ, camRotZ(),
                adjustBrightnessY, mat2(1.0, 0.0, 0.0, 10.0));
    } else if (id == 81) {
        // ### DC - Part I
        // Set object to be modified as sceneObject2 (Light Source 2)
        toolObj = 2;
        // Attach callbacks to menu item
        setToolCallbacks(adjustRedGreen, mat2(1.0, 0, 0, 1.0),
                adjustBlueBrightness, mat2(1.0, 0, 0, 1.0));
    } else if (id == 82) {
        // ### DC - Part J - Move Spotlight
        // Set object to be modified
        toolObj = 3;
        // Attach callbacks to menu item
        setToolCallbacks(adjustLocXZ, camRotZ(),
                adjustBrightnessY, mat2(1.0, 0.0, 0.0, 10.0));
    } else if (id == 83) {
        // ### DC - Part J - Change Spotlight Direction
        // Set object to be modified
        toolObj = 3;
        // Attach callbacks to menu item
        setToolCallbacks(adjustToolAngleYX, mat2(400, 0, 0, -400),
                adjustToolAngleZ, mat2(400, 0, 0, 15));
    } else {
        printf("Error in lightMenu\n");
    }
}

/**
 * Helper method for creating menus
 * DC - Changed string size to 200
 * @param size
 * @param menuEntries
 * @param menuFn
 * @return
 */
static int createArrayMenu(int size, const char menuEntries[][200], void(*menuFn)(int)) {
    int nSubMenus = (size - 1) / 10 + 1;
    int subMenus[nSubMenus];

    for (int i = 0; i < nSubMenus; i++) {
        subMenus[i] = glutCreateMenu(menuFn);
        for (int j = i * 10 + 1; j <= min(i * 10 + 10, size); j++)
            glutAddMenuEntry(menuEntries[j - 1], j);
        CheckError();
    }
    int menuId = glutCreateMenu(menuFn);

    for (int i = 0; i < nSubMenus; i++) {
        char num[6];
        sprintf(num, "%d-%d", i * 10 + 1, min(i * 10 + 10, size));
        glutAddSubMenu(num, subMenus[i]);
        CheckError();
    }
    return menuId;
}

/**
 * Material menu callback
 * @param id
 */
static void materialMenu(int id) {
    deactivateTool();
    if (currObject < 0) return;

    if (id == 10) {
        toolObj = currObject;
        setToolCallbacks(adjustRedGreen, mat2(1, 0, 0, 1),
                adjustBlueBrightness, mat2(1, 0, 0, 1));
    } else if (id == 20) {

        // ### DC - Part C
        // Set current object
        toolObj = currObject;
        // Adjusting the amounts of ambient, diffuse and specular light
        // > Ambient = left mouse left and right
        // > Diffuse = left mouse up and down
        // > Specular = middle mouse left and right
        // > Shine = middle mouse up and down
        setToolCallbacks(adjustAmbientDiffuse, mat2(1, 0, 0, 1),
                adjustSpecularShine, mat2(1, 0, 0, 100));

    } else {
        printf("Error in materialMenu\n");
    }
}

/**
 * DC - Helper method that returns true if scene object index is valid
 * @param index
 * @return
 */
static bool isIndexInvalid(int index) {
    return (index <= 0 || index > nObjects - 1);
}

/**
 * Check the current object index and notify accordingly
 * @return
 */
static bool isSelectionInvalid() {

    // Get validity of current index
    bool invalid = isIndexInvalid(currObject);

    // If index is invalid
    if (invalid) {

        // Notify and advise
        printf("\n\n You cannot do this action, ");
        printf("\n as no object is currently selected");
        printf("\n Please use 'setCurrentObject' and try again");
        printf("\n\n");

    } else if (currObject <= 3) {

        // Else If light sources are selected
        // Notify and advise
        printf("\n\n You cannot delete or duplicate any of the light sources");
        printf("\n\n");

        // Return invalid
        return true;
    }


    // Return valid
    return invalid;
}

/**
 * Process a new current object index
 * @param newIndex
 */
static void processNewIndex(int newIndex, char newObDesc[200]) {

    // Space
    printf("\n");

    // If input is bad
    if (isIndexInvalid(newIndex)) {

        // Notify user
        printf("\n That index value is invalid");
        printf("\n The current object index has not been changed");

    } else {

        // Else if input is good:

        // Update current object index and notify
        currObject = newIndex;
        printf("\n The current object index has been successfully updated");

        // If significant string given, print out
        if (strlen(newObDesc) > 2) {
            printf("\n The current object index is now: %s", newObDesc);
        }
    }

    // Space
    printf("\n");
}

/**
 * ### Part J Helper Method - Set Current Object
 */
static void setObject() {
    // Print start message
    printf("\n\n####### SET CURRENT OBJECT #######");
    printf("\n\n The scene objects available are:");

    // Print a list of all objects
    // For all scene objects
    char objectList [1024][200];
    for (int i = 0; i < nObjects; i++) {

        // Retrieve scene object
        SceneObject so = sceneObjs[i];

        // Get object ID
        int objID = so.meshId;

        // If object ID is 0
        if (objID == 0) {
            // Skip object
            continue;
        }

        // Save string with object index and name
        sprintf(objectList[i], "(Index %i) %s", i, objectMenuEntries[objID - 1]);

        // Print string
        printf("\n  > %s", objectList[i]);
    }

    // Print current object index
    char currObStr[200];
    if (currObject == -1) {
        sprintf(currObStr, "%s", "Nothing");
    } else {
        sprintf(currObStr, "%s", objectList[currObject]);
    }
    printf("\n\n The current object: %s", currObStr);

    // Get input from user
    printf("\n\n Please enter the new, desired current object index:");
    printf("\n ");
    int newIndex = 0;
    cin >> newIndex;

    // Process new current object index
    processNewIndex(newIndex, objectList[newIndex]);

    // Make space
    printf("\n\n");
}

/**
 * ### Part J Helper Method - Delete Current Object
 */
static void deleteObject() {

    // If current object index is invalid
    if (isSelectionInvalid()) {

        // Do not process further
        return;
    }

    // Delete current object and shift objects left
    for (int i = currObject; i < nObjects; i++) {
        sceneObjs[i] = sceneObjs[i + 1];
    }

    // Reduce number of objects
    nObjects--;

    // Reset current object index
    currObject = -1;

    // Update display
    glutPostRedisplay();
}

/**
 * ### Part J Helper Method
 */
static void duplicateObject() {

    // If current object index is invalid
    if (isSelectionInvalid()) {

        // Do not process further
        return;
    }

    // If we have reached the maximum number of objects
    if (nObjects >= maxObjects) {

        // Do not process further
        return;
    }

    // Save current object
    SceneObject currO = sceneObjs[currObject];

    // Change location slightly (so they are not inside eachother)
    currO.loc.x = currO.loc.x + 1.0;

    // Increase number of objects
    nObjects++;

    // Set current object as new object
    currObject = (nObjects - 1);

    // Fill current object with copy
    sceneObjs[currObject] = currO;

    // Update display
    glutPostRedisplay();
}

/**
 * Mainmenu callback
 * @param id
 */
static void mainmenu(int id) {

    deactivateTool();
    if (id == 41 && currObject >= 0) {
        toolObj = currObject;
        setToolCallbacks(adjustLocXZ, camRotZ(),
                adjustScaleY, mat2(0.05, 0, 0, 10));
    }
    if (id == 50)
        doRotate();
    if (id == 55 && currObject >= 0) {
        setToolCallbacks(adjustAngleYX, mat2(400, 0, 0, -400),
                adjustAngleZTexscale, mat2(400, 0, 0, 15));
    }
    // ### Part J Menu Item Actions
    if (id == 90) {
        setObject();
    } else if (id == 91) {
        deleteObject();
    } else if (id == 92) {
        duplicateObject();
    } else if (id == 99) {
        // EXIT command
        exit(0);
    }
}

/**
 * Quick method for changing current object
 * @param num
 */
static void quickSetObject(int num) {

    ///// Process input as new current object index
    // Make empty string as array 
    // (to prevent warning:
    // 'ISO C++ forbids converting a string constant to‘char*’warning)
    char empty[] = "";
    // Give to function
    processNewIndex(num, empty);

    // Make tools work with new value
    toolObj = currObject;
}

/**
 * Make menu
 */
static void makeMenu() {

    // Create sub menus
    int objectId = createArrayMenu(numMeshes, objectMenuEntries, objectMenu);

    // Create quick set object menu
    int quickId = createArrayMenu(20, numberEntries, quickSetObject);

    int materialMenuId = glutCreateMenu(materialMenu);
    glutAddMenuEntry("R/G/B/All", 10);
    // ### DC - Part C - Removed UNIMPLEMENTED
    glutAddMenuEntry("Ambient/Diffuse/Specular/Shine", 20);

    int texMenuId = createArrayMenu(numTextures, textureMenuEntries, texMenu);
    int groundMenuId = createArrayMenu(numTextures, textureMenuEntries, groundMenu);

    int lightMenuId = glutCreateMenu(lightMenu);
    glutAddMenuEntry("Move Light 1", 70);
    glutAddMenuEntry("R/G/B/All Light 1", 71);
    glutAddMenuEntry("Move Light 2", 80);
    glutAddMenuEntry("R/G/B/All Light 2", 81);
    glutAddMenuEntry("Move Spotlight", 82); // DC - Part J
    glutAddMenuEntry("Change Spotlight Direction", 83); // DC - Part J

    // Make menu
    glutCreateMenu(mainmenu);
    glutAddMenuEntry("Rotate/Move Camera", 50);
    glutAddSubMenu("Ground Texture", groundMenuId);
    glutAddSubMenu("Adjust Lights", lightMenuId);
    glutAddSubMenu("Add Object", objectId);
    glutAddMenuEntry("Object Position/Scale", 41);
    glutAddMenuEntry("Object Rotation/Texture Scale", 55);
    glutAddSubMenu("Object Material", materialMenuId);
    glutAddSubMenu("Object Texture", texMenuId);
    glutAddSubMenu("Set Current Object (Quick)", quickId); // DC - Part J
    glutAddMenuEntry("Set Current Object (Full)", 90); // DC - Part J
    glutAddMenuEntry("Delete Current Object", 91); // DC - Part J
    glutAddMenuEntry("Duplicate Current Object", 92); // DC - Part J
    glutAddMenuEntry("EXIT", 99);

    // Attach menu to right mouse button
    glutAttachMenu(GLUT_RIGHT_BUTTON);
}

/**
 * Keyboard callback
 * @param key
 * @param x
 * @param y
 */
void keyboard(unsigned char key, int x, int y) {
    switch (key) {
        case 033:
            exit(EXIT_SUCCESS);
            break;
    }
}

/**
 * Idle callback
 */
void idle(void) {
    glutPostRedisplay();
}

/**
 * Reshape callback
 * @param width
 * @param height
 */
void reshape(int width, int height) {

    // Update dimensions
    windowWidth = width;
    windowHeight = height;

    // Update viewport
    glViewport(0, 0, width, height);

    // You'll need to modify this so that the view is similar to that in the
    // sample solution.
    // In particular:
    //     - the view should include "closer" visible objects (slightly tricky)
    //     - when the width is less than the height, the view should adjust so
    //         that the same part of the scene is visible across the width of
    //         the window.

    // ### DC - Part D - To allow for closer zooming in:
    // - Reduced nearDist from 0.2 to 0.01
    // - Increased farDist by 10
    GLfloat nearDist = 0.01;
    GLfloat farDist = 1000.0;

    // ### DC - Part E - Change window reshape behavior
    // Set resize ratio factors according to relative size of dimensions
    GLfloat left = 0, right = 0, bottom = 0, top = 0;
    if (width < height) {
        left = 1.0f;
        right = 1.0f;
        bottom = (float) height / (float) width;
        top = bottom;
    } else {
        left = (float) width / (float) height;
        right = left;
        bottom = 1.0f;
        top = 1.0f;
    }

    // Adjust view window
    projection = Frustum(-nearDist * left, nearDist * right,
            -nearDist * bottom, nearDist * top, nearDist, farDist);
}

/**
 * Timer callback
 * @param unused
 */
void timer(int unused) {
    char title[256];
    sprintf(title, "%s %s: %d Frames Per Second @ %d x %d",
            lab, programName, numDisplayCalls, windowWidth, windowHeight);

    glutSetWindowTitle(title);

    numDisplayCalls = 0;
    glutTimerFunc(1000, timer, 1);
}

/**
 * Error function
 * @param fileName
 */
void fileErr(char* fileName) {
    printf("Error reading file: %s\n", fileName);
    printf("When not in the CSSE labs, you will need to include the directory containing\n");
    printf("the models on the command line, or put it in the same folder as the exectutable.");
    exit(1);
}

/**
 * Main method
 * @param argc
 * @param argv
 * @return
 */
int main(int argc, char* argv[]) {

    // My starting message
    printf("\n");
    printf("\n ############ Welcome to David's CITS3003 Project ############");
    printf("\n");

    // Get the program name, excluding the directory, for the window title
    programName = argv[0];
    for (char *cpointer = argv[0]; *cpointer != 0; cpointer++)
        if (*cpointer == '/' || *cpointer == '\\') programName = cpointer + 1;

    // Set the models-textures directory, via the first argument or some handy defaults.
    if (argc > 1)
        strcpy(dataDir, argv[1]);
    else if (opendir(dirDefault1)) strcpy(dataDir, dirDefault1);
    else if (opendir(dirDefault2)) strcpy(dataDir, dirDefault2);
    else if (opendir(dirDefault3)) strcpy(dataDir, dirDefault3);
    else if (opendir(dirDefault4)) strcpy(dataDir, dirDefault4);
    else fileErr(dirDefault1);

    // Init GLUT
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_RGBA | GLUT_DOUBLE | GLUT_DEPTH);
    glutInitWindowSize(windowWidth, windowHeight);
    glutInitContextProfile(GLUT_CORE_PROFILE);
    glutCreateWindow("Initialising...");

    // With some old hardware yields GL_INVALID_ENUM, if so use glewExperimental.
    // This bug is explained at: http://www.opengl.org/wiki/OpenGL_Loading_Library
    glewInit();
    CheckError();

    // Call init() function
    init();
    CheckError();

    // Register GLUT callbacks
    glutDisplayFunc(display);
    glutKeyboardFunc(keyboard);
    glutIdleFunc(idle);
    glutMouseFunc(mouseClickOrScroll);
    glutPassiveMotionFunc(mousePassiveMotion);
    glutMotionFunc(doToolUpdateXY);
    glutReshapeFunc(reshape);
    glutTimerFunc(1000, timer, 1);
    CheckError();

    makeMenu();
    CheckError();

    // Enter event loop
    glutMainLoop();
    return 0;
}
