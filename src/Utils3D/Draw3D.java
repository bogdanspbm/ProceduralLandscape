package Utils3D;

import static Utils3D.Stereometry.calcNormal;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Draw3D {

    private static void cellToFlat(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        Vector3f normal = calcNormal(d, c, b);
        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(d.x, d.y, d.z);
        glVertex3f(c.x, c.y, c.z);
        glVertex3f(b.x, b.y, b.z);

        normal = calcNormal(a, d, b);
        glNormal3f(normal.x, normal.y, normal.z);
        glVertex3f(a.x, a.y, a.z);
        glVertex3f(d.x, d.y, d.z);
        glVertex3f(b.x, b.y, b.z);
    }

    public static void matrixToFlat(Vector3f[][] mat, int size) {
        Vector3f a, b, c, d;
        glBegin(GL_TRIANGLES);
        glColor3f(1f, 1f, 1f);
        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - 1; k++) {
                cellToFlat(mat[i][k], mat[i + 1][k], mat[i + 1][k + 1], mat[i][k + 1]);
            }
        }
        glEnd();
    }

    public static void flatZone(int x, int y, int rad, Vector3f[][] mat, int size) {
        float height = mat[x][y].y;
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
        if (x + rad >= size) {
            ifinish = size - 1 - x;
        }
        if (y + rad >= size) {
            kfinish = size - 1 - y;
        }

        for (int i = istart; i <= ifinish; i++) {
            for (int k = kstart; k <= kfinish; k++) {
                dh = (mat[i + x][k + y].y - height) / (rad * rad) * Math.abs(i * k);
                mat[i + x][k + y].y = height + dh;
            }
        }
    }

    public static Vector2f vec3ToMatCord(Vector3f in, int size, int scaler) {
        float x, y;
        x = (in.x + scaler * size / 2) / scaler;
        y = (in.y + scaler * size / 2) / scaler;
        Vector2f res = new Vector2f(x, y);
        return res;
    }

    public static void genRoadBetweenPoints(int x1, int y1, int x2, int y2, Vector3f[][] mat, int size) {
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
            if (k < 0.5) {
                if (y1 + k * i + 1 < size && y1 + k * i >= 0 && xmin + i + 1 < size && (int) (y1 + k * i) - 1 >= 0) {
                    dh = 10f;
                    mat[(xmin + i)][(int) (y1 + k * i)].y += dh;
                    mat[(xmin + i)][(int) (y1 + k * i) + 1].y += dh;
                    mat[(xmin + i + 1)][(int) (y1 + k * i)].y += dh;
                    mat[(xmin + i + 1)][(int) (y1 + k * i) + 1].y += dh;
                    mat[(xmin + i)][(int) (y1 + k * i) - 1].y += dh;
                    mat[(xmin + i + 1)][(int) (y1 + k * i - 1)].y += dh;

                }
            }
        }
    }
}
