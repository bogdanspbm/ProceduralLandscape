package Landscape;

import Actors.VBOModel;
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

    private PelenNoise noise = new PelenNoise();
    private VBOModel terrainModel;
    private Texture texture;

    public Terrain() {
        terrainModel = new VBOModel(noise.getVerticesVector(), makeTexturesVector());
        try {
            texture = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/1024Grass.tga")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private float[] makeTexturesVector() {
        float[] vertices = noise.getVerticesVector();
        float[] textures = new float[vertices.length / 3 * 2];
        int flag = 0;
        float height;
        int switcher;

        for (int i = 0; i < vertices.length; i += 3) {
            height = vertices[i + 1];
            if (height < 0.5) {
                textures[flag] = vertices[i] / 10;
                textures[flag + 1] = vertices[i + 2] / 10;
            } else {
                textures[flag] = vertices[i] / 10;
                textures[flag + 1] = vertices[i + 2] / 10;
            }
            // Selection logic
            flag += 2;
        }
        return textures;
    }

    public void draw() {
        texture.bind();
        terrainModel.render();
    }

}
