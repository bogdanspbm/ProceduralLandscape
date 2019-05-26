package Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f pos; // Координаты
    private Vector2f rotation; // Поворот
    private final int SPEED = 30; // Cкорость полета камеры

    public Camera(float x, float y, float z) {
        pos = new Vector3f(x, y, z); // Ставлю начальные коорды
        rotation = new Vector2f(0, 0); // И поворот камеры
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2); // Выставляю курсов в центр экрана
    }

    public void Update(float delta) {
        float dx = Mouse.getX() - Display.getWidth() / 2; // Cчитаю длину перемещения мыши по x 
        float dy = Mouse.getY() - Display.getHeight() / 2; // По y

        rotation.x += dx / 5f; // Меняю соотвественно повороты
        rotation.y -= dy / 5f;

        // Фиксирую поворот по вертикали чтобы сильно не кружилась голова
        if (rotation.y > 90) {
            rotation.y = 90;
        }
        if (rotation.y < -90) {
            rotation.y = -90;
        }

        //Возвращаю курсор в центр экрана
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(true);

        // Управление с клавиатру
        // Ничего сложного, джаст аналитическая геометрия
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            pos.x += Math.cos(Math.toRadians(rotation.y)) * Math.cos(Math.toRadians(rotation.x - 90)) * SPEED * delta;
            pos.z += Math.cos(Math.toRadians(rotation.y)) * Math.sin(Math.toRadians(rotation.x - 90)) * SPEED * delta;
            pos.y -= Math.sin(Math.toRadians(rotation.y)) * SPEED * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            pos.x -= Math.cos(Math.toRadians(rotation.y)) * Math.cos(Math.toRadians(rotation.x - 90)) * SPEED * delta;
            pos.z -= Math.cos(Math.toRadians(rotation.y)) * Math.sin(Math.toRadians(rotation.x - 90)) * SPEED * delta;
            pos.y += Math.sin(Math.toRadians(rotation.y)) * SPEED * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            pos.x -= Math.cos(Math.toRadians(rotation.x)) * SPEED * delta;
            pos.z -= Math.sin(Math.toRadians(rotation.x)) * SPEED * delta;

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            pos.x += Math.cos(Math.toRadians(rotation.x)) * SPEED * delta;
            pos.z += Math.sin(Math.toRadians(rotation.x)) * SPEED * delta;
        }

    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector2f getRotation() {
        return rotation;
    }

}
