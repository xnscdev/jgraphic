#version 400 core

in vec2 pass_texture;

layout(location = 0) out vec4 out_color;

uniform sampler2D textureSampler;

void main(void) {
  out_color = texture(textureSampler, pass_texture);
}