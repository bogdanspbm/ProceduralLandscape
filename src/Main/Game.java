package Main;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Game {

    private Camera cam;

    public Game() {
        cam = new Camera(0, 1, 5); // Cоздаем камеру с координатами x=0, y = 1, z = 5;
        // Хз почему, но у нас походу плоскость земли в y,z мне лень фиксить
    }

    public void update(float delta) {
            cam.Update(delta); // Вызываю в камере обновление кадров, чтобы посчитать поворот + перемещение
    }

    public void render() {

        GL11.glRotatef(cam.getRotation().y, 1, 0, 0); // Выставляю поворот камеры по вертикали
        GL11.glRotatef(cam.getRotation().x, 0, 1, 0); // Выставляю повоторо камеры по горизонтали
        glTranslatef(-cam.getPos().x, -cam.getPos().y, -cam.getPos().z); // Ставлю координаты в пространтсве

        //Есть разные режимы GL_TRIANGLES будет основным
        //По сути это главная тема которой мы занимаемся это генерируем вертексы и рисуем полигон
        //Вроде не сложно так что залетай
        glBegin(GL_QUADS); // Тут типо рисовать 
        glColor3f(1, 1, 1);
        glVertex3f(0, 0, 0);
        glVertex3f(1, 0, 0);
        glVertex3f(1, 0, 1);
        glVertex3f(0, 0, 1);

        glVertex3f(0, 2, 1);
        glVertex3f(1, 2, 1);
        glVertex3f(1, 2, 0);
        glVertex3f(0, 2, 0);

        glColor3f(-1, -1, -1);
        glVertex3f(0, 0, 0);
        glVertex3f(-1, 0, 0);
        glVertex3f(-1, 0, -1);
        glVertex3f(0, 0, -1);

        glVertex3f(0, 2, -1);
        glVertex3f(-1, 2, -1);
        glVertex3f(-1, 2, 0);
        glVertex3f(0, 2, 0);
        glEnd();
    }

}
