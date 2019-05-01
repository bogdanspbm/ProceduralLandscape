package Landscape;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;

public class Terrain {

    private final List<Vector3f> vertices = new ArrayList<Vector3f>();

    public Terrain(int newSize, int height) {
        //Начинаешь реализацию алгорита
    }

    public void generateSquare() {
        //Рекурентная функция 

    }

    private void fillCell(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        // Сюда подаешь координаты клетки по часовой стрелке, для отрисовки в будущем
        glBegin(GL_TRIANGLES);
        glColor3f(0.2f, 0.8f, 0.2f);

        glVertex3f(d.x, d.y, d.z);
        glVertex3f(c.x, c.y, c.z);
        glVertex3f(b.x, b.y, b.z);

        glVertex3f(a.x, a.y, a.z);
        glVertex3f(d.x, d.y, d.z);
        glVertex3f(b.x, b.y, b.z);
        glEnd();
    }

    public void drawTerrain() {
        // Эта функция отрисует все твои клетки
        glBegin(GL_TRIANGLES);
        glColor3f(0.2f, 0.8f, 0.2f);
        for (int i = 0; i < vertices.size() / 3; i += 3) {
            glVertex3f(vertices.get(i).x, vertices.get(i).y, vertices.get(i).z);
            glVertex3f(vertices.get(i + 1).x, vertices.get(i + 1).y, vertices.get(i + 1).z);
            glVertex3f(vertices.get(i + 2).x, vertices.get(i + 2).y, vertices.get(i + 2).z);
        }
        glEnd();
    }
}
