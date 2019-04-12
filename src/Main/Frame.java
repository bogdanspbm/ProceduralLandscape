package Main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;

public class Frame {

    private int width, height; // Размеры окна 
    private Game game; // Клас в котором все что связано с миром

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

        glPopMatrix();
    }

    private void InitGL() { // Выставляем настройки камеры, FOV, режимы отрисовки и тд.
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45, (float) this.width / (float) this.height, 0.3f, 1000f);
        glMatrixMode(GL_MODELVIEW);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
    }

}
