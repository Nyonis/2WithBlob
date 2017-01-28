package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {

    public static int TILE_SIZE = 1024;
    private float move = 1f;

    public MyInput input;

    public DemoStage() {
        super("TestWorld.tmx");

        input = new MyInput() {
            @Override
            public boolean keyUp(int keycode) {
                super.keyUp(keycode);
                int dx = 0, dy = 0;
                if (keycode == Input.Keys.LEFT) {
                    dx = -TILE_SIZE;
                }
                if (keycode == Input.Keys.RIGHT) {
                    dx = TILE_SIZE;
                }
                if (keycode == Input.Keys.UP) {
                    dy = TILE_SIZE;
                }
                if (keycode == Input.Keys.DOWN) {
                    dy = -TILE_SIZE;
                }

                playerBlob.setPosition(playerBlob.getX() + dx, playerBlob.getY() + dy);
                camera.translate(dx, dy);

                return false;
            }
        };

        Gdx.input.setInputProcessor(input);

    }


    @Override
    public void act() {
        super.act();

        if (input.isPressed(Input.Keys.LEFT)) {

        }
        if (input.isPressed(Input.Keys.RIGHT)) {

        }
    }

    public void create() {
        Gdx.input.setInputProcessor(input);
        playerBlob.setPosition(0, 3 * TILE_SIZE);
    }
}