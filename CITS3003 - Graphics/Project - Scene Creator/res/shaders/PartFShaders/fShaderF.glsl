// Shared between shaders
varying vec4 color;
varying vec2 texCoord;  // The third coordinate is always 0.0 and is discarded

// Application variables
uniform sampler2D texture;

void main()
{
    // Set fragment color
    gl_FragColor = color * texture2D(texture, texCoord * 2.0);
}
