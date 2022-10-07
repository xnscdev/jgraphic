#version 400

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texture;

out vec2 pass_texture;

uniform vec2 translation;

void main(void) {
  gl_Position = vec4(position + translation, 0.0, 1.0);
  pass_texture = texture;
}