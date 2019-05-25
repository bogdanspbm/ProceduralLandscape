package Actors;

import Utils3D.Stereometry;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import org.lwjgl.util.vector.Vector3f;

public class VBOModel {

    private int draw_count;
    private int v_id;
    private int t_id;
    private int n_id;

    public VBOModel(float[] vertices, float[] text_coords) {
        draw_count = vertices.length;
        v_id = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(text_coords), GL_STATIC_DRAW);

        n_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, n_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(generateNormals(vertices)), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    public VBOModel(float[] vertices, float[] normals, float[] text_coords) {
        draw_count = vertices.length;
        v_id = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(text_coords), GL_STATIC_DRAW);

        n_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, n_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(normals), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, n_id);
        glNormalPointer(GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, draw_count);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
    }

    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private float[] generateNormals(float[] vertices) {
        float[] normals = new float[vertices.length];

        for (int i = 0; i < vertices.length; i += 9) {

            Vector3f a = new Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]
            ),
                    b = new Vector3f(vertices[i + 3], vertices[i + 4], vertices[i + 5]
                    ),
                    c = new Vector3f(vertices[i + 6], vertices[i + 7], vertices[i + 8]);

            Vector3f normal = Stereometry.calcNormal(a, b, c);

            normals[i] = normal.x;
            normals[i + 1] = normal.y;
            normals[i + 2] = normal.z;
            normals[i + 3] = normal.x;
            normals[i + 4] = normal.y;
            normals[i + 5] = normal.z;
            normals[i + 6] = normal.x;
            normals[i + 7] = normal.y;
            normals[i + 8] = normal.z;

        }
        return normals;
    }

    private void disp(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

}
