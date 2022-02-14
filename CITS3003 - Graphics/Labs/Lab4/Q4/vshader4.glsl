#version 150

in  vec3 vPosition;
in  vec3 vColor;
out vec4 color;

uniform mat2 multipliers;
uniform int rotType;

void
main()
{
   // Draw values as is
   gl_Position = vec4(vPosition, 1); 

   // Do Y rotation by changing XZ
   gl_Position.xz = multipliers * gl_Position.xz;  

   // Set color
   color = vec4( vColor, 1.0 );
}
