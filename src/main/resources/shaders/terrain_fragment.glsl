#version 400 core

in vec2 pass_texture;
in vec3 surface_normal;
in vec3 light_vector[4];
in vec3 camera_vector;
in float visibility;

layout(location = 0) out vec4 out_color;

uniform sampler2D background_sampler;
uniform sampler2D red_sampler;
uniform sampler2D green_sampler;
uniform sampler2D blue_sampler;
uniform sampler2D blend_map_sampler;
uniform vec3 light_color[4];
uniform vec3 attenuation[4];
uniform float reflectivity;
uniform float shine_damper;
uniform vec3 sky_color;
uniform float ambient_threshold;
uniform float texture_scale;

void main(void) {
  vec4 blend_map_color = texture(blend_map_sampler, pass_texture);
  float back_texture_amount = 1 - (blend_map_color.r + blend_map_color.g + blend_map_color.b);
  vec2 tiled_texture = pass_texture * texture_scale;
  vec4 background_texture = texture(background_sampler, tiled_texture) * back_texture_amount;
  vec4 red_texture = texture(red_sampler, tiled_texture) * blend_map_color.r;
  vec4 green_texture = texture(green_sampler, tiled_texture) * blend_map_color.g;
  vec4 blue_texture = texture(blue_sampler, tiled_texture) * blend_map_color.b;
  vec4 total_color = background_texture + red_texture + green_texture + blue_texture;

  vec3 unit_normal = normalize(surface_normal);
  vec3 unit_camera = normalize(camera_vector);
  vec3 total_diffuse = vec3(0.0);
  vec3 total_specular = vec3(0.0);

  for (int i = 0; i < 4; i++) {
    float distance = length(light_vector[i]);
    float attenuation_factor = attenuation[i].x + attenuation[i].y * distance + attenuation[i].z * distance * distance;
    vec3 unit_light = normalize(light_vector[i]);
    float brightness = max(dot(unit_normal, unit_light), 0.0);
    vec3 light_dir = reflect(-unit_light, unit_normal);
    float specular_factor = max(dot(light_dir, unit_camera), 0.0);
    total_diffuse = total_diffuse + brightness * light_color[i] / attenuation_factor;
    total_specular = total_specular + pow(specular_factor, shine_damper) * reflectivity * light_color[i] / attenuation_factor;
  }
  total_diffuse = max(total_diffuse, ambient_threshold);

  out_color = vec4(total_diffuse, 1.0) * total_color + vec4(total_specular, 1.0);
  out_color = mix(vec4(sky_color, 1.0), out_color, visibility);
}