#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;

out vec3 color;
out vec2 pass_texture;

void main(void) {
  gl_Position = vec4(position, 1.0);
  color = vec3(position.x + 0.5, 1.0, position.y + 0.5);
  pass_texture = texture;
}