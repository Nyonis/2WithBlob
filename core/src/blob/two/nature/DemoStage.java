package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import helper.MapConvertHelper;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {

    public static int TILE_SIZE = 128;
    private float move = 1f;

    public MyInput input;

    public DemoStage(NatureBlobGame game) {
        super(game, "TestWorld2.tmx");
        MapConvertHelper.mapToCollisionBdy(map, TILE_SIZE, b2dWorld);


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
            
            @Override
            public void onLeftClick(int screenX, int screenY, long duration) {
            	// TODO Auto-generated method stub
            	Vector2 vel = playerBlob.b2dFigureBody.getLinearVelocity();
            	Vector2 pos = playerBlob.b2dFigureBody.getPosition();
            	Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0));
            	
            	Vector2 impulse = new Vector2(v.x, v.y).sub(pos).scl(100000);
            	
            	playerBlob.b2dFigureBody.applyLinearImpulse(impulse, pos, true);
            	System.out.println(vel + ":" + pos + ":" + new Vector2(screenX, h - screenY) + ":" + impulse);
            }
        };

        input.addHandler(Input.Keys.ESCAPE, new MyInput.KeyPressHandler() {
            @Override
            public void press(boolean isDown) {
                Gdx.app.exit();
            }
        });
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
    }
}