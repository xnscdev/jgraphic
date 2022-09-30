package org.xnsc.jworld.render.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {
    private final int program;
    private final int vertex;
    private final int fragment;

    public Shader(String name) {
        vertex = loadShader("/shaders/" + name + "_vertex.glsl", GL_VERTEX_SHADER);
        fragment = loadShader("/shaders/" + name + "_fragment.glsl", GL_FRAGMENT_SHADER);
        program = glCreateProgram();
        glAttachShader(program, vertex);
        glAttachShader(program, fragment);
        glLinkProgram(program);
        glValidateProgram(program);
        //bindAttributes();
    }

    protected abstract void bindAttributes();

    public void start() {
        glUseProgram(program);
    }

    public void clean() {
        stop();
        glDetachShader(program, vertex);
        glDetachShader(program, fragment);
        glDeleteShader(vertex);
        glDeleteShader(fragment);
        glDeleteProgram(program);
    }

    protected void bindAttribute(int attr, String name) {
        glBindAttribLocation(program, attr, name);
    }

    public static void stop() {
        glUseProgram(0);
    }

    private static int loadShader(String path, int type) {
        StringBuilder source = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Shader.class.getResourceAsStream(path))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("//\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);
        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw new IllegalStateException("Failed to compile shader: " + glGetShaderInfoLog(id));
        return id;
    }
}
