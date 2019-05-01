package Main;

// Мы рады пришельцам с других проектов
// Не ругайте за русский , у меня в шк было 2
// Мышку после включения не двигай, до отрисовки моделей, а то не найдешь потом ее в этой черной дыре
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;

public class Main {

    public static void Window() {

        // Создаем окно
        try {
            Display.setDisplayMode(new DisplayMode(800, 800)); // Разрешение
            Display.setTitle("Engine"); // Имя окна
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        //Cоздаем класс в котором будем вызывать все обновляющие картинку функции
        Frame f = new Frame();

        //Создаем переменную для вычисления времени между кадрами
        long lastFrame = System.currentTimeMillis();

        while (!Display.isCloseRequested()) {

            //Считаем время между кадрами
            long thisFrame = System.currentTimeMillis();
            float delta = (thisFrame - lastFrame) / 1000f;
            lastFrame = thisFrame;

            //Запускаем две функции обновления картинки
            f.update(delta); // Учитывается время, для перемещений и тд.
            f.render(); // Статика и тд.

            // Выход на ESC
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                break;
            }

            // Частота кадров 1500 фпс как в майнкрафте
            Display.update();
            Display.sync(60);
        }

        //Удалить окно
        Display.destroy();

    }

    public static void main(String[] args) {
        //Мэйн функция
        Window();
    }
}
