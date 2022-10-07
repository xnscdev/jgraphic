#version 400

in vec2 pass_texture;

layout(location = 0) out vec4 out_color;

uniform vec3 color;
uniform sampler2D font_atlas;

void main(void) {
  out_color = vec4(color, texture(font_atlas, pass_texture).a);
}