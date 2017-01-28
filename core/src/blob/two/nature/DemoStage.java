package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

import helper.MapConvertHelper;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {

    public static int TILE_SIZE = 128;
    private final Sound sound;
    private float move = 1f;

    public MyInput input;

    public void makeEatMes() {
        PolygonShape s = new PolygonShape();
        s.setAsBox(40, 40);
        Item a = new Item(b2dWorld, s, new Texture("kugel.png"));
        a.setPos(300, 500);
        addActor(a);
    }

    public DemoStage(NatureBlobGame game) {
        super(game, "TestWorld2.tmx");
        MapConvertHelper.mapToCollisionBdy(map, "WallObjects", TILE_SIZE, b2dWorld, null);
        MapConvertHelper.mapToCollisionBdy(map, "SpikeObjects", TILE_SIZE, b2dWorld, GameStage.ID_DIE);


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
                if (keycode == Input.Keys.SPACE) {
                	playerBlob.b2dArmProjectile.setType(BodyType.StaticBody);
                	playerBlob.locked = true;
                }

                playerBlob.setPosition(playerBlob.getX() + dx, playerBlob.getY() + dy);
                camera.translate(dx, dy);

                return false;
            }

            @Override
            protected void onLeftDown(int screenX, int screenY) {
            	// TODO Auto-generated method stub
            	Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0));

            	playerBlob.activateExtendArm(new Vector2(v.x, v.y));
            }
            
            @Override
            public void onLeftClick(int screenX, int screenY, long duration) {
            	playerBlob.deactivateExtendArm();
            }
            
            @Override
            public void onLeftDragged(int screenX, int screenY) {
            	Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0));
            	
            	playerBlob.updateArmTarget(new Vector2(v.x, v.y));
            }

        };

        input.addHandler(Input.Keys.ESCAPE, new MyInput.KeyPressHandler() {
            @Override
            public void press(boolean isDown, int key) {
                Gdx.app.exit();
            }
        });
        input.addHandler(Input.Keys.A, playerNature.left);
        input.addHandler(Input.Keys.D, playerNature.right);
        input.addHandler(Input.Keys.S, playerNature.down);
        input.addHandler(Input.Keys.W, playerNature.up);
        input.addHandler(Input.Keys.SPACE, playerNature);

        Gdx.input.setInputProcessor(input);

        makeEatMes();

        final FileHandle soundFile = Gdx.files.internal("8bit_music_fadeout.mp3");
        sound = Gdx.audio.newSound(soundFile);
        sound.setLooping(0, true);
        sound.play(0.3f);
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