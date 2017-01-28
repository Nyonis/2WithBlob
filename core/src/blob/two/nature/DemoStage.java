package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {


    public DemoStage() {
        super("desert.tmx");
    }

    public void create() {
        Gdx.input.setInputProcessor(this);

        playerBlob.setPosition(50,50);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT)
            camera.translate(-32, 0);
        if (keycode == Input.Keys.RIGHT)
            camera.translate(32, 0);
        if (keycode == Input.Keys.UP)
            camera.translate(0, -32);
        if (keycode == Input.Keys.DOWN)
            camera.translate(0, 32);
        return false;
    }

}
