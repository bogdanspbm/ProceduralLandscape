package Main;

import Actors.StaticMesh;
import Landscape.LowPolyOcean;
import Landscape.LowPolyTerrain;
import Landscape.PelenNoise;
import Utils3D.Draw3D;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Game {

    public final Camera cam;
    private StaticMesh skySphere = new StaticMesh("res/models/sky.obj", "res/textures/T_Skybox_No_Snow_Diff.tga"); // Загрузка модели
    private LowPolyTerrain landscape = new LowPolyTerrain();
    private LowPolyOcean ocean = new LowPolyOcean();
    private PelenNoise noise = new PelenNoise();
    int i = 0;

    public Game() {
        cam = new Camera(20, 20, 20); // Cоздаем камеру с координатами x=0, y = 1, z = 5;
        skySphere.addCopy(0, 200, 0);
        skySphere.setScale(2f);

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
        }

        skySphere.drawModel();
        landscape.matrixToLandscape(noise.getNoiseMat(), noise.getSize());
        ocean.drawOcean(noise.getNoiseMat());
    }
}
