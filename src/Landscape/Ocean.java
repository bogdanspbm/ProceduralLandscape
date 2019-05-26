package Landscape;

import Actors.VBOModel;

public class Ocean {

    private float[] vertices, color;
    private PelenNoise noise, biom;
    private VBOModel oceanModel;

    public Ocean() {
        noise = new PelenNoise(2, 100, 0.5f);
        vertices = noise.getVerticesVector();
        color = new float[vertices.length / 3 * 4];
        generateColor();
        oceanModel = new VBOModel(vertices, color);
    }

    public Ocean(float scaler, int size, float height, PelenNoise biom) {
        noise = new PelenNoise(scaler, size, height);
        this.biom = biom;
        vertices = noise.getVerticesVector();
        color = new float[vertices.length / 3 * 4];
        generateColor();
        oceanModel = new VBOModel(vertices, color);
    }

    private void generateColor() {
        for (int i = 0; i < color.length; i += 4) {
            if (biom.getVerticesVector()[i / 4 * 3 + 1] < 0.5f) {
                color[i] = 0.3f;
                color[i + 1] = 0.3f;
                color[i + 2] = 0.3f;
                color[i + 3] = 0.3f;
            } else {
                color[i] = 0.9f;
                color[i + 1] = 0.9f;
                color[i + 2] = 0.9f;
                color[i + 3] = 0.9f;
            }
        }
    }

    public void draw() {
        oceanModel.renderColored();
    }

}
