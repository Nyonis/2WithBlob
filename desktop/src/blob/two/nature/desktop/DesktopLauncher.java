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
        //config.fullscreen = true;
        Dimension gd = Toolkit.getDefaultToolkit().getScreenSize();
        config.width = (int) gd.getWidth()/2;
        config.height = (int) gd.getHeight()/2;
        new LwjglApplication(new NatureBlobGame(), config);
    }
}