package blob.two.nature.desktop;

import blob.two.nature.NatureBlobGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = true;
        Dimension gd = Toolkit.getDefaultToolkit().getScreenSize();


        config.width = (int) gd.getWidth();
        config.height = (int) gd.getHeight();
        new LwjglApplication(new NatureBlobGame(), config);
    }
}