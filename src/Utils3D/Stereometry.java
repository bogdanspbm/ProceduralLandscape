package Utils3D;

import org.lwjgl.util.vector.Vector3f;

public class Stereometry {

    public static Vector3f calcNormal(Vector3f s1, Vector3f s2, Vector3f s3) {
        Vector3f a = new Vector3f(s2.x - s1.x, s2.y - s1.y, s2.z - s1.z);
        Vector3f b = new Vector3f(s3.x - s2.x, s3.y - s2.y, s3.z - s2.z);
        Vector3f normal = new Vector3f(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
        float length = (float) Math.pow(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z, 0.5f);
        Vector3f result = new Vector3f(normal.x / length, normal.y / length, normal.z / length);
        return result;
    }

    public static float getVectorWorldDegree(Vector3f a) {
        float result = 0;
        result = (float) Math.acos(a.y);
        return result;
    }

    public static float getVectorLenght(Vector3f a, Vector3f b) {
        return (float) (Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z)));
    }

}
