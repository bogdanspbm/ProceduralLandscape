package Main;

import Actors.Actor;
import Actors.Model;
import Actors.OBJLoader;
import Landscape.Terrain;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.opengl.Display;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;

import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;

public class Game {

    private Camera cam;
    private Terrain myTerrain;
    private Actor demoter = new Actor("res/models/landscape.obj"); // Загрузка модели

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
        demoter.drawModel(); // Отрисовка модели ( Загрузка выше ) (См класс Actor)
    }
}
