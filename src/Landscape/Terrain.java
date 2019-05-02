package Landscape;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;

public class Terrain {

    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    private final int cellCount = 100;
    private Vector3f[][] verticesMatrix = new Vector3f[cellCount][cellCount];

    public Terrain(int newSize, int height) {
        //Начинаешь реализацию алгорита
        fillZerosVerticesMatrix();
        fillVericexArray();
    }

    private void fillZerosVerticesMatrix() {
        for (int i = 0; i < cellCount; i++) {
            for (int k = 0; k < cellCount; k++) {
                verticesMatrix[i][k] = new Vector3f(i, (float) (Math.random() * 1), k);
            }
        }
    }

    private void fillVericexArray() {
        for (int i = 0; i < cellCount - 1; i++) {
            for (int k = 0; k < cellCount - 1; k++) {
                fillCell(verticesMatrix[i][k], verticesMatrix[i + 1][k], verticesMatrix[i + 1][k + 1], verticesMatrix[i][k + 1]);
            }
        }
    }

    public void generateSquare() {
        //Рекурентная функция 

    }

    private void fillCell(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        // Сюда подаешь координаты клетки по часовой стрелке, для отрисовки в будущем
        //glBegin(GL_TRIANGLES);
        //glColor3f(0.2f, 0.8f, 0.2f);

        vertices.add(d);
        vertices.add(c);
        vertices.add(b);

        vertices.add(a);
        vertices.add(d);
        vertices.add(b);

    }

    public void calcNormal(Vector3f s1, Vector3f s2, Vector3f s3) {
        Vector3f a = new Vector3f(s2.x - s1.x, s2.y - s1.y, s2.z - s1.z);
        Vector3f b = new Vector3f(s3.x - s2.x, s3.y - s2.y, s3.z - s2.z);
        Vector3f normal = new Vector3f(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
        glNormal3f(normal.x, normal.y, normal.z);
    }

    public void drawTerrain() {
        // Эта функция отрисует все твои клетки
        glBegin(GL_TRIANGLES);
        glColor3f(0.2f, 0.8f, 0.2f);
        for (int i = 0; i < vertices.size() / 3; i += 3) {
            calcNormal(vertices.get(i + 0), vertices.get(i + 1),vertices.get(i + 2));
            glVertex3f(vertices.get(i + 0).x, vertices.get(i + 0).y, vertices.get(i + 0).z);
            glVertex3f(vertices.get(i + 1).x, vertices.get(i + 1).y, vertices.get(i + 1).z);
            glVertex3f(vertices.get(i + 2).x, vertices.get(i + 2).y, vertices.get(i + 2).z);
        }
        glEnd();
    }
}
