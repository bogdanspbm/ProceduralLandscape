/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Landscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author crewd
 */
public class SkyBox {

    Texture skytext;

    public SkyBox(String filePath) {
        try {
            skytext = TextureLoader.getTexture("PNG", new FileInputStream(new File(filePath)));
        } catch (IOException ex) {
            System.out.print("Can't load texture");
            Logger.getLogger(SkyBox.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    public void drawSkyBox() {

        /// skytext.bind();
        glBegin(GL_QUADS);
        glColor3f(0.5f, 0.6f, 0.7f);

        glNormal3f(0, 0, 1);

        //glTexCoord2f(1,0);
        glVertex3f(10000, -10000, -10000);
        //glTexCoord2f(1,1);
        glVertex3f(10000, 10000, -10000);
        // glTexCoord2f(0,1);
        glVertex3f(-10000, 10000, -10000);
        // glTexCoord2f(0,0);
        glVertex3f(-1000, -10000, -10000);

        glNormal3f(0, 0, -1);
        glVertex3f(-10000, -10000, 10000);
        glVertex3f(-10000, 10000, 10000);
        glVertex3f(10000, 10000, 10000);
        glVertex3f(10000, -10000, 10000);

        glNormal3f(0, -1, 0);
        glVertex3f(10000, 10000, -10000);
        glVertex3f(10000, 10000, 10000);
        glVertex3f(-10000, 10000, 10000);
        glVertex3f(-10000, 10000, -10000);

        glNormal3f(0, 1, 0);
        glVertex3f(-10000, -10000, -10000);
        glVertex3f(-10000, -10000, 10000);
        glVertex3f(10000, -10000, 10000);
        glVertex3f(10000, -10000, -10000);

        glNormal3f(1, 0, 0);
        glVertex3f(-10000, 10000, -10000);
        glVertex3f(-10000, 10000, 10000);
        glVertex3f(-10000, -10000, 10000);
        glVertex3f(-10000, -10000, -10000);

        glNormal3f(-1, 0, 0);
        glVertex3f(10000, -10000, -10000);
        glVertex3f(10000, -10000, 10000);
        glVertex3f(10000, 10000, 10000);
        glVertex3f(10000, 10000, -10000);

        glEnd();
    }

}
