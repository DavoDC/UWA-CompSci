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

   // Do certain type of rotation
   switch (rotType) {
        case 2:
            gl_Position.xz = multipliers * gl_Position.xz;  
            break;
        case 0:
            gl_Position.xy = multipliers * gl_Position.xy;  
            break;
        case 1:
            gl_Position.yz = multipliers * gl_Position.yz;  
            break;
   }

   // Set color
   color = vec4( vColor, 1.0 );
}
