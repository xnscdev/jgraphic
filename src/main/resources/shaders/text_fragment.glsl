#version 400

in vec2 pass_texture;

layout(location = 0) out vec4 out_color;

uniform vec3 color;
uniform float width;
uniform float edge;
uniform vec3 border_color;
uniform vec2 border_offset;
uniform float border_width;
uniform float border_edge;
uniform sampler2D font_atlas;

void main(void) {
  float distance = 1.0 - texture(font_atlas, pass_texture).a;
  float alpha = 1.0 - smoothstep(width, width + edge, distance);
  float border_distance = 1.0 - texture(font_atlas, pass_texture + border_offset).a;
  float border_alpha = 1.0 - smoothstep(border_width, border_width + border_edge, border_distance);
  float final_alpha = alpha + (1.0 - alpha) * border_alpha;
  vec3 final_color = mix(border_color, color, alpha / final_alpha);
  out_color = vec4(final_color, final_alpha);
}