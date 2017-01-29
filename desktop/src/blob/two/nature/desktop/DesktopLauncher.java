package blob.two.nature.desktop;

import blob.two.nature.NatureBlobGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        Dimension gd = Toolkit.getDefaultToolkit().getScreenSize();


        config.width = (int) gd.getWidth()/2;
        config.height = (int) gd.getHeight()/2;
        new LwjglApplication(new NatureBlobGame(), config);
    }
}