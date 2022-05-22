// Shared between shaders
varying vec2 texCoord; // The third coordinate is always 0.0 and is discarded
varying vec4 vPos; // DC
varying vec3 vNorm; // DC

// Application variables
uniform vec4 AmbientProduct, DiffuseProduct, SpecularProduct;
uniform mat4 ModelView;
uniform vec4 LightPosition1; // Original light source
uniform vec4 LightPosition2; // Directional light source
uniform vec4 LightPosition3; // Spotlight
uniform float spotSize; // Spotlight conesize
uniform vec4 spotDir; // Spotlight direction
uniform float Shininess;
uniform float texScale; // Texture scale (Part B)
uniform sampler2D texture;

void main()
{
    ////// Transform vertex position into eye coordinates
    vec3 pos = (ModelView * vPos).xyz;


    ////// LIGHT VECTORS
    // Light 1
    // The vector to the original light from the vertex
    vec3 Lvec = LightPosition1.xyz - pos;

    // Light 2 (DC - Part I)
    // The vector to the second light (negative so 'moving up -> floor brighter')
    vec3 Lvec2 = -LightPosition2.xyz;

    // Light 3 (DC - Part J)
    // The vector to the spotlight/light 3 from the vertex
    vec3 spotVec = LightPosition3.xyz - pos;


    ////// Unit direction vectors for Blinn-Phong shading calculation
    // Directions to the light source
    // Light 1
    vec3 L = normalize( Lvec );
    // Light 2 (DC - Part I)
    vec3 L2 = normalize( Lvec2 );
    // Light 3 (DC - Part J)
    vec3 L3 = normalize( spotVec );

    ////// E VALUE
    // Direction to the eye/camera
    vec3 E = normalize( -pos );
	

    ////// HALF WAY VECTORS
    // Light 1
    vec3 H = normalize( L + E );
    // Light 2 (DC - Part I)
    vec3 H2 = normalize( L2 + E ); 
    // Light 3 (DC - Part J)
    vec3 H3 = normalize ( L3 + E);


    ////// N VALUE
    // Transform vertex normal into eye coordinates
    // (assumes scaling is uniform across dimensions)
    vec3 N = normalize( (ModelView * vec4(vNorm, 0.0)).xyz );


    ////// LIGHT COMPONENTS

    /// Ambient components
    vec4 globalAmbient = vec4(0.05, 0.05, 0.05, 1.0);
    vec4 ambient1 = AmbientProduct;
    vec4 ambient2 = globalAmbient; 
    vec4 ambient3 = globalAmbient;
    // Note: The last two have been made constant to emphasize the 
    // light attenuation effect of the first light source

    /// Diffuse Component
    // Light Source 1
    float Kd1 = max( dot(L, N), 0.0 );
    vec4 diffuse1 = Kd1 * DiffuseProduct;
    // Light Source 2 (Part I)
    float Kd2 = max( dot(L2, N), 0.0 );
    vec4 diffuse2 = Kd2 * DiffuseProduct;
    // Light Source 3 (Spotlight) (Part J)
    float Kd3 = max( dot(L3, N), 0.0 );
    vec4 diffuse3 = Kd3 * DiffuseProduct;

    /// Specular Component
    // Light Source 1
    float Ks = pow( max(dot(N, H), 0.0), Shininess );
    vec4 specular1 = Ks * SpecularProduct;
    // Light Source 2 (Part I)
    float Ks2 = pow( max(dot(N, H2), 0.0), Shininess );
    vec4 specular2 = Ks2 * SpecularProduct;
    // Light Source 3 (Spotlight) (Part J)
    float Ks3 = pow( max(dot(N, H3), 0.0), Shininess );
    vec4 specular3 = Ks3 * SpecularProduct;


    ////// SPOTLIGHT CALCULATION - Part J
    // Theta Value. See https://learnopengl.com/Lighting/Light-casters
    float thetaValue = dot(L3, normalize(spotDir.xyz)); 

    // If fragment is outside of spotlight cone
    if(thetaValue > spotSize) {

        // Nullify light components
        ambient3 = vec4(0.0, 0.0, 0.0, 0.0);
        diffuse3 = vec4(0.0, 0.0, 0.0, 0.0);
        specular3 = vec4(0.0, 0.0, 0.0, 0.0);
    }

    ////// SPECULAR CHECKS
    // If the angle between the normal and the light direction 
    // is greater than 90 degrees, then set the specular contribution to 0.
    // So surfaces facing away from the light don't get specular light from it.
    // 
    // Light Source 1
    if (dot(L, N) < 0.0 ) {
	    specular1 = vec4(0.0, 0.0, 0.0, 1.0);
    }  
    // Light Source 2 (Part I)
    if( dot(L2, N) < 0.0 ) {
	    specular2 = vec4(0.0, 0.0, 0.0, 1.0);
    }
    // Light Source 3 (Spotlight) (Part J)
    if( dot(L3, N) < 0.0 ) {
	    specular3 = vec4(0.0, 0.0, 0.0, 1.0);
    }

    ////// ATTENUATION CALCULATIONS
    // Light Source 1 Attenuation
    // Get distance between the light and vertex
    float dist1 = length(Lvec);
    // Calculate light attenuation factor
    float a1 = 0.5;
    float b1 = 0.25;
    float c1 = 0.1;
    float attf1 = 1.0 / (a1 + (b1 * dist1) + (c1 * pow(dist1, 2.0)));

    // Light Source 3 Attenuation
    // Get distance between the light and vertex
    float dist3 = length(spotVec);
    // Calculate light attenuation factor
    float b3 = 0.2;
    float c3 = 0.1;
    float attf3 = 1.0 / ((b3 * dist3) + (c3 * pow(dist3, 2.0)));


    ////// COLOR CALCULATION
    // Start with globalAmbient
    vec4 color = globalAmbient;
    /// Light Source 1
    // DC - Part H - Removed specular (previously added after diffuse)
    color.rgb = color.rgb + (attf1 * (ambient1 + diffuse1)).rgb;
    /// Light Source 2
    // DC - Part I - Added second light source components (no attenuation)
    color.rgb = color.rgb + (ambient2 + diffuse2).rgb;
    /// Light Source 3 (Spotlight) - Part J
    color.rgb = color.rgb + (attf3 * (ambient3 + diffuse3)).rgb;
    color.a = 1.0;
    
    
    ////// SET FRAGMENT COLOR
    // DC - Part B - Apply texture scale factor
    gl_FragColor = color * texture2D(texture, texCoord * 2.0 * texScale);

    // DC - Part H - Specular components are added separate from texture color
    vec4 totalSpec = (attf1 * specular1) + specular2 + specular3;
    //gl_FragColor = gl_FragColor + totalSpec;
    gl_FragColor += totalSpec;
}
