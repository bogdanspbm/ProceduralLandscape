package Utils3D;

import static Utils3D.Stereometry.calcNormal;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
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

}
