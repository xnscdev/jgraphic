#version 400 core

struct PointLight
{
  vec3 color;
  vec3 attenuation;
};

struct Material
{
  float use_texture;
  vec4 color;
};

const int MAX_LIGHTS = 4;

in vec2 pass_texture;
in vec3 surface_normal;
in vec3 light_vector[MAX_LIGHTS];
in vec3 camera_vector;
in float visibility;

layout(location = 0) out vec4 out_color;

uniform sampler2D texture_sampler;
uniform PointLight lights[MAX_LIGHTS];
uniform float reflectivity;
uniform vec3 sky_color;
uniform float ambient_threshold;
uniform Material material;

void main(void) {
  vec3 unit_normal = normalize(surface_normal);
  vec3 unit_camera = normalize(camera_vector);
  vec3 total_diffuse = vec3(0.0);
  vec3 total_specular = vec3(0.0);

  vec4 real_color = material.color;
  if (material.use_texture > 0.5) {
    real_color = texture(texture_sampler, pass_texture);
  }
  if (real_color.a < 0.5) {
    discard;
  }

  for (int i = 0; i < MAX_LIGHTS; i++) {
    float distance = length(light_vector[i]);
    float attenuation_factor = lights[i].attenuation.x + lights[i].attenuation.y * distance + lights[i].attenuation.z * distance * distance;
    vec3 unit_light = normalize(light_vector[i]);
    float brightness = max(dot(unit_normal, unit_light), 0.0);
    vec3 light_dir = reflect(-unit_light, unit_normal);
    float specular_factor = max(dot(light_dir, unit_camera), 0.0);
    total_diffuse = total_diffuse + brightness * lights[i].color / attenuation_factor;
    total_specular = total_specular + specular_factor * reflectivity * lights[i].color / attenuation_factor;
  }
  total_diffuse = max(total_diffuse, ambient_threshold);

  out_color = vec4(total_diffuse, 1.0) * real_color + vec4(total_specular, 1.0);
  out_color = mix(vec4(sky_color, 1.0), out_color, visibility);
}