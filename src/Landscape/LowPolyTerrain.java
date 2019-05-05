package Landscape;

import Actors.StaticMesh;
import static Utils3D.Stereometry.calcNormal;
import static Utils3D.Stereometry.getVectorWorldDegree;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class LowPolyTerrain {

    private Texture terrainColors;
    private String texturePath = "res/textures/T_PolygonNature_01.tga";
    private StaticMesh tree = new StaticMesh("res/models/Tree.obj", "res/textures/T_PolygonNature_01.tga");
    private float seed;
    private boolean bGeneratedTrees = false;
    private static float waterLevel = 0.6f;

    public LowPolyTerrain() {
        try {
            terrainColors = TextureLoader.getTexture("tga", new FileInputStream(new File(texturePath)));
        } catch (IOException ex) {
            System.out.print("Can't load texture");
            ex.printStackTrace();
        }

        seed = (float) (Math.PI * 2 * 10 * (1 + Math.random()));
    }

    private static Vector2f[] selectRandomGreen() {
        Vector2f[] textCoords = new Vector2f[3];
        int caseNum = (int) (Math.random() * 0);
        switch (caseNum) {
            case 0:
                textCoords[0] = new Vector2f(0f, 0.1f);
                textCoords[1] = new Vector2f(0f, 0.1f);
                textCoords[2] = new Vector2f(0f, 0.1f);
                break;
            case 1:
                textCoords[0] = new Vector2f(0f, 9f);
                textCoords[1] = new Vector2f(0f, 9f);
                textCoords[2] = new Vector2f(0f, 9f);
                break;
            case 2:
                textCoords[0] = new Vector2f(0f, 0.6f);
                textCoords[1] = new Vector2f(0f, 0.6f);
                textCoords[2] = new Vector2f(0f, 0.6f);
                break;
        }
        return textCoords;
    }

    private void drawWater() {
        glBegin(GL_QUADS);
        glColor4f(0.7f, 0.9f, 0.9f, 0.5f);
        glNormal3f(0, 1, 0);
        glVertex3f(-500f, waterLevel, -500f);
        glVertex3f(-500f, waterLevel, 500f);
        glVertex3f(500f, waterLevel, 500f);
        glVertex3f(500f, waterLevel, -500f);
        glEnd();

    }

    private static Vector2f[] selectRandomGray() {
        Vector2f[] textCoords = new Vector2f[3];
        int caseNum = (int) (Math.random() * 0);
        switch (caseNum) {
            case 0:
                textCoords[0] = new Vector2f(0.4f, 0.9f);
                textCoords[1] = new Vector2f(0.4f, 0.9f);
                textCoords[2] = new Vector2f(0.4f, 0.9f);
                break;
            case 1:
                textCoords[0] = new Vector2f(0.4f, 0f);
                textCoords[1] = new Vector2f(0.4f, 0f);
                textCoords[2] = new Vector2f(0.4f, 0f);
                break;
            case 2:
                textCoords[0] = new Vector2f(0.4f, 0.6f);
                textCoords[1] = new Vector2f(0.4f, 0.6f);
                textCoords[2] = new Vector2f(0.4f, 0.6f);
                break;
        }
        return textCoords;
    }

    private static Vector2f[] selectSand() {
        Vector2f[] textCoords = new Vector2f[3];

        textCoords[0] = new Vector2f(0.8f, 0.3f);
        textCoords[1] = new Vector2f(0.8f, 0.3f);
        textCoords[2] = new Vector2f(0.8f, 0.3f);

        return textCoords;

    }

    private static Vector2f[] textureGet(Vector3f a, Vector3f b, Vector3f c) {
        float normalDegree = getVectorWorldDegree(calcNormal(a, b, c));
        if ((float) ((a.y + b.y + c.y) / 3) < (float) (waterLevel - 4f)) {
            return selectSand();
        }

        if (normalDegree < 0.7f) {
            return selectRandomGreen();

        } else {
            return selectRandomGray();
        }

    }

    private void cellToFlat(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
        Vector3f normal = calcNormal(d, c, b);
        Vector2f[] texture = textureGet(d, c, b);
        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(texture[0].x, texture[0].y);
        glVertex3f(d.x, d.y, d.z);
        glTexCoord2f(texture[1].x, texture[1].y);
        glVertex3f(c.x, c.y, c.z);
        glTexCoord2f(texture[2].x, texture[2].y);
        glVertex3f(b.x, b.y, b.z);

        normal = calcNormal(a, d, b);
        texture = textureGet(a, d, b);
        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(texture[0].x, texture[0].y);
        glVertex3f(a.x, a.y, a.z);
        glTexCoord2f(texture[1].x, texture[1].y);
        glVertex3f(d.x, d.y, d.z);
        glTexCoord2f(texture[2].x, texture[2].y);
        glVertex3f(b.x, b.y, b.z);
    }

    private boolean canPlaceTree(Vector3f[][] mat, int x, int y) {
        boolean res = true;
        float normalDegree = getVectorWorldDegree(calcNormal(mat[x][y], mat[x + 1][y], mat[x + 1][y + 1]));
        if (normalDegree - 2 < 0.7f) {
            res = false;
            System.out.println("Bad degree " + normalDegree);
            return res;
        }
        
        if ((mat[x][y].y + mat[x + 1][y].y + mat[x + 1][y + 1].y) / 3 < waterLevel + 2f) {
            res = false;
            System.out.println("Bad water level " + (mat[x][y].y + mat[x + 1][y].y + mat[x + 1][y + 1].y) / 3);
            return res;
        }

        return res;
    }

    private void generateTrees(Vector3f[][] mat, int count, int size) {
        int x, y;
        Vector3f a, b, c;
        if (bGeneratedTrees == false) {
            for (int i = 0; i < count; i++) {
                x = (int) (Math.random() * (size - 2));
                y = (int) (Math.random() * (size - 2));
                a = mat[x][y + 1];
                b = mat[x + 1][y+1];
                c = mat[x][y + 1];
                if (canPlaceTree(mat, x, y)) {
                    tree.addCopy((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3);
                    System.out.println("good");
                } else {
                    ///System.out.println("can't place");
                }
            }
            bGeneratedTrees = true;
        }
    }

    public void matrixToLandscape(Vector3f[][] mat, int size) {

        generateTrees(mat, 10000, size);
        terrainColors.bind();
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);

        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - 1; k++) {
                cellToFlat(mat[i][k], mat[i + 1][k], mat[i + 1][k + 1], mat[i][k + 1]);
            }
        }
        glEnd();
        glDisable(GL_TEXTURE_2D);
        drawWater();
        tree.drawModel();
    }

}
