package org.xnsc.jworld.render.shader;

public class StaticShader extends Shader {
    public StaticShader() {
        super("static");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
}
