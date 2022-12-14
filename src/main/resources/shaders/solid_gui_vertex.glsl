#version 400 core

layout(location = 0) in vec2 position;

uniform mat4 transform_matrix;

void main(void) {
  gl_Position = transform_matrix * vec4(position, 0.0, 1.0);
}