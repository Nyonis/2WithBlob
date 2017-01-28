package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {


    public DemoStage() {
        super("TestWorld.tmx");
    }

    public void create() {
        Gdx.input.setInputProcessor(this);

        playerBlob.setPosition(0, 3*1024);
    }

    @Override
    public boolean keyUp(int keycode) {
    	int distance = 1024;
        if (keycode == Input.Keys.LEFT) {
        	playerBlob.setPosition(playerBlob.getX()-distance, playerBlob.getY());
            camera.translate(-distance, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
        	playerBlob.setPosition(playerBlob.getX()+distance, playerBlob.getY());
            camera.translate(distance, 0);
        }
        if (keycode == Input.Keys.UP) {
        	playerBlob.setPosition(playerBlob.getX(), playerBlob.getY()+distance);
            camera.translate(0, distance);
        }
        if (keycode == Input.Keys.DOWN) {
        	playerBlob.setPosition(playerBlob.getX(), playerBlob.getY()-distance);
            camera.translate(0, -distance);
        }
        return false;
    }

}