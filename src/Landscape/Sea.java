package Landscape;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;

public class Sea {

    private int cellCount = 32;
    private float scaler = 4;
    private float maxHeight = 0.4f;
    private List<Vector3f> vertices = new ArrayList<>();
    private List<Float> verticesStartZ = new ArrayList<>();
    private Vector3f[][] verticesMatrix;
    private float dtime = 0;
    public float seaLevel = 0f;

    public Sea(int size, float scaler) {
        this.cellCount = size;
        this.scaler = scaler;
        calcCellCount();
        fillZerosVerticesMatrix();
        fillVericexArray();

    }

    private void fillZerosVerticesMatrix() {
        for (int i = 0; i < cellCount; i++) {
            for (int k = 0; k < cellCount; k++) {
                verticesMatrix[i][k] = new Vector3f((float) (i - cellCount / 2) / scaler, (float) (Math.sin(Math.random() * 7)), (float) (k - cellCount / 2) / scaler);
                // i и k в данном случае координаты 
                // по идеи можно поменять систему счисления тем самым уменьшив размер поля и уменьшить полигоны
                // как вариант поделив на const
            }
        }
    }

    private void calcCellCount() {
        int k = 2;
        while (k < cellCount) {
            k += k - 1;
        }
        cellCount = k;
        verticesMatrix = new Vector3f[cellCount][cellCount];
    }

    private void fillVericexArray() {
        for (int i = 0; i < cellCount - 1; i++) {
            for (int k = 0; k < cellCount - 1; k++) {
                fillCell(verticesMatrix[i][k], verticesMatrix[i + 1][k], verticesMatrix[i + 1][k + 1], verticesMatrix[i][k + 1]);
            }
        }
    }

    private void fillCell(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {

        // Делю клетку на два треугольника и добавляю их в очередь для отрисовки
        vertices.add(d);
        verticesStartZ.add(d.y);
        vertices.add(c);
        verticesStartZ.add(c.y);
        vertices.add(b);
        verticesStartZ.add(b.y);

        vertices.add(a);
        verticesStartZ.add(a.y);
        vertices.add(d);
        verticesStartZ.add(d.y);
        vertices.add(b);
        verticesStartZ.add(b.y);

    }

    // Считаю нормаль к клетке. Обычное векторное произведение векторов
    public void calcNormal(Vector3f s1, Vector3f s2, Vector3f s3) {
        Vector3f a = new Vector3f(s2.x - s1.x, s2.y - s1.y, s2.z - s1.z);
        Vector3f b = new Vector3f(s3.x - s2.x, s3.y - s2.y, s3.z - s2.z);
        Vector3f normal = new Vector3f(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
        float length = (float) Math.pow(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z, 0.5f);
        glNormal3f(normal.x / length, normal.y / length, normal.z / length);
    }

    private void updateHeight() {
        float dph = 0;

        for (int i = 0; i < vertices.size(); i++) {
            dph = (float) Math.asin((vertices.get(i).y / maxHeight));
            vertices.get(i).y = (float) ((Math.sin(dtime + verticesStartZ.get(i)) * maxHeight)) / scaler + seaLevel;
        }

    }

    public void drawSea(float dtime) {
        // Эта функция отрисует все твои клетки
        this.dtime += dtime;
        updateHeight();

        glBegin(GL_TRIANGLES);
        glColor3f(0.5f, 0.6f, 0.8f);

        for (int k = 0; k < vertices.size() / 3; k += 1) {
            calcNormal(vertices.get(k * 3 + 0), vertices.get(k * 3 + 1), vertices.get(k * 3 + 2));

            glVertex3f(vertices.get(k * 3 + 0).x, vertices.get(k * 3 + 0).y, vertices.get(k * 3 + 0).z);
            glVertex3f(vertices.get(k * 3 + 1).x, vertices.get(k * 3 + 1).y, vertices.get(k * 3 + 1).z);
            glVertex3f(vertices.get(k * 3 + 2).x, vertices.get(k * 3 + 2).y, vertices.get(k * 3 + 2).z);
        }

        glEnd();

    }
}
