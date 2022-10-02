#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normal;

out vec2 pass_texture;
out vec3 surface_normal;
out vec3 light_vector;
out vec3 camera_vector;
out float visibility;

uniform mat4 transform_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;
uniform vec3 light_pos;
uniform float fog_density;
uniform float fog_gradient;
uniform float texture_scale;

void main(void) {
  vec4 world_pos = transform_matrix * vec4(position, 1.0);
  vec4 camera_pos = view_matrix * world_pos;
  gl_Position = projection_matrix * camera_pos;
  pass_texture = texture * texture_scale;

  surface_normal = (transform_matrix * vec4(normal, 0.0)).xyz;
  light_vector = light_pos - world_pos.xyz;
  camera_vector = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - world_pos.xyz;

  float distance = length(camera_pos.xyz);
  visibility = clamp(exp(-pow(distance * fog_density, fog_gradient)), 0.0, 1.0);
}