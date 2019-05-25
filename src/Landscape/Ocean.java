package Landscape;

import Actors.VBOModel;

public class Ocean {

    private float[] vertices, color;
    private PelenNoise noise;
    private VBOModel oceanModel;

    public Ocean() {
        noise = new PelenNoise(2, 100, 0.5f);
        vertices = noise.getVerticesVector();
        color = new float[vertices.length / 3 * 4];
        generateColor();
        oceanModel = new VBOModel(vertices, color);
    }

    public Ocean(int scaler, int size, float height) {
        noise = new PelenNoise(scaler, size, height);
        vertices = noise.getVerticesVector();
        color = new float[vertices.length / 3 * 4];
        generateColor();
        oceanModel = new VBOModel(vertices, color);
    }

    private void generateColor() {
        for (int i = 0; i < color.length; i++) {
            color[i] = 0.3f;
        }
    }

    public void draw() {
        oceanModel.renderColored();
    }

}
