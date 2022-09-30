#version 400 core

in vec3 color;
in vec2 pass_texture;

layout(location = 0) out vec4 out_color;

uniform sampler2D textureSampler;

void main(void) {
  out_color = vec4(color, 1.0) * texture(textureSampler, pass_texture);
}