package org.xnsc.jgraphic.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {
    private static final FloatBuffer bufferMatrix4f = BufferUtils.createFloatBuffer(16);
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
        getUniforms();
    }

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

    protected abstract void getUniforms();

    protected int getUniform(String name) {
        return glGetUniformLocation(program, name);
    }

    protected void loadFloat(int loc, float value) {
        glUniform1f(loc, value);
    }

    protected void loadInt(int loc, int value) {
        glUniform1i(loc, value);
    }

    protected void loadBoolean(int loc, boolean value) {
        glUniform1f(loc, value ? 1 : 0);
    }

    protected void loadVector3f(int loc, Vector3f value) {
        glUniform3f(loc, value.x, value.y, value.z);
    }

    protected void loadMatrix4f(int loc, Matrix4f value) {
        glUniformMatrix4fv(loc, false, value.get(bufferMatrix4f));
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
