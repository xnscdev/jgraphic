#version 400 core

layout(location = 0) in vec2 position;

out vec2 pass_texture;

uniform mat4 transform_matrix;

void main(void) {
  gl_Position = transform_matrix * vec4(position, 0.0, 1.0);
  pass_texture = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
}