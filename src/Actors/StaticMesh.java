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
    private float lodRange = 50f;

    public StaticMesh(String[] fileName, String fileName2) {
        m = new Model[fileName.length];
        for (int i = 0; i < fileName.length; i++) {
            try {
                m[i] = OBJLoader.loadTexturedModel(new File(fileName[i]));
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

    public void setLodRange(float range) {
        lodRange = range;
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

            texture.bind();
            glEnable(GL_TEXTURE_2D);
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

            texture.bind();
            glColor4f(1, 1, 1, 1);
            glEnable(GL_TEXTURE_2D);
            glBegin(GL_TRIANGLES);

            if (Stereometry.getVectorLenght(camLocation, copies[i]) < lodRange) {
                k = 0;
            } else {
                k = m.length - 1;
            }
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
