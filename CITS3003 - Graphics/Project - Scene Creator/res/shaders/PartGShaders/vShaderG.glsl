// Shared between shaders
varying vec2 texCoord;
varying vec4 vPos; // DC
varying vec3 vNorm; // DC

// Application variables
attribute vec4 vPosition;
attribute vec3 vNormal;
attribute vec2 vTexCoord;
uniform mat4 ModelView;
uniform mat4 Projection;

void main()
{
    // Give values to fragment shader
    vPos.xyz = vPosition.xyz;
    vPos.w = 1.0; // Ensure w = 1
    vNorm = vNormal;
    texCoord = vTexCoord;

     // Set position
    gl_Position = Projection * ModelView * vPos;
}
