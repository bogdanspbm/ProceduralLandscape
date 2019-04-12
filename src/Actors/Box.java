package Actors;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.lwjgl.*;

public class Box {

    public Box(int x, int y, int z) {
        glBegin(GL_TRIANGLES);
        glVertex3f(x - 200, y - 200, z - 200);
        glVertex3f(x - 200, y + 200, z - 200);
        glVertex3f(x + 200, y + 200, z + 200);
        glVertex3f(x + 200, y - 200, z + 200);
        glEnd();
    }

}
