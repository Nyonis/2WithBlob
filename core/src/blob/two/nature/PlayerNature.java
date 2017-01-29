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
public class PlayerNature extends Group {

    private static final long ON_TIME = 2000;
    private static final long WAIT_TIME = 3000 + ON_TIME;
    public static int SPEED = 1000;
    private final AnimActor actor;
    private final BodyDef bDef;

    //public AnimActor cloud;
    public Body hitbox;
    FixtureDef fDef;
    boolean spaceAble = true;
    Timer timer;
    float sx = 0, sy = 0;
	public KeyPressHandler mover;

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


    long start;

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!spaceAble && System.currentTimeMillis() > start + ON_TIME) {
            setHittable(false);
        }
        if (!spaceAble && System.currentTimeMillis() > start + WAIT_TIME) {
            spaceAble = true;
        }
    }

    public PlayerNature(Shape shape, World world) {
    	mover = new KeyPressHandler() {
            @Override
            public void press(boolean isDown, int key) {
            	if (key == Input.Keys.SPACE) {
            		if (isDown && spaceAble) {
                        spaceAble = false;
                        start = System.currentTimeMillis();
                        setHittable(true);
                    }
            		return;
            	}
            	if (isDown) {
                	switch(key) {
                	case Input.Keys.W:
                		sy = SPEED;
                		break;
                	case Input.Keys.S:
                		sy = -SPEED;
                		break;
                	case Input.Keys.A:
                		sx = -SPEED;
                		break;
                	case Input.Keys.D:
                		sx = SPEED;
                		break;
                	default: break;
                	}
            	} else {
                	switch(key) {
                	case Input.Keys.W:
                	case Input.Keys.S:
                		sy = 0;
                		break;
                	case Input.Keys.A:
                	case Input.Keys.D:
                		sx = 0;
                		break;
                	default: break;
                	}
            		
            	}
                hitbox.setLinearVelocity(sx, sy);
            }
        };
        MyInput.getInstance().addKeyHandler(mover);

        actor = new AnimActor(new Texture("Cloud.png"));
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

    public void setHittable(boolean hittable) {

        Fixture f = hitbox.getFixtureList().get(0);
        Filter fi = f.getFilterData();


        if (hittable) {
            fi.groupIndex = 1;
            actor.setColor(1, 1, 1, 1);
        } else {
            fi.groupIndex = 2;
            actor.setColor(1f, 1f, 1f, .2f);
        }
        f.setFilterData(fi);
    }
}