package Landscape;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;

public class Terrain {

    private int size = 10;
    private int height = 10;
    private final List<Vector3f> vertices = new ArrayList<Vector3f>();
    Vector3f s1;
    Vector3f s2;
    Vector3f s3;
    Vector3f s4;

    public Terrain(int newSize, int height) {
        this.size = newSize;
        this.height = height;
        int h = (int) (Math.random() * height - height / 2);
        s1 = new Vector3f(-size, h, size);
        h = (int) (Math.random() * height - height / 2);
        s2 = new Vector3f(size, h, size);
        h = (int) (Math.random() * height - height / 2);
        s3 = new Vector3f(size, h, -size);
        h = (int) (Math.random() * height - height / 2);
        s4 = new Vector3f(-size, h, -size);
        generateSquare(s1, s2, s3, s4, 0);
    }

    public void generateSquare(Vector3f lt, Vector3f rt, Vector3f rd, Vector3f ld, int curDepth) {
        if (curDepth < size) {
            int h = (int) (Math.random() * height - height / 2);
            Vector3f center = new Vector3f(lt.x + (lt.x - rt.x) / 2, (2 * h + lt.y + rt.y + ld.y + rd.y) / 4, lt.z + (lt.z - ld.z) / 2);
            h = (int) (Math.random() * height - height / 2);
            Vector3f cl = new Vector3f(lt.x, (h + lt.y + ld.y + center.y) / 3, lt.z + (lt.z - ld.z) / 2);
            h = (int) (Math.random() * height - height / 2);
            Vector3f cr = new Vector3f(rt.x, (h + rt.y + rd.y + center.y) / 3, rt.z + (rt.z - rd.z) / 2);
            h = (int) (Math.random() * height - height / 2);
            Vector3f ct = new Vector3f(lt.x + (lt.x - rt.x) / 2, (h + lt.y + rt.y + center.y) / 3, lt.z);
            h = (int) (Math.random() * height - height / 2);
            Vector3f cd = new Vector3f(ld.x + (ld.x - rd.x) / 2, (h + ld.y + rd.y + center.y) / 3, ld.z);
            generateSquare(lt, ct, center, cl, curDepth + 1);
            generateSquare(ct, rt, cr, center, curDepth + 1);
            generateSquare(cl, center, cd, ld, curDepth + 1);
            generateSquare(center, cr, rd, cd, curDepth + 1);
        } else {
            //fillCell(lt, rt, rd, ld);
            vertices.add(ld);
            vertices.add(rd);
            vertices.add(rt);

            vertices.add(lt);
            vertices.add(ld);
            vertices.add(rt);
        }
    }

    private void fillCell(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        glBegin(GL_TRIANGLES);
        glColor3f(0.2f, 0.8f, 0.2f);

        glVertex3f(d.x, d.y, d.z);
        glVertex3f(c.x, c.y, c.z);
        glVertex3f(b.x, b.y, b.z);

        glVertex3f(a.x, a.y, a.z);
        glVertex3f(d.x, d.y, d.z);
        glVertex3f(b.x, b.y, b.z);
        glEnd();
    }

    public void drawTerrain() {
        glBegin(GL_TRIANGLES);
        glColor3f(0.2f, 0.8f, 0.2f);
        for (int i = 0; i < vertices.size() / 3; i += 3) {
            glVertex3f(vertices.get(i).x, vertices.get(i).y, vertices.get(i).z);
            glVertex3f(vertices.get(i + 1).x, vertices.get(i + 1).y, vertices.get(i + 1).z);
            glVertex3f(vertices.get(i + 2).x, vertices.get(i + 2).y, vertices.get(i + 2).z);
        }
        glEnd();
    }
}
