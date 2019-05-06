package Actors;

import Utils3D.Stereometry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
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

public class StaticMesh {

    private Model[] m;
    Texture texture;
    private boolean hasText = false;
    private int count = 0;
    private int maxCount = 10000;
    private Vector3f[] copies = new Vector3f[maxCount];
    private float scale = 1f;
    private int k = 0;
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
            count += 1;
        }
    }

    public void setScale(float sc) {
        scale = sc;
    }

    public void setLodRange(float range) {
        lodRange = range;
    }

    public void drawModel() {
        texture.bind();
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        glColor3f(1, 1, 1);
        for (int i = 0; i < count; i++) {
            for (Model.Face face : m[k].getFaces()) {

                //Первая точка
                Vector3f n1 = m[k].getNormals().get(face.getNormalIndices()[0] - 1);
                glNormal3f(n1.x, n1.y, n1.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t1 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1);
                    glTexCoord2f(t1.x, t1.y);
                }
                Vector3f v1 = m[k].getVertices().get(face.getVertexIndices()[0] - 1);
                glVertex3f(v1.x * scale + copies[i].x, v1.y * scale + copies[i].y, v1.z * scale + copies[i].z);

                // Вторая точка
                Vector3f n2 = m[k].getNormals().get(face.getNormalIndices()[1] - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t2 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                    glTexCoord2f(t2.x, t2.y);
                }
                Vector3f v2 = m[k].getVertices().get(face.getVertexIndices()[1] - 1);
                glVertex3f(v2.x * scale + copies[i].x, v2.y * scale + copies[i].y, v2.z * scale + copies[i].z);

                //Третья точка
                Vector3f n3 = m[k].getNormals().get(face.getNormalIndices()[2] - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t3 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                    glTexCoord2f(t3.x, t3.y);
                }
                Vector3f v3 = m[k].getVertices().get(face.getVertexIndices()[2] - 1);
                glVertex3f(v3.x * scale + copies[i].x, v3.y * scale + copies[i].y, v3.z * scale + copies[i].z);
            }
        }
        glEnd();
        glDisable(GL_TEXTURE_2D);

    }

    public void drawModel(Vector3f camLocation) {
        texture.bind();
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        glColor3f(1, 1, 1);
        for (int i = 0; i < count; i++) {
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
                glVertex3f(v1.x * scale + copies[i].x, v1.y * scale + copies[i].y, v1.z * scale + copies[i].z);

                // Вторая точка
                Vector3f n2 = m[k].getNormals().get(face.getNormalIndices()[1] - 1);
                glNormal3f(n2.x, n2.y, n2.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t2 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                    glTexCoord2f(t2.x, t2.y);
                }
                Vector3f v2 = m[k].getVertices().get(face.getVertexIndices()[1] - 1);
                glVertex3f(v2.x * scale + copies[i].x, v2.y * scale + copies[i].y, v2.z * scale + copies[i].z);

                //Третья точка
                Vector3f n3 = m[k].getNormals().get(face.getNormalIndices()[2] - 1);
                glNormal3f(n3.x, n3.y, n3.z);
                if (m[k].hasTextureCoordinates() && hasText) {
                    Vector2f t3 = m[k].getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                    glTexCoord2f(t3.x, t3.y);
                }
                Vector3f v3 = m[k].getVertices().get(face.getVertexIndices()[2] - 1);
                glVertex3f(v3.x * scale + copies[i].x, v3.y * scale + copies[i].y, v3.z * scale + copies[i].z);
            }
        }
        glEnd();
        glDisable(GL_TEXTURE_2D);

    }
}
