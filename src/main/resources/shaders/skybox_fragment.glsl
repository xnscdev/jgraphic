#version 400 core

in vec3 pass_texture;

layout(location = 0) out vec4 out_color;

uniform samplerCube texture_sampler;

void main(void) {
  out_color = texture(texture_sampler, pass_texture);
}