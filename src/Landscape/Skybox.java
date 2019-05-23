package Landscape;

import Actors.StaticMesh;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Skybox {

    private String skyboxModel = "res/models/sky.obj";
    private Texture texture;
    private Vector3f location = new Vector3f(0f, 200f, 0f);
    private StaticMesh skySphere = new StaticMesh(skyboxModel, location); // Загрузка модели

    public Skybox() {
        try {
            texture = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/T_Skybox_No_Snow_Diff.tga")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw() {
        texture.bind();
        skySphere.drawVBO();
    }

}
