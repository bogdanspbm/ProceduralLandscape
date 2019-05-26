package Landscape;

import Actors.StaticMesh;
import Actors.VBOModel;
import Utils3D.Stereometry;
import static Utils3D.Stereometry.calcNormal;
import static Utils3D.Stereometry.getVectorWorldDegree;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Terrain {

    private PelenNoise noise, biom;
    private VBOModel terrainModel;
    private Texture textureLand, textureGrass, texturePine, textureBush, textureTree;
    StaticMesh grass, pine, bushWinter, tree, treeB, logA, bushA, stoneA, stoneB;
    float[] vertices, textures;

    public Terrain() {
        refresh();
        loadTexture();
    }

    public final void refresh() {
        int grassCount, bushCount;
        noise.refresh();
        vertices = noise.getVerticesVector();
        makeTexturesVector();
        terrainModel = new VBOModel(vertices, textures);
        
        // Summer Grass
        grassCount = calcGrassPlacesCount(3, 0);
        grass = new StaticMesh("res/models/Grass.obj", genFoliage(grassCount, 3, 0), 0.5f);
        grass.enableRandomText();
        grass.convertToVBOMany();
        
        // Winter Bush
        bushWinter = new StaticMesh("res/models/BushWinter.obj", genRandomFoliage(100, 1000, 1), 0.5f);
        bushWinter.convertToVBOMany();
        
        // Winter Log A
        logA = new StaticMesh("res/models/logA.obj", genRandomFoliage(30, 1000, 1), 0.5f);
        logA.enableRandomText();
        logA.convertToVBOMany();
        
        // Summer Bush A
        
        bushA = new StaticMesh("res/models/bushA.obj", genRandomFoliage(100, 1000, 0), 0.5f);
        bushA.enableRandomText();
        bushA.convertToVBOMany();
        
        // Stone A
        stoneA = new StaticMesh("res/models/StoneA.obj", genRandomFoliage(30, 1000, 0), 0.5f);
        stoneA.enableRandomText();
        stoneA.convertToVBOMany();
        
        // Stone B
        stoneB = new StaticMesh("res/models/StoneB.obj", genRandomFoliage(200, 1000, 0), 0.5f);
        stoneB.enableRandomText();
        stoneB.convertToVBOMany();
        
        
        // Winter Tree
        pine = new StaticMesh("res/models/PineA.obj", genRandomFoliage(100, 1000, 1), 0.7f);
        pine.convertToVBOMany();
        
        //Summer Tree A 
        tree = new StaticMesh("res/models/TreeA.obj", genRandomFoliage(100, 1000, 0), 1.3f);
        tree.convertToVBOMany();
        
        //Summer Tree B
        treeB = new StaticMesh("res/models/TreeB.obj", genRandomFoliage(10, 1000, 0), 0.4f);
        treeB.convertToVBOMany();
    }

    public Terrain(float scaler, int size, float height, PelenNoise biom) {
        this.biom = biom;
        noise = new PelenNoise(scaler, size, height);
        refresh();
        loadTexture();
    }

    private void loadTexture() {
        try {
            textureLand = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/TerrainTexture.tga")));
            textureGrass = TextureLoader.getTexture("tga", new FileInputStream(new File("res/textures/T_PolygonNature_01.tga")));
            textureBush = TextureLoader.getTexture("png", new FileInputStream(new File("res/textures/BushWinter.png")));
            texturePine = TextureLoader.getTexture("png", new FileInputStream(new File("res/textures/PineA.png")));
            textureTree = TextureLoader.getTexture("png", new FileInputStream(new File("res/textures/TreeA.png")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Skybox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private float[] makeTexturesVector() {
        textures = new float[vertices.length / 3 * 2];
        int flag = 0;
        float height, degree;
        int switcher;

        for (int i = 0; i < vertices.length; i += 3) {
            height = vertices[i + 1];
            degree = Stereometry.getVectorWorldDegree(getNormalByIndex(i));
            if (height < 0.5) {
                //sand
                textures[flag] = Math.abs(vertices[i] / 5) % 0.5f;
                textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f + 0.5f;

            } else {
                if (degree < 0.7f) {
                    if (biom.getVerticesVector()[i + 1] < 0.5f) {
                        //grass
                        textures[flag] = Math.abs(vertices[i] / 5) % 0.5f;
                        textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f;
                    } else {
                        //snow
                        textures[flag] = Math.abs(vertices[i] / 5) % 0.5f + 0.5f;
                        textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f;
                    }
                } else {
                    //cliff
                    textures[flag] = Math.abs(vertices[i] / 5) % 0.5f + 0.5f;
                    textures[flag + 1] = Math.abs(vertices[i + 2] / 5) % 0.5f + 0.5f;
                }
            }
            flag += 2;
        }
        // Selection logic

        return textures;
    }

    private Vector3f getNormalByIndex(int index) {
        int startIndex = index / 9;
        startIndex *= 9;
        Vector3f a = new Vector3f(vertices[startIndex], vertices[startIndex + 1], vertices[startIndex + 2]
        ),
                b = new Vector3f(vertices[startIndex + 3], vertices[startIndex + 4], vertices[startIndex + 5]
                ),
                c = new Vector3f(vertices[startIndex + 6], vertices[startIndex + 7], vertices[startIndex + 8]);
        Vector3f normal = Stereometry.calcNormal(a, b, c);
        return normal;
    }

    private Vector3f[] genFoliage(int count, int biom) {
        Vector3f[] locations = new Vector3f[count];
        int flag = 0;
        for (int i = 0; i < vertices.length; i += 9) {
            if (canPlaceFoliage(i, biom)) {
                locations[flag] = new Vector3f(vertices[i], vertices[i + 1] - 0.2f, vertices[i + 2]);
                flag += 1;
            }
        }
        return locations;
    }

    private Vector3f[] genFoliage(int count, int toSkip, int biom) {
        Vector3f[] locations = new Vector3f[count];
        int flag = 0;
        for (int i = 0; i < vertices.length; i += 9 * (toSkip + 1)) {
            if (canPlaceFoliage(i, biom)) {
                locations[flag] = new Vector3f(vertices[i], vertices[i + 1] - 0.2f, vertices[i + 2]);
                flag += 1;
            }
        }
        return locations;
    }

    private Vector3f[] genRandomFoliage(int count, int maxDepth, int biom) {
        Vector3f[] locations = new Vector3f[count];
        int flag = 0;
        int dl;
        int curDepth = 0;
        while (flag < count) {
            dl = (int) (Math.random() * (vertices.length - 1));
            dl = dl - dl % 9;
            if (canPlaceFoliage(dl, biom) == true) {
                locations[flag] = new Vector3f(vertices[dl], vertices[dl + 1], vertices[dl + 2]);
                flag++;

            }
            curDepth++;
            if (curDepth >= maxDepth) {
                Vector3f[] locationsShort = new Vector3f[flag];
                System.arraycopy(locations, 0, locationsShort, 0, locationsShort.length);
                return locationsShort;
            }
        }
        return locations;
    }

    private int calcGrassPlacesCount(int toSkip, int biom) {
        int i = 0;
        for (int k = 0; k < vertices.length; k += 9 * (toSkip + 1)) {
            if (canPlaceFoliage(k, biom)) {
                i += 1;
            }
        }
        System.out.println(i + " count");
        return i;
    }

    private boolean canPlaceFoliage(int index, int biom) {
        boolean flag = false;
        int startIndex = index;
        int curBiom;
        if (this.biom.getVerticesVector()[index + 1] > 0.5f) {
            curBiom = 1;
        } else {
            curBiom = 0;
        }
        float degree;

        if (curBiom != biom) {
            return false;
        }

        Vector3f a, b, c;

        a = new Vector3f(vertices[startIndex], vertices[startIndex + 1], vertices[startIndex + 2]);
        b = new Vector3f(vertices[startIndex + 3], vertices[startIndex + 4], vertices[startIndex + 5]);
        c = new Vector3f(vertices[startIndex + 6], vertices[startIndex + 7], vertices[startIndex + 8]);

        degree = getVectorWorldDegree(calcNormal(a, b, c));

        if (a.y > 0.5f && degree < 0.7f) {
            flag = true;
        }

        return flag;
    }

    public void draw() {
        textureLand.bind();
        terrainModel.renderTextured();
        
        textureGrass.bind();
        grass.drawVBO();
        logA.drawVBO();
        bushA.drawVBO();
        stoneA.drawVBO();
        stoneB.drawVBO();
        
        textureBush.bind();
        bushWinter.drawVBO();
        
        texturePine.bind();
        pine.drawVBO();
        
        textureTree.bind();
        tree.drawVBO();
        treeB.drawVBO();
    }

}
