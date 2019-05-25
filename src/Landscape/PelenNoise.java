package Landscape;

import static Utils3D.Draw3D.matrixToFlat;
import org.lwjgl.util.vector.Vector3f;

public final class PelenNoise {

    private int cellCount = 200; // было 150
    private Vector3f[][] verticesMatrix;
    private float[] verticesToBuffer;
    private float scaler = 1f; // было 0.25f
    private float defaultFreq = 0.15f;
    private float defaultAmplitude = 8f;
    private float defaultPersis = 0.2f;
    private float height = 4f;
    private int inum = 1;
    private int NUM_OCTAVES = 50;
    float seed = (float) (Math.PI * 2 * 10 * (1 + Math.random()));

    public PelenNoise() {
        verticesToBuffer = new float[(cellCount - 1) * (cellCount - 1) * 18];
        verticesMatrix = new Vector3f[cellCount][cellCount];
        refresh();
    }

    public PelenNoise(float scaler, int size) {
        this.cellCount = size;
        this.scaler = scaler;
        verticesToBuffer = new float[(cellCount - 1) * (cellCount - 1) * 18];
        verticesMatrix = new Vector3f[cellCount][cellCount];
        refresh();
    }
    
     public PelenNoise(float scaler, int size, float height) {
        this.cellCount = size;
        this.height = height;
        this.scaler = scaler;
        verticesToBuffer = new float[(cellCount - 1) * (cellCount - 1) * 18];
        verticesMatrix = new Vector3f[cellCount][cellCount];
        refresh();
    }

    private void matToVector() {
        int x = 0, y = 0;
        float a, b, c;
        int counterV = 0;
        for (int i = 0; i < cellCount - 1; i++) {
            for (int k = 0; k < cellCount - 1; k++) {

                a = verticesMatrix[i][k].x;
                b = verticesMatrix[i][k].y;
                c = verticesMatrix[i][k].z;

                verticesToBuffer[counterV] = a;
                counterV++;
                verticesToBuffer[counterV] = b;
                counterV++;
                verticesToBuffer[counterV] = c;
                counterV++;



                a = verticesMatrix[i][k + 1].x;
                b = verticesMatrix[i][k + 1].y;
                c = verticesMatrix[i][k + 1].z;
                ;
                verticesToBuffer[counterV] = a;
                counterV++;
                verticesToBuffer[counterV] = b;
                counterV++;
                verticesToBuffer[counterV] = c;
                counterV++;



                a = verticesMatrix[i + 1][k].x;
                b = verticesMatrix[i + 1][k].y;
                c = verticesMatrix[i + 1][k].z;

                verticesToBuffer[counterV] = a;
                counterV++;
                verticesToBuffer[counterV] = b;
                counterV++;
                verticesToBuffer[counterV] = c;
                counterV++;



                a = verticesMatrix[i][k + 1].x;
                b = verticesMatrix[i][k + 1].y;
                c = verticesMatrix[i][k + 1].z;
                verticesToBuffer[counterV] = a;
                counterV++;
                verticesToBuffer[counterV] = b;
                counterV++;
                verticesToBuffer[counterV] = c;
                counterV++;


                a = verticesMatrix[i + 1][k + 1].x;
                b = verticesMatrix[i + 1][k + 1].y;
                c = verticesMatrix[i + 1][k + 1].z;
                
                verticesToBuffer[counterV] = a;
                counterV++;
                verticesToBuffer[counterV] = b;
                counterV++;
                verticesToBuffer[counterV] = c;
                counterV++;


                a = verticesMatrix[i + 1][k].x;
                b = verticesMatrix[i + 1][k].y;
                c = verticesMatrix[i + 1][k].z;

                verticesToBuffer[counterV] = a;
                counterV++;
                verticesToBuffer[counterV] = b;
                counterV++;
                verticesToBuffer[counterV] = c;
                counterV++;
            }
            
        }
        System.out.println(counterV);
    }

    private static float[] texCordFromHeight(float h) {
        float[] res = new float[2];
        if (h < 0.2) {
            res[0] = 0;
            res[1] = 0;
            return res;
        }
        if (h > 1) {
            res[0] = 0.66f;
            res[1] = 0.66f;
            return res;
        }
        res[0] = 0.33f;
        res[1] = 0.88f;
        return res;
    }

    public float[] getVerticesVector() {
        return verticesToBuffer;
    }


    public void refresh() {
        inum = getIPrime((int) (Math.random() * 19));
        fillZerosVerticesMatrix();
        genNoise();
        matToVector();
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
                verticesMatrix[i][j].y = height * perlinNoise2D((float) verticesMatrix[i][j].x, verticesMatrix[i][j].z, fac) / scaler;
            }
        }
    }

    private void genRoadBetweenPoints(int x1, int y1, int x2, int y2) {
        int xmin, xmax;
        int ymin, ymax;
        float dh;
        if (x1 > x2) {
            xmin = x2;
            xmax = x1;
            ymin = y2;
            ymax = y1;
        } else {
            xmin = x1;
            xmax = x2;
            ymin = y1;
            ymax = y2;
        }
        float kx = xmax - xmin;
        float ky = ymax - ymin;
        float k = kx / ky;
        for (int i = 0; i < xmax - xmin; i++) {

            if (y1 + k * i < cellCount && y1 + k * i >= 0) {
                dh = verticesMatrix[(xmin + i)][(int) (y1 + k * i)].y + 10;
                verticesMatrix[(xmin + i)][(int) (y1 + k * i)].y = dh;

                if (y1 + k * i + 1 < cellCount && y1 + k * i + 1 >= 0) {
                    verticesMatrix[(xmin + i)][(int) (y1 + k * i) + 1].y = dh;
                }
            }
        }
    }

    private void flatZone(int x, int y, int rad) {
        float height = verticesMatrix[x][y].y;
        float dh;
        int istart = -rad;
        int ifinish = rad;
        int kstart = -rad;
        int kfinish = rad;

        if (x - rad < 0) {
            istart = -x;
        }
        if (y - rad < 0) {
            kstart = -y;
        }
        if (x + rad >= cellCount) {
            ifinish = cellCount - 1 - x;
        }
        if (y + rad >= cellCount) {
            kfinish = cellCount - 1 - y;
        }

        for (int i = istart; i <= ifinish; i++) {
            for (int k = kstart; k <= kfinish; k++) {
                dh = (verticesMatrix[i + x][k + y].y - height) / rad;
                verticesMatrix[i + x][k + y].y = height + dh;
            }
        }
    }

    private float cosinusCurve(float t) {
        return (float) (1 - Math.cos(t * Math.PI)) / 2;
    }

    static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
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
