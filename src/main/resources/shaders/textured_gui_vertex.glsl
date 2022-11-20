#version 400 core

layout(location = 0) in vec2 position;

out vec2 pass_texture;

uniform mat4 transform_matrix;
uniform vec2 texture_pos;
uniform vec2 texture_size;

void main(void) {
  gl_Position = transform_matrix * vec4(position, 0.0, 1.0);
  pass_texture = vec2(texture_pos.x + position.x * texture_size.x, 1 - texture_pos.y - position.y * texture_size.y);
}