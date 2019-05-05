package Landscape;

import static Utils3D.Draw3D.matrixToFlat;
import org.lwjgl.util.vector.Vector3f;

public final class PelenNoise {

    private int cellCount = 130;
    private Vector3f[][] verticesMatrix = new Vector3f[cellCount][cellCount];
    private float scaler = 0.25f;
    private float defaultFreq = 0.15f;
    private float defaultAmplitude = 4f;
    private float defaultPersis = 0.2f;
    private int inum = 1;
    private int NUM_OCTAVES = 50;
    float seed = (float) (Math.PI * 2 * 10 * (1 + Math.random()));
    private float lowPoint = 10000, hightPoint = -10000;

    public PelenNoise() {
        refresh();
    }

    public void refresh() {
        inum = getIPrime((int) (Math.random() * 19));
        fillZerosVerticesMatrix();
        genNoise();
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
        float h;
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
            }
        }
    }

    public Vector3f[][] getNoiseMat() {
        return verticesMatrix;
    }

    public void drawTerrain() {
        matrixToFlat(verticesMatrix, cellCount);
    }

    public int getSize() {
        return cellCount;
    }

}
