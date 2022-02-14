// Shared between shaders
varying vec2 texCoord;
varying vec4 color;

// Application variables
attribute vec4 vPosition;
attribute vec3 vNormal;
attribute vec2 vTexCoord;
uniform vec4 AmbientProduct, DiffuseProduct, SpecularProduct;
uniform mat4 ModelView;
uniform vec4 LightPosition;
uniform mat4 Projection;
uniform float Shininess;

void main()
{
    // Ensure W = 1 in vertex position
    vec4 vpos;
    vpos.xyz = vPosition.xyz;
    vpos.w = 1.0;

    // Transform vertex position into eye coordinates
    vec3 pos = (ModelView * vpos).xyz;

    // The vector to the light from the vertex    
    vec3 Lvec = LightPosition.xyz - pos;

    // Unit direction vectors for Blinn-Phong shading calculation
    vec3 L = normalize( Lvec );  // Direction to the light source
    vec3 E = normalize( -pos );  // Direction to the eye/camera
    vec3 H = normalize( L + E ); // Halfway vector

    // Transform vertex normal into eye coordinates (assumes scaling
    // is uniform across dimensions)
    vec3 N = normalize( (ModelView * vec4(vNormal, 0.0)).xyz );

    // Compute terms in the illumination equation
    vec4 ambient = AmbientProduct;

    float Kd = max( dot(L, N), 0.0 );
    vec4  diffuse = Kd * DiffuseProduct;

    float Ks = pow( max(dot(N, H), 0.0), Shininess );
    vec4  specular = Ks * SpecularProduct;
    
    if (dot(L, N) < 0.0 ) {
    	specular = vec4(0.0, 0.0, 0.0, 1.0);
    } 

    // Get globalAmbient (independent of distance from the light source) 
    // DC - Reduced intensity
    vec4 globalAmbient = vec4(0.05, 0.05, 0.05, 1.0);

    // DC - Get distance between the light and vertex
    float dist = length(Lvec);

    // DC - Calculate the light attenuation factor
    float attf = 1.0 / (0.5 + (0.25 * dist) + (0.1 * pow(dist, 2.0)));

    // DC - Calculate color from light components and apply attentuation
    color.rgb = (globalAmbient + attf*(ambient + diffuse + specular)).rgb;

    // Set opacity
    color.a = 1.0;

    // Set position
    gl_Position = Projection * ModelView * vpos;

    // Give texCoord to fragment shader
    texCoord = vTexCoord;
}
