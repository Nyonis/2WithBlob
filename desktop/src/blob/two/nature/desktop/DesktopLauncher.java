package blob.two.nature.desktop;

import blob.two.nature.NatureBlobGame;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
    	startDemoLevel();
    }

    public static void startDemoLevel() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        Dimension gd = Toolkit.getDefaultToolkit().getScreenSize();
        config.width = (int) ((int) gd.getWidth()/1.5f);
        config.height = (int) ((int) gd.getHeight()/1.5f);
        new LwjglApplication(new NatureBlobGame(), config);
    }
}