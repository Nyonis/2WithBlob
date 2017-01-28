package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {

    private float move = 1f;

    public DemoStage() {
        super("desert.tmx");
    }

    @Override
    public void act() {
        super.act();

        if (input.isPressed(Input.Keys.LEFT)) {
            camera.translate(-32, 0);
        }
        if (input.isPressed(Input.Keys.RIGHT)) {
            camera.translate(32, 0);
        }
    }

    public void create() {
        Gdx.input.setInputProcessor(input);
        playerBlob.setPosition(50, 50);
    }


}
