package Landscape;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Terrain {

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector4f> cellsToDo = new ArrayList<>();
    private List<Vector4f> rhumbsToDo = new ArrayList<>();
    private int cellCount = 100;
    private float scaler = 1f;
    private int state = 0;
    private Vector3f[][] verticesMatrix;
    private float maxHeight = 22;
    private float lowPoint = 10000, hightPoint = -10000;
    private int depth = 1;
    private Sea sea = new Sea(cellCount, scaler);

    public Terrain(int newSize, int height) {
        calcCellCount();
        fillZerosVerticesMatrix();
        fillCorners();
        buildTerrain();
        fillVericexArray();
        getHightAndLow();
    }

    public void refreshTerrain() {
        // Обнуляю переменные и запускаю все из конструктора
        vertices = new ArrayList<>();
        cellsToDo = new ArrayList<>();
        rhumbsToDo = new ArrayList<>();
        state = 0;
        depth = 1;
        fillZerosVerticesMatrix();
        fillCorners();
        buildTerrain();
        fillVericexArray();
        getHightAndLow();
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
                verticesMatrix[i][k] = new Vector3f((float) (i - cellCount / 2) / scaler, (float) 0, (float) (k - cellCount / 2) / scaler);
                // i и k в данном случае координаты 
                // по идеи можно поменять систему счисления тем самым уменьшив размер поля и уменьшить полигоны
                // как вариант поделив на const
            }
        }
    }

    private void fillCorners() {
        // Выставляю рандомные значения высот на краях карты 
        // Можно эксперементировать делая один больше другой меньше 
        verticesMatrix[0][0].y = (float) (Math.random() * maxHeight);
        verticesMatrix[0][cellCount - 1].y = (float) (Math.random() * maxHeight);
        verticesMatrix[cellCount - 1][cellCount - 1].y = (float) (Math.random() * maxHeight);
        verticesMatrix[cellCount - 1][0].y = (float) (Math.random() * maxHeight);
        cellsToDo.add(new Vector4f(0, 0, cellCount - 1, cellCount - 1));
    }

    private void getHightAndLow() {
        float h = 0;
        lowPoint = 10000;
        hightPoint = -10000;
        for (int i = 0; i < cellCount; i++) {
            for (int k = 0; k < cellCount; k++) {
                h = verticesMatrix[i][k].y;
                if (h < lowPoint) {
                    lowPoint = h;
                }
                if (h > hightPoint) {
                    hightPoint = h;
                }
            }
        }
        sea.seaLevel = lowPoint + (hightPoint - lowPoint) / 5;
    }

    private void buildTerrain() {
        int lim = 0;
        Vector4f coord;
        while (true) {
            if (state == 0) { // Строим клетки
                lim = cellsToDo.size();
                for (int i = 0; i < lim; i++) {
                    coord = cellsToDo.get(0);
                    if ((int) (coord.z - coord.x) / 2 == 0) {
                        state = 2;
                        break;
                    }
                    squareFill((int) coord.x, (int) coord.y, (int) coord.z, (int) coord.w);
                    cellsToDo.remove(0);
                }
                if (state == 0) {
                    state = 1;
                }
            } else { // Строим ромбы
                lim = rhumbsToDo.size();
                for (int i = 0; i < lim; i++) {
                    coord = rhumbsToDo.get(0);
                    if ((int) (coord.z - coord.x) / 2 == 0) {
                        return;
                    }
                    fillRhombus((int) coord.x, (int) coord.y, (int) coord.z, (int) coord.w);
                    rhumbsToDo.remove(0);
                }
                depth *= 2;

                if (state == 1) {
                    state = 0;
                } else {
                    return;
                }
            }
        }

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
                    + (verticesMatrix[leftTopX][leftTopY].y
                    + verticesMatrix[leftTopX][rightDownY].y
                    + verticesMatrix[rightDownX][leftTopY].y
                    + verticesMatrix[rightDownX][rightDownY].y) / 4 * (float) (-0.5 + Math.random()) * 4 / depth / scaler;
            // Опять же можно модифицировать
            //но главное это брать сумму 4 соседей

            // Записываю высоту 
            verticesMatrix[centerX][centerY].y = centerZ;

            rhumbsToDo.add(new Vector4f(leftTopX - (int) dx, leftTopY, centerX, rightDownY));
            rhumbsToDo.add(new Vector4f(leftTopX, leftTopY + (int) dy, rightDownX, centerY));
            rhumbsToDo.add(new Vector4f(centerX, leftTopY, rightDownX + (int) dx, rightDownY));
            rhumbsToDo.add(new Vector4f(leftTopX, centerY, rightDownX, rightDownY - (int) dy));

            // Запускаю рекурсивно для левого вверхнего квадрата
            cellsToDo.add(new Vector4f(leftTopX, leftTopY, centerX, centerY));
            cellsToDo.add(new Vector4f(centerX, centerY, rightDownX, rightDownY));
            cellsToDo.add(new Vector4f(centerX, leftTopY, rightDownX, centerY));
            cellsToDo.add(new Vector4f(leftTopX, centerY, centerX, rightDownY));

        }
    }

    private void fillRhombus(int left, int top, int right, int down) {
        float dx = (right - left) / 2;
        float dy = (top - down) / 2;
        int centerX = left + (int) dx;
        int centerY = down + (int) dy;
        float centerZ = 0;

        // Если разница не 0 => можно произвести деление
        if ((int) dx != 0 && (int) dy != 0) {

            int myLeft = left;
            if (myLeft < 0) {
                myLeft += cellCount - 1;
                centerZ -= verticesMatrix[myLeft][centerY].y / 4;
            }
            int myTop = top;
            if (myTop < 0) {
                myTop += cellCount - 1;
                centerZ -= verticesMatrix[centerX][myTop].y / 4;
            }
            int myDown = down;
            if (myDown >= cellCount) {
                myDown -= cellCount;
                centerZ -= verticesMatrix[centerX][myDown].y / 4;
            }
            int myRight = right;
            if (myRight >= cellCount) {
                myRight -= cellCount;
                centerZ -= verticesMatrix[myRight][centerY].y / 4;
            }

            // Нахожу координату центра клетки
            // Считаю ей по формуле высоту 
            centerZ += (verticesMatrix[myLeft][centerY].y
                    + verticesMatrix[centerX][myDown].y
                    + verticesMatrix[myRight][centerY].y
                    + verticesMatrix[centerX][myTop].y) / 4
                    * (1 + (float) (Math.random() - 0.5) / depth / scaler);
            // Опять же можно модифицировать
            //но главное это брать сумму 4 соседей

            // Записываю высоту 
            verticesMatrix[centerX][centerY].y = centerZ;
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

        for (int k = 0; k < vertices.size() / 3; k += 1) {

            if (vertices.get(k * 3).y > (hightPoint - lowPoint) / 2 + lowPoint) { // Попытка раскрасить участок по высоте
                glColor3f(0.7f, 0.7f, 0.8f);
            } else {
                glColor3f(0.2f, 0.8f, 0.2f);
            }

            calcNormal(vertices.get(k * 3 + 0), vertices.get(k * 3 + 1), vertices.get(k * 3 + 2));

            glVertex3f(vertices.get(k * 3 + 0).x, vertices.get(k * 3 + 0).y, vertices.get(k * 3 + 0).z);
            glVertex3f(vertices.get(k * 3 + 1).x, vertices.get(k * 3 + 1).y, vertices.get(k * 3 + 1).z);
            glVertex3f(vertices.get(k * 3 + 2).x, vertices.get(k * 3 + 2).y, vertices.get(k * 3 + 2).z);
        }

        glEnd();

        //drawDemoOcean();
        sea.drawSea(0.006f);
    }
}
