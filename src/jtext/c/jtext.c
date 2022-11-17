#ifdef __APPLE__
#define GL_SILENCE_DEPRECATION 1
#include <OpenGL/gl.h>
#else
#include <GL/gl.h>
#endif
#include <pango/pangocairo.h>
#include <jni.h>

static unsigned int
gen_texture (int width, int height, unsigned char *pixels)
{
  unsigned int texture;
  glGenTextures (1, &texture);
  glBindTexture (GL_TEXTURE_2D, texture);
  glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
  glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  glTexImage2D (GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_BGRA,
		GL_UNSIGNED_BYTE, pixels);
  return texture;
}

static cairo_t *
gen_cairo_ctx (int width, int height, int channels, cairo_surface_t **surface,
	       unsigned char **buffer)
{
  *buffer = calloc (channels * width * height, 1);
  *surface = cairo_image_surface_create_for_data (*buffer, CAIRO_FORMAT_ARGB32,
						  width, height,
						  channels * width);
  return cairo_create (*surface);
}

static cairo_t *
gen_layout_ctx (void)
{
  cairo_surface_t *surface =
    cairo_image_surface_create (CAIRO_FORMAT_ARGB32, 0, 0);
  cairo_t *ctx = cairo_create (surface);
  cairo_surface_destroy (surface);
  return ctx;
}

static void
get_text_size (PangoLayout *layout, int *width, int *height)
{
  pango_layout_get_size (layout, width, height);
  *width /= PANGO_SCALE;
  *height /= PANGO_SCALE;
}

static int
gen_text (const char *text, const char *font, int size, int *width, int *height,
	  unsigned int *texture)
{
  cairo_t *layout_ctx;
  cairo_t *render_ctx;
  cairo_surface_t *surface;
  unsigned char *surface_data = NULL;
  PangoFontDescription *desc;
  PangoLayout *layout;
  char *desc_name;

  layout_ctx = gen_layout_ctx ();
  layout = pango_cairo_create_layout (layout_ctx);
  pango_layout_set_text (layout, text, -1);

  desc_name = g_strdup_printf ("%s %d", font, size > 60 ? size : 60);
  desc = pango_font_description_from_string (desc_name);
  g_free (desc_name);
  pango_layout_set_font_description (layout, desc);
  pango_font_description_free (desc);

  get_text_size (layout, width, height);
  render_ctx = gen_cairo_ctx (*width, *height, 4, &surface, &surface_data);
  cairo_set_source_rgba (render_ctx, 1, 1, 1, 1);
  pango_cairo_show_layout (render_ctx, layout);
  *texture = gen_texture (*width, *height, surface_data);

  free (surface_data);
  g_object_unref (layout);
  cairo_destroy (layout_ctx);
  cairo_destroy (render_ctx);
  cairo_surface_destroy (surface);
  return 0;
}

JNIEXPORT jobject JNICALL
Java_com_github_xnscdev_jgraphic_util_ObjectManager_loadText (JNIEnv *env,
							      jclass klass,
							      jstring text,
							      jint size,
							      jstring font)
{
  jclass ret_class =
    (*env)->FindClass (env, "com/github/xnscdev/jgraphic/render/TextureData");
  jmethodID cid;
  int texture;
  int width;
  int height;
  const char *text_chars;
  const char *font_chars;
  int ret;
  if (!ret_class)
    return NULL;
  cid = (*env)->GetMethodID (env, ret_class, "<init>", "(III)V");
  if (!cid)
    return NULL;
  text_chars = (*env)->GetStringUTFChars (env, text, 0);
  font_chars = (*env)->GetStringUTFChars (env, font, 0);
  ret = gen_text (text_chars, font_chars, size, &width, &height, &texture);
  (*env)->ReleaseStringUTFChars (env, text, text_chars);
  (*env)->ReleaseStringUTFChars (env, font, font_chars);
  return ret ? NULL :
    (*env)->NewObject (env, ret_class, cid, texture,
		       (int) ((float) width * size / height), size);
}
