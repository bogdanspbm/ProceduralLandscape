package Main;

import Landscape.LowPolyOcean;
import Landscape.PelenNoise;
import Landscape.Skybox;
import Landscape.Terrain;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Game {

    public final Camera cam;
    private LowPolyOcean ocean = new LowPolyOcean();
    //private PelenNoise biom = new PelenNoise();
    private Terrain landscape = new Terrain();
    private Skybox skySphere = new Skybox();
    int i = 0;

    public Game() {
        cam = new Camera(20, 20, 20); // Cоздаем камеру с координатами x=0, y = 1, z = 5;
        //ocean.setGeneration(biom.getNoiseMat(), biom.getSize());

    }

    public void update(float delta) {
        cam.Update(delta); // Вызываю в камере обновление кадров, чтобы посчитать поворот + перемещение
    }

    public void render() {

        GL11.glRotatef(cam.getRotation().y, 1, 0, 0); // Выставляю поворот камеры по вертикали
        GL11.glRotatef(cam.getRotation().x, 0, 1, 0); // Выставляю повоторо камеры по горизонтали
        glTranslatef(-cam.getPos().x, -cam.getPos().y, -cam.getPos().z); // Ставлю координаты в пространтсве

        if (Keyboard.isKeyDown(Keyboard.KEY_R)) { // Обновляю шум
            //biom.refresh();
           // ocean.setGeneration(biom.getNoiseMat(), biom.getSize());

        }

        landscape.draw();
        skySphere.draw();

    }
}
