#version 400 core

struct Fog
{
  float density;
  float gradient;
};

const int MAX_LIGHTS = 4;

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normal;

out vec2 pass_texture;
out vec3 surface_normal;
out vec3 light_vector[MAX_LIGHTS];
out vec3 camera_vector;
out float visibility;

uniform mat4 transform_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;
uniform vec3 light_pos[MAX_LIGHTS];
uniform float fake_lighting;
uniform Fog fog;

void main(void) {
  vec4 world_pos = transform_matrix * vec4(position, 1.0);
  vec4 camera_pos = view_matrix * world_pos;
  gl_Position = projection_matrix * camera_pos;
  pass_texture = texture;

  vec3 actual_normal = normal;
  if (fake_lighting > 0.5) {
    actual_normal = vec3(0.0, 1.0, 0.0);
  }
  surface_normal = (transform_matrix * vec4(actual_normal, 0.0)).xyz;
  for (int i = 0; i < MAX_LIGHTS; i++) {
    light_vector[i] = light_pos[i] - world_pos.xyz;
  }
  camera_vector = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - world_pos.xyz;

  float distance = length(camera_pos.xyz);
  visibility = clamp(exp(-pow(distance * fog.density, fog.gradient)), 0.0, 1.0);
}