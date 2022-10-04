#version 400 core

layout(location = 0) in vec3 position;

out vec3 pass_texture;

uniform mat4 projection_matrix;
uniform mat4 view_matrix;

void main(void) {
  gl_Position = projection_matrix * view_matrix * vec4(position, 1.0);
  pass_texture = position;
}