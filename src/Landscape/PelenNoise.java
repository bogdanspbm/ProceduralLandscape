package Landscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public final class PelenNoise {

    private List<Vector3f> vertices = new ArrayList<>();
    private int cellCount = 100;
    private Vector3f[][] verticesMatrix = new Vector3f[cellCount][cellCount];
    private float scaler = 0.25f;
    private float defaultFreq = 0.15f;
    private float defaultAmplitude = 4f;
    private float defaultPersis = 0.2f;
    private int inum = 1;
    private int NUM_OCTAVES = 50;
    private int varToChange = 0;
    float seed = (float) (Math.PI * 2 * 10 * (1 + Math.random()));
    private float lowPoint = 10000, hightPoint = -10000;
    String filePath = "res/textures/gradient.png";
    Texture gradient;

    public PelenNoise(int seed) {

        try {
            gradient = TextureLoader.getTexture("PNG", new FileInputStream(new File(filePath)));
        } catch (IOException ex) {
            System.out.print("Can't load texture");
            Logger.getLogger(SkyBox.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        refresh();
    }

    public void changeVar(float toAdd) {
        switch (varToChange) {
            case 1:
                defaultFreq += toAdd;
                break;
            case 2:
                defaultAmplitude += toAdd;
                break;
            case 3:
                defaultPersis += toAdd;
                break;
        }

        System.out.println(defaultAmplitude + " Amplitude");
        System.out.println(defaultFreq + " Freq");
        System.out.println(defaultPersis + " Persis");

        refresh();
    }

    public void changeVarToChange(int mode) {
        varToChange = mode;
    }

    public void refresh() {
        vertices = new ArrayList<>();
        inum = getIPrime((int) (Math.random() * 19));
        fillZerosVerticesMatrix();
        genNoise();
        fillVericexArray();
        getHightAndLow();
    }

    private int getIPrime(int i) {
        int[] primes = {3, 5, 7, 11, 13, 17, 31, 37, 41, 43, 47, 53, 59, 71, 73, 79, 97, 113, 157, 179};
        return primes[i];
    }

    private float noise2D(int x, int y) {
        int n = (int) (x + y * inum);
        n = (n << 13) ^ n;
        return (1.0f - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff)
                / 1073741824.0f);
    }

    private float smoothedNoise2D(int x, int y) {
        float corners = (noise2D(x - 1, y - 1) + noise2D(x + 1, y - 1)
                + noise2D(x - 1, y + 1) + noise2D(x + 1, y + 1)) / 16;
        float sides = (noise2D(x - 1, y) + noise2D(x + 1, y)
                + noise2D(x, y - 1) + noise2D(x, y + 1)) / 8;
        float center = noise2D(x, y) / 4;
        return corners + sides + center;
    }

    private float compileNoise(float x, float y) {
        int int_X = (int) (x);//целая часть х
        float fractional_X = x - int_X;//дробь от х
//аналогично у
        int int_Y = (int) (y);
        float fractional_Y = y - int_Y;
        //получаем 4 сглаженных значения
        float v1 = smoothedNoise2D(int_X, int_Y);
        float v2 = smoothedNoise2D(int_X + 1, int_Y);
        float v3 = smoothedNoise2D(int_X, int_Y + 1);
        float v4 = smoothedNoise2D(int_X + 1, int_Y + 1);
        //интерполируем значения 1 и 2 пары и производим интерполяцию между ними
        float i1 = lerp(v1, v2, cosinusCurve(fractional_X));
        float i2 = lerp(v3, v4, cosinusCurve(fractional_X));
        //я использовал косинусною интерполяцию ИМХО лучше 
        //по параметрам быстрота-//качество
        return lerp(i1, i2, cosinusCurve(fractional_Y));
    }

    private float perlinNoise2D(float x, float y, float factor) {
        float total = 0;
        // это число может иметь и другие значения хоть cosf(sqrtf(2))*3.14f 
        // главное чтобы было красиво и результат вас устраивал
        float persistence = defaultPersis;

        // экспериментируйте с этими значениями, попробуйте ставить 
        // например sqrtf(3.14f)*0.25f или что-то потяжелее для понимания J)
        float frequency = defaultFreq;
        float amplitude = defaultAmplitude;//амплитуда, в прямой зависимости от значения настойчивости

        // вводим фактор случайности, чтобы облака не были всегда одинаковыми
        // (Мы ведь помним что ф-ция шума когерентна?) 
        x += (factor);
        y += (factor);

        // NUM_OCTAVES - переменная, которая обозначает число октав,
        // чем больше октав, тем лучше получается шум
        for (int i = 0; i < NUM_OCTAVES; i++) {
            total += compileNoise(x * frequency, y * frequency) * amplitude;
            amplitude *= persistence;
            frequency *= Math.E;
        }

        //total += 1;
        //total /= 2;
        float res = (float) (total);
        return res;
    }

    private void genNoise() {
        // случайное число, которое призвано внести
        // случайность в нашу текстуру
        float fac = (float) (Math.PI * 2 * 10 * (1 + Math.random()));

        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                //проходим по всем элементам массива и заполняем их значениями   
                verticesMatrix[i][j].y = 4f * perlinNoise2D((float) i, (float) j, fac) / scaler;
            }
        }
    }

    private float cosinusCurve(float t) {
        return (float) (1 - Math.cos(t * Math.PI)) / 2;
    }

    static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
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
    }

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

    public void calcNormal(Vector3f s1, Vector3f s2, Vector3f s3) {
        Vector3f a = new Vector3f(s2.x - s1.x, s2.y - s1.y, s2.z - s1.z);
        Vector3f b = new Vector3f(s3.x - s2.x, s3.y - s2.y, s3.z - s2.z);
        Vector3f normal = new Vector3f(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
        float length = (float) Math.pow(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z, 0.5f);
        glNormal3f(normal.x / length, normal.y / length, normal.z / length);
    }

    private void setVertexWithColor(int num) {
        if ((float) (vertices.get(num).y - lowPoint) / (hightPoint - lowPoint) > 0.95f) {
            glTexCoord2f((float) (vertices.get(num).y - lowPoint) / (hightPoint - lowPoint) - 0.05f, 0.5f);
        } else {
            glTexCoord2f((float) (vertices.get(num).y - lowPoint) / (hightPoint - lowPoint), 0.5f);
        }
        glVertex3f(vertices.get(num).x, vertices.get(num).y, vertices.get(num).z);
    }

    public void drawTerrain() {
        // Эта функция отрисует все твои клетки
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);

        for (int k = 0; k < vertices.size() / 3; k += 1) {

            calcNormal(vertices.get(k * 3 + 0), vertices.get(k * 3 + 1), vertices.get(k * 3 + 2));

            setVertexWithColor(k * 3 + 0);
            setVertexWithColor(k * 3 + 1);
            setVertexWithColor(k * 3 + 2);

        }

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

}