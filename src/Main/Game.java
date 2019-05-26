package Main;

import Landscape.LowPolyOcean;
import Landscape.Ocean;
import Landscape.PelenNoise;
import Landscape.Skybox;
import Landscape.Terrain;
import Shaders.Shader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Game {

    public final Camera cam;
    private int size = 200;
    private float scaler = 2;
    private PelenNoise biom = new PelenNoise(scaler, size, 2f);
    private Terrain landscape = new Terrain(scaler, size, 4f, biom);
    private Skybox skySphere = new Skybox();
    private Ocean ocean = new Ocean(scaler, size, 0.5f, biom);
    private Shader shader = new Shader("shader");
    int i = 0;

    public Game() {
        cam = new Camera(20, 20, 20); // Cоздаем камеру с координатами x=0, y = 1, z = 5;

    }

    public void update(float delta) {
        cam.Update(delta); // Вызываю в камере обновление кадров, чтобы посчитать поворот + перемещение
    }

    public void render() {

        GL11.glRotatef(cam.getRotation().y, 1, 0, 0); // Выставляю поворот камеры по вертикали
        GL11.glRotatef(cam.getRotation().x, 0, 1, 0); // Выставляю повоторо камеры по горизонтали
        glTranslatef(-cam.getPos().x, -cam.getPos().y, -cam.getPos().z); // Ставлю координаты в пространтсве

        if (Keyboard.isKeyDown(Keyboard.KEY_R)) { // Обновляю шум
            biom.refresh();
            landscape.refresh();
        }
        
        skySphere.draw();
        landscape.draw();
        ocean.draw();   
    }
}
