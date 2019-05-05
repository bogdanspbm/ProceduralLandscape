package Actors;

import Actors.Model.Material;
import org.lwjgl.util.vector.Vector3f;

public class Face {

    public Vector3f vertex = new Vector3f();
    public Vector3f normal = new Vector3f();
    public Vector3f textures = new Vector3f();
    public Material mat = new Material();

    public Face(Vector3f vertex, Vector3f normal, Vector3f textures, Material material) {
        this.normal = normal;
        this.vertex = vertex;
        this.textures = textures;
        this.mat = material;
    }
}
