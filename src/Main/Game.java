package Main;

import Actors.StaticMesh;
import Actors.VBOModel;
import Landscape.LowPolyOcean;
import Landscape.LowPolyTerrain;
import Landscape.PelenNoise;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glTranslatef;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Game {

    public final Camera cam;
    private String[] skyboxModel = {"res/models/sky.obj"};
    private StaticMesh skySphere = new StaticMesh(skyboxModel, "res/textures/T_Skybox_No_Snow_Diff.tga"); // Загрузка модели
    private LowPolyTerrain landscape = new LowPolyTerrain();
    private LowPolyOcean ocean = new LowPolyOcean();
    private PelenNoise noise = new PelenNoise();
    private PelenNoise biom = new PelenNoise();
    private Texture tex;
    int i = 0;

    private float[] vertices;
    private float[] textures;

    VBOModel model;

    public Game() throws FileNotFoundException, IOException {
        cam = new Camera(20, 20, 20); // Cоздаем камеру с координатами x=0, y = 1, z = 5;
        skySphere.addCopy(0, 200, 0);
        skySphere.setScale(2f);
        landscape.setCamera(cam);
        landscape.setGenerations(noise.getNoiseMat(), biom.getNoiseMat(), noise.getSize());
        ocean.setGeneration(biom.getNoiseMat(), biom.getSize());
        landscape.refresh();

        tex = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/T_Skybox_No_Snow_Diff.tga")));
        
        vertices = noise.getVerticesVector();
        textures = noise.getTextureVector();
        model = new VBOModel(vertices, textures);

    }

    public void update(float delta) {
        cam.Update(delta); // Вызываю в камере обновление кадров, чтобы посчитать поворот + перемещение
    }

    public void render() {

        GL11.glRotatef(cam.getRotation().y, 1, 0, 0); // Выставляю поворот камеры по вертикали
        GL11.glRotatef(cam.getRotation().x, 0, 1, 0); // Выставляю повоторо камеры по горизонтали
        glTranslatef(-cam.getPos().x, -cam.getPos().y, -cam.getPos().z); // Ставлю координаты в пространтсве

        if (Keyboard.isKeyDown(Keyboard.KEY_R)) { // Обновляю шум
            noise.refresh();
            biom.refresh();
            landscape.setGenerations(noise.getNoiseMat(), biom.getNoiseMat(), noise.getSize());
            ocean.setGeneration(biom.getNoiseMat(), biom.getSize());
            landscape.refresh();
            vertices = noise.getVerticesVector();
        }

        tex.bind();
        model.render();
        skySphere.drawModel();
        //landscape.matrixToLandscape();
        //ocean.drawOcean(noise.getNoiseMat());
    }
}
