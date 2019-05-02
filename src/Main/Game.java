package Main;

import Actors.Actor;
import Landscape.Terrain;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class Game {

    public final Camera cam;
    private final Terrain myTerrain;
    private Actor demoter = new Actor("res/models/landscape.obj"); // Загрузка модели
    int i = 0;

    public Game() {
        cam = new Camera(0, 1, 5); // Cоздаем камеру с координатами x=0, y = 1, z = 5;
        // Хз почему, но у нас походу плоскость земли в y,z мне лень фиксить
        myTerrain = new Terrain(3, 3);
    }

    public void update(float delta) {
        cam.Update(delta); // Вызываю в камере обновление кадров, чтобы посчитать поворот + перемещение
    }

    public void render() {

        GL11.glRotatef(cam.getRotation().y, 1, 0, 0); // Выставляю поворот камеры по вертикали
        GL11.glRotatef(cam.getRotation().x, 0, 1, 0); // Выставляю повоторо камеры по горизонтали
        glTranslatef(-cam.getPos().x, -cam.getPos().y, -cam.getPos().z); // Ставлю координаты в пространтсве
        //demoter.drawModel(); // Отрисовка модели ( Загрузка выше ) (См класс Actor)
        myTerrain.drawTerrain();
    }
}
