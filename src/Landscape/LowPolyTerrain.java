package Landscape;

import Actors.StaticMesh;
import Main.Camera;
import static Utils3D.Draw3D.flatZone;
import static Utils3D.Draw3D.genRoadBetweenPoints;
import static Utils3D.Draw3D.vec3ToMatCord;
import static Utils3D.Stereometry.calcNormal;
import static Utils3D.Stereometry.getVectorWorldDegree;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private String[] pinePath = {"res/models/PineA.obj"};
    private String[] treePath = {"res/models/TreeA.obj"};
    private String[] buildingPath = {"res/models/HouseA.obj"};
    private StaticMesh building = new StaticMesh(buildingPath, "res/textures/PolygonAdventure_Tex_01.tga");
    private StaticMesh pine = new StaticMesh(pinePath, "res/textures/T_PolygonNature_01.tga");
    private StaticMesh tree = new StaticMesh(treePath, "res/textures/T_PolygonNature_01.tga");
    private final float seed;
    private int[] buildingsLocationsX, buildingsLocationsY;
    private static final float waterLevel = 0.6f;
    private Camera cam;
    private Vector3f land[][], biom[][];
    private int size = 0;
    private float biomHeightLimit = 0.5f;

    public LowPolyTerrain() {
        try {
            terrainColors = TextureLoader.getTexture("tga", new FileInputStream(new File(texturePath)));
        } catch (IOException ex) {
            System.out.print("Can't load texture");
            ex.printStackTrace();
        }
        seed = (float) (Math.PI * 2 * 10 * (1 + Math.random()));
        tree.setScale(1.5f);
    }

    public void setCamera(Camera cam) {
        this.cam = cam;
    }

    public void setGenerations(Vector3f[][] mat, Vector3f[][] biom, int size) {
        this.land = mat;
        this.biom = biom;
        this.size = size;
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

    private static Vector2f[] selectRandomWhite() {
        Vector2f[] textCoords = new Vector2f[3];
        int caseNum = (int) (Math.random() * 0);
        switch (caseNum) {
            case 0:
                textCoords[0] = new Vector2f(0.9f, 0.9f);
                textCoords[1] = new Vector2f(0.9f, 0.9f);
                textCoords[2] = new Vector2f(0.9f, 0.9f);
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

    public void refresh() {
        biomHeightLimit = (float)Math.random();
        generateBuilding(10);
        generateStaticMesh(pine, 100, 1, waterLevel + 1f, 0.7f, 1000);
        generateStaticMesh(tree, 300, 0, waterLevel + 1f, 0.7f, 1000);
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

    private Vector2f[] textureGetWithXY(Vector3f a, Vector3f b, Vector3f c, int x, int y) {
        float normalDegree = getVectorWorldDegree(calcNormal(a, b, c));
        if ((float) ((a.y + b.y + c.y) / 3) < (float) (waterLevel - 4f)) {
            return selectSand();
        }
        if (biom[x][y].y > biomHeightLimit) {
            if (normalDegree < 0.7f) {
                return selectRandomGreen();

            } else {
                return selectRandomGray();
            }
        } else {
            if (normalDegree < 0.7f) {
                return selectRandomWhite();

            } else {
                return selectRandomGray();
            }
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
        //texture = textureGet(a, d, b);
        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(texture[0].x, texture[0].y);
        glVertex3f(a.x, a.y, a.z);
        glTexCoord2f(texture[1].x, texture[1].y);
        glVertex3f(d.x, d.y, d.z);
        glTexCoord2f(texture[2].x, texture[2].y);
        glVertex3f(b.x, b.y, b.z);
    }

    private void cellToFlatWithXY(Vector3f a, Vector3f b, Vector3f c, Vector3f d, int x, int y) {
        Vector3f normal = calcNormal(d, c, b);
        Vector2f[] texture = textureGetWithXY(d, c, b, x, y);
        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(texture[0].x, texture[0].y);
        glVertex3f(d.x, d.y, d.z);
        glTexCoord2f(texture[1].x, texture[1].y);
        glVertex3f(c.x, c.y, c.z);
        glTexCoord2f(texture[2].x, texture[2].y);
        glVertex3f(b.x, b.y, b.z);

        normal = calcNormal(a, d, b);
       // texture = textureGetWithXY(a, d, b, x, y);
        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(texture[0].x, texture[0].y);
        glVertex3f(a.x, a.y, a.z);
        glTexCoord2f(texture[1].x, texture[1].y);
        glVertex3f(d.x, d.y, d.z);
        glTexCoord2f(texture[2].x, texture[2].y);
        glVertex3f(b.x, b.y, b.z);
    }

    private void generateStaticMesh(StaticMesh mesh, int count) {
        int x, y;
        Vector3f a, b, c;
        mesh.clearCopies();
        for (int i = 0; i < count; i++) {
            x = (int) (Math.random() * (size - 2));
            y = (int) (Math.random() * (size - 2));
            a = land[x][y + 1];
            b = land[x + 1][y + 1];
            c = land[x][y];
            mesh.addCopy((a.x + b.x + c.x) / 3 + (float) (-0.5f + Math.random()) * 6, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3 + (float) (-0.5f + Math.random()) * 6, (float) (Math.random() * 360));
        }
    }

    private void generateStaticMesh(StaticMesh mesh, int count, int biom, float minHeight, float maxDegree) {
        int x, y;
        int dbiom = 0;
        float normalDegree;
        Vector3f a, b, c;
        mesh.clearCopies();
        for (int i = 0; i < count;) {
            x = (int) (Math.random() * (size - 2));
            y = (int) (Math.random() * (size - 2));
            if (this.biom[x][y].y > 0.5) {
                dbiom = 0;
            } else {
                dbiom = 1;
            }
            a = land[x][y + 1];
            b = land[x + 1][y + 1];
            c = land[x][y];

            normalDegree = getVectorWorldDegree(calcNormal(a, b, c));
            if (dbiom == biom && (float) (a.y + b.y + c.y) / 3 > minHeight && normalDegree < maxDegree) {
                mesh.addCopy((a.x + b.x + c.x) / 3 + (float) (-0.5f + Math.random()) * 6, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3 + (float) (-0.5f + Math.random()) * 6, (float) (Math.random() * 360));
                i++;
            }
        }
    }

    private void generateStaticMesh(StaticMesh mesh, int count, int biom, float minHeight, float maxDegree, int maxLimit) {
        int x, y;
        int dbiom = 0;
        int k = 0;
        float normalDegree;
        Vector3f a, b, c;
        mesh.clearCopies();
        for (int i = 0; i < count; k++) {
            if (k > maxLimit) {
                return;
            }
            x = (int) (Math.random() * (size - 2));
            y = (int) (Math.random() * (size - 2));
            if (this.biom[x][y].y > biomHeightLimit) {
                dbiom = 0;
            } else {
                dbiom = 1;
            }
            a = land[x][y + 1];
            b = land[x + 1][y + 1];
            c = land[x + 1][y];

            normalDegree = getVectorWorldDegree(calcNormal(a, b, c));
            if (dbiom == biom && (float) (a.y + b.y + c.y) / 3 > minHeight && normalDegree < maxDegree) {
                mesh.addCopy((a.x + b.x + c.x) / 3 + (float) (-0.5f + Math.random()) * 6, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3 + (float) (-0.5f + Math.random()) * 6, (float) (Math.random() * 360));
                i++;
            }
        }
    }

    private void generateBuilding(int count) {

        buildingsLocationsX = new int[count];
        buildingsLocationsY = new int[count];

        int x, y;
        Vector3f a, b, c;
        building.clearCopies();
        for (int i = 0; i < count; i++) {
            x = (int) (Math.random() * (size - 2));
            y = (int) (Math.random() * (size - 2));

            if (land[x][y].y > waterLevel) { // Выше уровня моря
                buildingsLocationsX[i] = x;
                buildingsLocationsY[i] = y;
                flatZone(x, y, 3, land, size);
                a = land[x][y + 1];
                b = land[x + 1][y + 1];
                c = land[x][y];
                building.addCopy((a.x + b.x + c.x) / 3 + (float) (-0.5f + Math.random()) * 6, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3 + (float) (-0.5f + Math.random()) * 6, (float) (Math.random() * 360));
            }

        }

    }

    public void matrixToLandscape() {

        terrainColors.bind();
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        glColor4f(1, 1, 1, 1);

        for (int i = 0; i < size - 1; i++) {
            for (int k = 0; k < size - 1; k++) {
                cellToFlatWithXY(land[i][k], land[i + 1][k], land[i + 1][k + 1], land[i][k + 1], i, k);
            }
        }
        glEnd();
        glDisable(GL_TEXTURE_2D);
        building.drawModel();
        pine.drawModel();
        tree.drawModel();

    }

}
