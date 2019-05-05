package Actors;

import java.io.File;
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

public class Actor {

    private Model m = null;

    public Actor(String fileName) {
        try {
            m = OBJLoader.loadTexturedModel(new File(fileName));
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

    public void drawModel() {
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_TRIANGLES);
        glColor3f(1, 1, 1);
        for (Model.Face face : m.getFaces()) {

            //Первая точка
            Vector3f n1 = m.getNormals().get(face.getNormalIndices()[0] - 1);
            glNormal3f(n1.x, n1.y, n1.z);
            if (m.hasTextureCoordinates()) {
                System.out.println("Downloaded Textures");
                Vector2f t1 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[0] - 1);
                glTexCoord2f(t1.x, t1.y);
            }
            Vector3f v1 = m.getVertices().get(face.getVertexIndices()[0] - 1);
            glVertex3f(v1.x, v1.y, v1.z);

            // Вторая точка
            Vector3f n2 = m.getNormals().get(face.getNormalIndices()[1] - 1);
            glNormal3f(n2.x, n2.y, n2.z);
            if (m.hasTextureCoordinates()) {
                Vector2f t2 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[1] - 1);
                glTexCoord2f(t2.x, t2.y);
            }
            Vector3f v2 = m.getVertices().get(face.getVertexIndices()[1] - 1);
            glVertex3f(v2.x, v2.y, v2.z);

            //Третья точка
            Vector3f n3 = m.getNormals().get(face.getNormalIndices()[2] - 1);
            glNormal3f(n3.x, n3.y, n3.z);
            if (m.hasTextureCoordinates()) {
                Vector2f t3 = m.getTextureCoordinates().get(face.getTextureCoordinateIndices()[2] - 1);
                glTexCoord2f(t3.x, t3.y);
            }
            Vector3f v3 = m.getVertices().get(face.getVertexIndices()[2] - 1);
            glVertex3f(v3.x, v3.y, v3.z);
        }
        glEnd();
        glDisable(GL_TEXTURE_2D);
    }
}
