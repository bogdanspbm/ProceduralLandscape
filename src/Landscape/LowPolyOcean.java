package Landscape;

import static Utils3D.Stereometry.calcNormal;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class LowPolyOcean {

    private static int size = 150;
    private float scaler = 2;
    private float waterLevel = -4f;
    private float[][] waterPoly = new float[size][size];

    public LowPolyOcean() {
        generateRandHeight();
    }

    private void generateRandHeight() {
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                waterPoly[i][k] = (float) Math.asin(Math.random());
            }
        }
    }

    private void calcNewHeight() {
        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - 1; k++) {
                waterPoly[i][k] += 0.01;
            }
        }
    }

    private void drawCell(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
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

    private void selectColor(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        if ((a.y + b.y + c.y + d.y) / 4 < waterLevel) {
            glColor4f(0.8f, 0.95f, 0.95f, 0.3f);
        } else {
            glColor4f(0.8f, 0.97f, 0.8f, 0.5f);
        }
    }

    public void drawOcean(Vector3f[][] mat) {
        Vector3f a = new Vector3f(), b = new Vector3f(), c = new Vector3f(), d = new Vector3f();
        calcNewHeight();
        glBegin(GL_TRIANGLES);
        glColor4f(0.8f, 0.95f, 0.95f, 0.6f);
        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - 1; k++) {
                a.x = mat[i][k].x;
                a.y = (float) Math.sin(waterPoly[i][k]);
                a.z = mat[i][k].z;

                b.x = mat[i + 1][k].x;
                b.y = (float) Math.sin(waterPoly[i + 1][k]);
                b.z = mat[i + 1][k].z;

                c.x = mat[i + 1][k + 1].x;
                c.y = (float) Math.sin(waterPoly[i + 1][k + 1]);
                c.z = mat[i + 1][k + 1].z;

                d.x = mat[i][k + 1].x;
                d.y = (float) Math.sin(waterPoly[i][k + 1]);
                d.z = mat[i][k + 1].z;

                selectColor(mat[i][k], mat[i + 1][k], mat[i + 1][k + 1], mat[i][k + 1]);
                drawCell(a, b, c, d);
            }
        }
        glEnd();
    }

}
