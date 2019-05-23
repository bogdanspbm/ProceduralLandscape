package Landscape;

import Actors.VBOModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Terrain {

    private PelenNoise noise = new PelenNoise();
    private VBOModel terrainModel;
    private Texture texture;

    public Terrain() {
        terrainModel = new VBOModel(noise.getVerticesVector(), noise.getTextureVector());
        try {
            texture = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/PolygonAdventure_Tex_01.tga")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw() {
        texture.bind();
        terrainModel.render();
    }

}
