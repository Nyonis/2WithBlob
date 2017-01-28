package blob.two.nature.desktop;

import blob.two.nature.NatureBlobGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        //config.width = 800;
        new LwjglApplication(new NatureBlobGame(), config);
    }
}
