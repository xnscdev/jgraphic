#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;

out vec2 pass_texture;

uniform mat4 transform_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

void main(void) {
  gl_Position = projection_matrix * view_matrix * transform_matrix * vec4(position, 1.0);
  pass_texture = texture;
}