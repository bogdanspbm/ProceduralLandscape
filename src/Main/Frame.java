package Main;

import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import utility.*;

public class Frame {

    private int width, height; // Размеры окна 
    private Game game; // Клас в котором все что связано с миром
    private static float[] lightPosition = {200.19f, 500.36f, -10.45f, 2f};
    private static float[] lightAmbient = {0.1f, 0.1f, 0.1f, 0.3f};

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
        glClearColor(0.925f, 0.98f, 0.988f, 1f);

        // Закидываем сюда новые модели. Вся отрисовка между Push и Pop Матрицами
        glPushMatrix();
        game.render();
        glPopMatrix();
    }

    private void InitGL() { // Выставляем настройки камеры, FOV, режимы отрисовки и тд.
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45, (float) this.width / (float) this.height, 0.3f, 1000f);

        setUpLighting();
    }

    private static void setUpLighting() {
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        glLightModel(GL_LIGHT_MODEL_AMBIENT, BufferTools.asFlippedFloatBuffer(new float[]{0.05f, 0.05f, 0.05f, 1f}));
        glLight(GL_LIGHT0, GL_POSITION, BufferTools.asFlippedFloatBuffer(lightPosition));
        // glLight(GL_LIGHT0, GL_AMBIENT, BufferTools.asFlippedFloatBuffer(lightAmbient));
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT, GL_DIFFUSE);
    }

}
