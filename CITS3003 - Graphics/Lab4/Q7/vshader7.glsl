#version 150

in  vec3 vPosition;
in  vec3 vColor;
out vec4 color;

uniform mat3 rotMatrix;

void
main()
{
   // Apply rotation
   gl_Position = vec4(rotMatrix * vPosition, 1.0);

   // Set color
   color = vec4( vColor, 1.0 );
}
