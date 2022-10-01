#version 400 core

in vec2 pass_texture;
in vec3 surface_normal;
in vec3 light_vector;
in vec3 camera_vector;

layout(location = 0) out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 light_color;
uniform float reflectivity;
uniform float shine_damper;

void main(void) {
  vec3 unit_normal = normalize(surface_normal);
  vec3 unit_light = normalize(light_vector);
  vec3 unit_camera = normalize(camera_vector);
  float brightness = max(dot(unit_normal, unit_light), 0.0);
  vec3 diffuse = brightness * light_color;
  vec3 light_dir = reflect(-unit_light, unit_normal);
  float specular_factor = max(dot(light_dir, unit_camera), 0.0);
  vec3 specular_color = pow(specular_factor, shine_damper) * reflectivity * light_color;
  out_color = vec4(diffuse, 1.0) * texture(textureSampler, pass_texture) + vec4(specular_color, 1.0);
}