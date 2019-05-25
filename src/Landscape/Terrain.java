package Landscape;

import Actors.VBOModel;
import Utils3D.Stereometry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Terrain {

    private PelenNoise noise;
    private VBOModel terrainModel;
    private Texture texture;
    float[] vertices, textures;

    public Terrain() {
        noise = new PelenNoise(2, 100);
        vertices = noise.getVerticesVector();
        makeTexturesVector();
        terrainModel = new VBOModel(vertices, textures);
        try {
            texture = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/TerrainTexture.tga")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        noise.refresh();
        vertices = noise.getVerticesVector();
        makeTexturesVector();
        terrainModel = new VBOModel(vertices, textures);
    }

    public Terrain(int scaler, int size, float height) {
        noise = new PelenNoise(scaler, size, height);
        vertices = noise.getVerticesVector();
        makeTexturesVector();
        terrainModel = new VBOModel(vertices, textures);
        try {
            texture = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/TerrainTexture.tga")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private float[] makeTexturesVector() {
        textures = new float[vertices.length / 3 * 2];
        int flag = 0;
        float height, degree;
        int switcher;

        for (int i = 0; i < vertices.length; i += 3) {
            height = vertices[i + 1];
            degree = Stereometry.getVectorWorldDegree(getNormalByIndex(i));
            if (height < 0.5) {
                textures[flag] = Math.abs(vertices[i] / 5) % 0.5f;
                textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f + 0.5f;

            } else {
                if (degree < 0.7f) {
                    textures[flag] = Math.abs(vertices[i] / 5) % 0.5f;
                    textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f;
                } else {
                    textures[flag] = Math.abs(vertices[i] / 5) % 0.5f + 0.5f;
                    textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f + 0.5f;
                }
            }
            flag += 2;
        }
        // Selection logic

        return textures;
    }

    private Vector3f getNormalByIndex(int index) {
        int startIndex = index / 9;
        startIndex *= 9;
        Vector3f a = new Vector3f(vertices[startIndex], vertices[startIndex + 1], vertices[startIndex + 2]
        ),
                b = new Vector3f(vertices[startIndex + 3], vertices[startIndex + 4], vertices[startIndex + 5]
                ),
                c = new Vector3f(vertices[startIndex + 6], vertices[startIndex + 7], vertices[startIndex + 8]);
        Vector3f normal = Stereometry.calcNormal(a, b, c);
        return normal;
    }

    public void draw() {
        texture.bind();
        terrainModel.renderTextured();
    }

}
