package Actors;

import Utils3D.Stereometry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class StaticMesh {

    private Model[] m;
    Texture texture;
    private boolean hasText = false;
    private int count = 0;
    private int maxCount = 10000;
    private Vector3f[] copies = new Vector3f[maxCount];
    private float scale = 1f;
    private int k = 0;
    private float[] rotations = new float[maxCount];
    private float[] vertices, textures, normals;
    private VBOModel optModel;
    private Vector3f location = new Vector3f(0f, 0f, 0f);

    public StaticMesh(String[] fileName, String fileName2) {
        m = new Model[fileName.length];
        for (int i = 0; i < fileName.length; i++) {
            try {
                m[i] = OBJLoader.loadTexturedModel(new File(fileName[i]));
                convertToVBO(m[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            }
        }
        try {
            texture = TextureLoader.getTexture("tga", new FileInputStream(new File(fileName2)));
            hasText = true;
        } catch (IOException ex) {
            System.out.print("Can't load texture");
            ex.printStackTrace();
        }
    }

    public StaticMesh(String fileName) {
        m = new Model[1];
        try {
            m[0] = OBJLoader.loadTexturedModel(new File(fileName));
            convertToVBO(m[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
    }

    public StaticMesh(String fileName, Vector3f location) {
        m = new Model[1];
        try {
            this.location = location;
            m[0] = OBJLoader.loadTexturedModel(new File(fileName));
            convertToVBO(m[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
    }

    private void convertToVBO(Model m) {
        vertices = new float[m.getFaces().size() * 3 * 3];
        normals = new float[m.getFaces().size() * 3 * 3];
        textures = new float[m.getFaces().size() * 3 * 2];
        int flagV = 0, flagT = 0;

        for (Model.Face face : m.getFaces()) {

            Vector3f n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
            normals[flagV] = n1.x;
            normals[flagV + 1] = n1.y;
            normals[flagV + 2] = n1.z;
            if (m.hasTextureCoordinates()) {
                Vector2f t1 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1);
                textures[flagT] = t1.x;
                textures[flagT + 1] = t1.y;
                flagT += 2;

            }
            Vector3f v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
            vertices[flagV] = v1.x + location.x;
            vertices[flagV + 1] = v1.y + location.y;
            vertices[flagV + 2] = v1.z + location.z;
            flagV += 3;

            Vector3f n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
            normals[flagV] = n2.x;
            normals[flagV + 1] = n2.y;
            normals[flagV + 2] = n2.z;
            if (m.hasTextureCoordinates()) {
                Vector2f t2 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                textures[flagT] = t2.x;
                textures[flagT + 1] = t2.y;
                flagT += 2;
            }
            Vector3f v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
            vertices[flagV] = v2.x + location.x;
            vertices[flagV + 1] = v2.y + location.y;
            vertices[flagV + 2] = v2.z + location.z;
            flagV += 3;

            Vector3f n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
            normals[flagV] = n3.x;
            normals[flagV + 1] = n3.y;
            normals[flagV + 2] = n3.z;

            if (m.hasTextureCoordinates()) {
                Vector2f t3 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                textures[flagT] = t3.x;
                textures[flagT + 1] = t3.y;
                flagT += 2;
            }
            Vector3f v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
            vertices[flagV] = v3.x + location.x;
            vertices[flagV + 1] = v3.y + location.y;
            vertices[flagV + 2] = v3.z + location.z;
            flagV += 3;
        }
        optModel = new VBOModel(vertices, normals, textures);
    }

    public void drawVBO() {
        optModel.render();
    }

    public StaticMesh(String[] fileName) {
        m = new Model[fileName.length];
        for (int i = 0; i < fileName.length; i++) {
            try {
                m[i] = OBJLoader.loadTexturedModel(new File(fileName[i]));
                convertToVBO(m[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            }
        }
    }

    public void addCopy(float x, float y, float z) {
        if (count < maxCount) {
            copies[count] = new Vector3f(x, y, z);
            rotations[count] = 0;
            count += 1;
        }
    }

    public void addCopy(float x, float y, float z, float r) {
        if (count < maxCount) {
            copies[count] = new Vector3f(x, y, z);
            rotations[count] = r;
            count += 1;
        }
    }

    public void setScale(float sc) {
        scale = sc;
    }

    public void clearCopies() {
        copies = new Vector3f[maxCount];
        rotations = new float[maxCount];
        count = 0;
    }

    public void drawModel() {
        for (int i = 0; i < count; i++) {

            glPushMatrix();
            glTranslated(copies[i].x, copies[i].y, copies[i].z);
            glRotatef(rotations[i], 0, 1, 0);

            if (hasText == true) {
                texture.bind();
                glEnable(GL_TEXTURE_2D);
            }
            glBegin(GL_TRIANGLES);
            glColor4f(1, 1, 1, 1);

            for (Model.Face face : m[k].getFaces()) {

                //Первая точка
                Vector3f n1 = m[k].getNormals().get(face.getNormalIndices()[0] - 1);
                glNormal3f(n1.x, n1.y, n1.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t1 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1);
                    glTexCoord2f(t1.x, t1.y);
                }
                Vector3f v1 = m[k].getVertices().get(face.getVertexIndices()[0] - 1);
                glVertex3f(v1.x * scale, v1.y * scale, v1.z * scale);

                // Вторая точка
                Vector3f n2 = m[k].getNormals().get(face.getNormalIndices()[1] - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t2 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                    glTexCoord2f(t2.x, t2.y);
                }
                Vector3f v2 = m[k].getVertices().get(face.getVertexIndices()[1] - 1);
                glVertex3f(v2.x * scale, v2.y * scale, v2.z * scale);

                //Третья точка
                Vector3f n3 = m[k].getNormals().get(face.getNormalIndices()[2] - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t3 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                    glTexCoord2f(t3.x, t3.y);
                }
                Vector3f v3 = m[k].getVertices().get(face.getVertexIndices()[2] - 1);
                glVertex3f(v3.x * scale, v3.y * scale, v3.z * scale);
            }
            glEnd();
            glDisable(GL_TEXTURE_2D);
            glTranslated(-copies[i].x, -copies[i].y, -copies[i].z);
            glPopMatrix();
        }

    }

    public void drawModel(Vector3f camLocation) {

        for (int i = 0; i < count; i++) {

            glPushMatrix();
            glTranslated(copies[i].x, copies[i].y, copies[i].z);
            glRotatef(rotations[i], 0, 1, 0);

            if (hasText == true) {
                texture.bind();
                glEnable(GL_TEXTURE_2D);
            }
            glColor4f(1, 1, 1, 1);
            glBegin(GL_TRIANGLES);

            for (Model.Face face : m[k].getFaces()) {

                //Первая точка
                Vector3f n1 = m[k].getNormals().get(face.getNormalIndices()[0] - 1);
                glNormal3f(n1.x, n1.y, n1.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t1 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1);
                    glTexCoord2f(t1.x, t1.y);
                }
                Vector3f v1 = m[k].getVertices().get(face.getVertexIndices()[0] - 1);
                glVertex3f(v1.x * scale, v1.y * scale, v1.z * scale);

                // Вторая точка
                Vector3f n2 = m[k].getNormals().get(face.getNormalIndices()[1] - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t2 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                    glTexCoord2f(t2.x, t2.y);
                }
                Vector3f v2 = m[k].getVertices().get(face.getVertexIndices()[1] - 1);
                glVertex3f(v2.x * scale, v2.y * scale, v2.z * scale);

                //Третья точка
                Vector3f n3 = m[k].getNormals().get(face.getNormalIndices()[2] - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t3 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                    glTexCoord2f(t3.x, t3.y);
                }
                Vector3f v3 = m[k].getVertices().get(face.getVertexIndices()[2] - 1);
                glVertex3f(v3.x * scale, v3.y * scale, v3.z * scale);
            }
            glEnd();
            glDisable(GL_TEXTURE_2D);
            glTranslated(-copies[i].x, -copies[i].y, -copies[i].z);
            glPopMatrix();
        }

    }
}
