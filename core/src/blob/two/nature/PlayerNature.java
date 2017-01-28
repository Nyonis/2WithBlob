package blob.two.nature;

import blob.two.nature.MyInput.KeyPressHandler;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by me on 28.01.17.
 */
public class PlayerNature extends Group implements KeyPressHandler {

    private static final long WAIT = 3000;
    public static int SPEED = 1000;
    private final AnimActor actor;
    private final World world;
    private final BodyDef bDef;

    //public AnimActor cloud;
    public Body hitbox;
    FixtureDef fDef;
    boolean spaceAble = true;
    Timer timer;
    float sx = 0, sy = 0;

    public KeyPressHandler left = new KeyPressHandler() {
        @Override
        public void press(boolean isDown, int key) {
            sx = -SPEED;
            if (!isDown) {
                sx = 0;
            }
            hitbox.setLinearVelocity(sx, sy);
        }
    };

    public KeyPressHandler right = new KeyPressHandler() {
        @Override
        public void press(boolean isDown, int key) {
            sx = SPEED;
            if (!isDown) {
                sx = 0;
            }
            hitbox.setLinearVelocity(sx, sy);
        }
    };


    public KeyPressHandler down = new KeyPressHandler() {
        @Override
        public void press(boolean isDown, int key) {
            sy = -SPEED;
            if (!isDown) {
                sy = 0;
            }
            hitbox.setLinearVelocity(sx, sy);
        }
    };


    public KeyPressHandler up = new KeyPressHandler() {
        @Override
        public void press(boolean isDown, int key) {
            sy = SPEED;
            if (!isDown) {
                sy = 0;
            }
            hitbox.setLinearVelocity(sx, sy);
        }
    };

    long start;

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!spaceAble && System.currentTimeMillis() > start + WAIT) {
            spaceAble = true;
            setHittable(false);
        }
    }

    public PlayerNature(Shape shape, World world) {
        this.world = world;
        actor = new AnimActor(new Texture("cloud001_128x256.png"));
        this.addActor(actor);

        bDef = new BodyDef();
        bDef.type = BodyType.KinematicBody;

        // TODO positions

        hitbox = world.createBody(bDef);
        fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1f;
        fDef.friction = 0f;
        fDef.filter.categoryBits = 1;
        fDef.filter.maskBits = 2;
        fDef.filter.groupIndex = 1;
        timer = new Timer();

        hitbox.createFixture(fDef);
        setHittable(false);
        shape.dispose();
    }

    @Override
    public void press(boolean isDown, int key) {
        System.out.println("key: " + isDown + " " + key);

        switch (key) {

            case Input.Keys.SPACE:

                if (spaceAble) {
                    spaceAble = false;

                    start = System.currentTimeMillis();
                    setHittable(true);
                }
        }
    }

    public void setHittable(boolean hittable) {
        System.out.println(fDef.filter.groupIndex);
        Fixture f = hitbox.getFixtureList().get(0);
        Filter fi = f.getFilterData();

        if (hittable) {
            fi.groupIndex = 1;
            actor.setColor(1, 1, 1, 1);
        } else {
            fi.groupIndex = 2;
            actor.setColor(0.2f, 0.2f, 0.2f, .2f);
        }
        f.setFilterData(fi);

    }
}