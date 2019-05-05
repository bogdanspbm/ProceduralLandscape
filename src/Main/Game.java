package Main;

import Actors.Actor;
import Landscape.PelenNoise;
import Landscape.Sea;
import Landscape.SkyBox;
import Landscape.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Game {

    public final Camera cam;
    //private final Terrain myTerrain;
    private Actor demoter = new Actor("res/models/sky.obj", "res/textures/T_Skybox_No_Snow_Diff.tga"); // Загрузка модели
    private PelenNoise terrain = new PelenNoise(100);
    //private Terrain terrain = new Terrain();
    int i = 0;

    public Game() {
        cam = new Camera(20, 20, 20); // Cоздаем камеру с координатами x=0, y = 1, z = 5;
        // Хз почему, но у нас походу плоскость земли в y,z мне лень фиксить
        //myTerrain = new Terrain(3, 3);
    }

    public void update(float delta) {
        cam.Update(delta); // Вызываю в камере обновление кадров, чтобы посчитать поворот + перемещение
    }

    public void render() {

        GL11.glRotatef(cam.getRotation().y, 1, 0, 0); // Выставляю поворот камеры по вертикали
        GL11.glRotatef(cam.getRotation().x, 0, 1, 0); // Выставляю повоторо камеры по горизонтали
        glTranslatef(-cam.getPos().x, -cam.getPos().y, -cam.getPos().z); // Ставлю координаты в пространтсве
        //demoter.drawModel(); // Отрисовка модели ( Загрузка выше ) (См класс Actor)
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            terrain.refresh();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            //terrain.changeVarToChange(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            //terrain.changeVarToChange(2);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
            //terrain.changeVarToChange(3);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            //terrain.changeVar(0.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            //terrain.changeVar(-0.1f);
        }

        demoter.drawModel();
        terrain.drawTerrain();
    }
}
