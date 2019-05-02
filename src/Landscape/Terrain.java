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

public class Terrain {

    private List<Vector3f> vertices = new ArrayList<>();
    private int cellCount = 32;
    private Vector3f[][] verticesMatrix;
    private int maxHeight = 5;

    public Terrain(int newSize, int height) {
        calcCellCount();
        fillZerosVerticesMatrix();
        fillCorners();
        squareFill(0, 0, cellCount - 1, cellCount - 1);
        fillVericexArray();
    }

    public void refreshTerrain() {
        // Обнуляю переменные и запускаю все из конструктора
        maxHeight = 2 + (int) (Math.random() * 20);
        vertices = new ArrayList<>();
        fillZerosVerticesMatrix();
        fillCorners();
        squareFill(0, 0, cellCount - 1, cellCount - 1);
        fillVericexArray();
    }

    // Считаю размер матрицы в зависимости от заданной величены, тк.
    // кол-во клеток на новом шагу n += n-1 => нам не подойдет любое число
    // Его нужно взять приблизительно
    // Формула кстати 2^n + 1;
    private void calcCellCount() {
        int k = 2;
        while (k < cellCount) {
            k += k - 1;
        }
        cellCount = k;
        verticesMatrix = new Vector3f[cellCount][cellCount];
    }

    // Заполняю матрицу 0 высотами
    private void fillZerosVerticesMatrix() {
        for (int i = 0; i < cellCount; i++) {
            for (int k = 0; k < cellCount; k++) {
                verticesMatrix[i][k] = new Vector3f((float) (i - cellCount / 2), (float) 0, (float) (k - cellCount / 2));
                // i и k в данном случае координаты 
                // по идеи можно поменять систему счисления тем самым уменьшив размер поля и уменьшить полигоны
                // как вариант поделив на const
            }
        }
    }

    private void fillCorners() {
        // Выставляю рандомные значения высот на краях карты 
        // Можно эксперементировать делая один больше другой меньше 
        verticesMatrix[0][0].y = (float) (Math.random() * 2 * maxHeight) - 40.0f;
        verticesMatrix[0][cellCount - 1].y = (float) (Math.random() / 2 * maxHeight) - 46.0f;
        verticesMatrix[cellCount - 1][cellCount - 1].y = (float) (Math.random() * maxHeight) - 45.0f;
        verticesMatrix[cellCount - 1][0].y = (float) (Math.random() * maxHeight) - 45.0f;
    }

    // X,Y индекс координата в матрице verticesMatrix
    private void squareFill(int leftTopX, int leftTopY, int rightDownX, int rightDownY) {

        // Алгоритм Diamond Square
        // Вычисляю разницу координаты самых нижних и вверхних
        // Самых левых и правых
        float dx = (rightDownX - leftTopX) / 2;
        float dy = (leftTopY - rightDownY) / 2;

        // Если разница не 0 => можно произвести деление
        if ((int) dx != 0 && (int) dy != 0) {

            // Нахожу координату центра клетки
            int centerX = leftTopX + (int) dx;
            int centerY = rightDownY + (int) dy;

            // Считаю ей по формуле высоту 
            float centerZ = (verticesMatrix[leftTopX][leftTopY].y
                    + verticesMatrix[leftTopX][rightDownY].y
                    + verticesMatrix[rightDownX][leftTopY].y
                    + verticesMatrix[rightDownX][rightDownY].y) / 4
                    + (float) Math.random();
            // Опять же можно модифицировать
            //но главное это брать сумму 4 соседей

            // Записываю высоту 
            verticesMatrix[centerX][centerY].y = centerZ;

            // Делаю тоже самое, но для левой, правой и тд точки
            float topCenterZ = (verticesMatrix[leftTopX][leftTopY].y
                    + verticesMatrix[rightDownX][leftTopY].y
                    + centerZ) / 3
                    + (float) Math.random();
            float downCenterZ = (verticesMatrix[leftTopX][rightDownY].y
                    + verticesMatrix[rightDownX][rightDownY].y
                    + centerZ) / 3
                    + (float) Math.random();
            float leftCenterZ = (verticesMatrix[leftTopX][leftTopY].y
                    + verticesMatrix[leftTopX][rightDownY].y
                    + centerZ) / 3
                    + (float) Math.random();
            float rightCenterZ = (verticesMatrix[rightDownX][leftTopY].y
                    + verticesMatrix[rightDownX][rightDownY].y
                    + centerZ) / 3
                    + (float) Math.random();

            // Записываю высоту
            verticesMatrix[centerX][centerY + (int) dy].y = topCenterZ;
            verticesMatrix[centerX][centerY - (int) dy].y = downCenterZ;
            verticesMatrix[centerX - (int) dx][centerY].y = leftCenterZ;
            verticesMatrix[centerX + (int) dx][centerY].y = rightCenterZ;

            // Запускаю рекурсивно для левого вверхнего квадрата
            squareFill(leftTopX, leftTopY, centerX, centerY);
            // Правого нижнего
            squareFill(centerX, centerY, rightDownX, rightDownY);
            // Правого вверхнего
            squareFill(centerX, leftTopY, rightDownX, centerY);
            // Левого нижнего
            squareFill(leftTopX, centerY, centerX, rightDownY);
        }
    }

    // Подаю клетки в функцию, где ее разделят на полигоны и добавял в массив для отрисовки
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
        vertices.add(c);
        vertices.add(b);

        vertices.add(a);
        vertices.add(d);
        vertices.add(b);

    }

    // Считаю нормаль к клетке. Обычное векторное произведение векторов
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

        for (int k = 0; k < vertices.size() / 3; k += 1) {

            if (vertices.get(k * 3).y > -maxHeight / 2 - 30f) { // Попытка раскрасить участок по высоте
                glColor3f(1.0f, 1.0f, 1.0f);
            } else {
                glColor3f(0.2f, 0.8f, 0.2f);
            }

            calcNormal(vertices.get(k * 3 + 0), vertices.get(k * 3 + 1), vertices.get(k * 3 + 2));

            glVertex3f(vertices.get(k * 3 + 0).x, vertices.get(k * 3 + 0).y, vertices.get(k * 3 + 0).z);
            glVertex3f(vertices.get(k * 3 + 1).x, vertices.get(k * 3 + 1).y, vertices.get(k * 3 + 1).z);
            glVertex3f(vertices.get(k * 3 + 2).x, vertices.get(k * 3 + 2).y, vertices.get(k * 3 + 2).z);
        }

        glEnd();
    }
}
