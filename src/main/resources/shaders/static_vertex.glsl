#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normal;

out vec2 pass_texture;
out vec3 surface_normal;
out vec3 light_vector;
out vec3 camera_vector;

uniform mat4 transform_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;
uniform vec3 light_pos;

void main(void) {
  vec4 world_pos = transform_matrix * vec4(position, 1.0);
  gl_Position = projection_matrix * view_matrix * world_pos;
  pass_texture = texture;

  surface_normal = (transform_matrix * vec4(normal, 0.0)).xyz;
  light_vector = light_pos - world_pos.xyz;
  camera_vector = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - world_pos.xyz;
}