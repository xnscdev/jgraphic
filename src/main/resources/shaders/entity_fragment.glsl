#version 400 core

in vec2 pass_texture;
in vec3 surface_normal;
in vec3 light_vector[4];
in vec3 camera_vector;
in float visibility;

layout(location = 0) out vec4 out_color;

uniform sampler2D texture_sampler;
uniform vec3 light_color[4];
uniform float reflectivity;
uniform float shine_damper;
uniform vec3 sky_color;
uniform float ambient_threshold;

void main(void) {
  vec3 unit_normal = normalize(surface_normal);
  vec3 unit_camera = normalize(camera_vector);
  vec3 total_diffuse = vec3(0.0);
  vec3 total_specular = vec3(0.0);

  for (int i = 0; i < 4; i++) {
    vec3 unit_light = normalize(light_vector[i]);
    float brightness = max(dot(unit_normal, unit_light), 0.0);
    vec3 light_dir = reflect(-unit_light, unit_normal);
    float specular_factor = max(dot(light_dir, unit_camera), 0.0);
    total_diffuse = total_diffuse + brightness * light_color[i];
    total_specular = total_specular + pow(specular_factor, shine_damper) * reflectivity * light_color[i];
  }
  total_diffuse = max(total_diffuse, ambient_threshold);

  vec4 texture_color = texture(texture_sampler, pass_texture);
  if (texture_color.a < 0.5) {
    discard;
  }
  out_color = vec4(total_diffuse, 1.0) * texture_color + vec4(total_specular, 1.0);
  out_color = mix(vec4(sky_color, 1.0), out_color, visibility);
}