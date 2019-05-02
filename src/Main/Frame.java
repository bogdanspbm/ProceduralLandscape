package Main;

import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import utility.*;

public class Frame {

    private int width, height; // Размеры окна 
    private Game game; // Клас в котором все что связано с миром
    private static float[] lightPosition = {-2.19f, 1.36f, 11.45f, 0.5f};

    public Frame() { // Конструктор
        this.width = Display.getWidth();
        this.height = Display.getHeight();
        game = new Game();
        InitGL();
    }

    public void update(float delta) {
        game.update(delta); // Вызываем обновление у класса игры
    }

    public void render() { // Событие обработки кадров
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Чистим буферы из памяти карточки

        // Закидываем сюда новые модели. Вся отрисовка между Push и Pop Матрицами
        glPushMatrix();

        game.render();
        //glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(lightPosition));
       // lightPosition = new float[]{game.cam.getRotation().x, game.cam.getRotation().y, 1.0f, 1};

        glPopMatrix();
    }

    private void InitGL() { // Выставляем настройки камеры, FOV, режимы отрисовки и тд.
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45, (float) this.width / (float) this.height, 0.3f, 1000f);
        //glMatrixMode(GL_MODELVIEW);

        setUpLighting();

    }

    private static void setUpLighting() {
        glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float[]{0.05f, 0.05f, 0.05f, 1f}));
        glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(lightPosition));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
    }

}
