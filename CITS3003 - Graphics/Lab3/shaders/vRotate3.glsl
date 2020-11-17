#version 150

in  vec3 vPosition;
in  vec3 vColor;
out vec4 color;

uniform mat2 multipliers;

void
main()
{
  // Long way 1:
  // multipliers[0][0] for the x multiplier affecting the x coordinate,
  // multipliers[0][1] for the y multiplier affecting the x coordinate 
  // (replacing also the -ve since instead it's in the matrix),
  // multipliers[1][0] for the x multiplier affecting the y coordinate,
  // multipliers[1][1] for the y multiplier affecting the y coordinate,
  // gl_Position = vec4(
  //	    vPosition.x*cosAngle - vPosition.y*sinAngle,
  //	    vPosition.x*sinAngle + vPosition.y*cosAngle,
  //    0.0, 1.0);

  // Long way 2:
  //gl_Position = vec4(
  //	    vPosition.x*multipliers[0][0] + vPosition.y*multipliers[0][1],
  //	    vPosition.x*multipliers[1][0] + vPosition.y*multipliers[1][1],
  //	    0.0, 1.0);

  // X and Y rotation
  // gl_Position = vec4(0, 0, 0 , 1);
  // gl_Position.xy = multipliers * vPosition.xy; 

  // Y and Z rotation
  // Fixed x
  gl_Position = vec4(vPosition.x, 0, 0, 1); 
  // Multiplying y and z
  gl_Position.yz = multipliers * vPosition.yz;  
	
    color = vec4( vColor, 1.0 );
}
