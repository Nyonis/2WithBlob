package blob.two.nature;

import blob.two.nature.MyInput.KeyPressHandler;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Created by me on 28.01.17.
 */
public class PlayerNature extends Group implements KeyPressHandler {

    public static int SPEED = 1000;
    private final AnimActor actor;

    //public AnimActor cloud;
    public Body hitbox;
    FixtureDef fDef;
    boolean spaceAble;
    Timer timer;

    public PlayerNature(Shape shape, World world) {

        actor = new AnimActor(new Texture("cloud001_128x256.png"));
        this.addActor(actor);

        BodyDef bDef = new BodyDef();
        bDef.type = BodyType.KinematicBody;

        // TODO positions

        hitbox = world.createBody(bDef);
        fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 0f;
        fDef.friction = 0f;
        timer = new Timer();

        hitbox.createFixture(fDef);
        shape.dispose();
    }

    @Override
    public void press(boolean isDown, int key) {
        System.out.println("key: " + isDown + " " + key);
       if (!isDown){
           hitbox.setLinearVelocity(0,0);
           return;
       }

        Vector2 v = hitbox.getLinearVelocity();
        float x = v.x;
        float y = v.y;
        System.out.println("Veloxity: " + x + " " + y);
        switch (key) {
            case Input.Keys.W:
                hitbox.setLinearVelocity(0, SPEED);
                break;
            case Input.Keys.A:
                hitbox.setLinearVelocity(-SPEED, 0);
                break;
            case Input.Keys.S:
                hitbox.setLinearVelocity(0, -SPEED);
                break;
            case Input.Keys.D:
                hitbox.setLinearVelocity(SPEED, y);
                break;
            case Input.Keys.SPACE:
                if (spaceAble) {
                    Task task1 = new Task() {
                        @Override
                        public void run() {
                            spaceAble = true;
                        }
                    };
                    timer.scheduleTask(task1, 5000);
                    setHittable(true);
                    Task task2 = new Task() {
                        @Override
                        public void run() {
                            setHittable(false);
                        }
                    };
                    timer.scheduleTask(task2, 2000);
                }
        }
        System.out.println("Veloxity: " + x + " " + y);
    }

    public void setHittable(boolean hittable) {
        if (hittable) {
            fDef.density = 1f;
            fDef.friction = .4f;
        } else {
            fDef.density = 0f;
            fDef.friction = 0f;
        }
        hitbox.createFixture(fDef);
    }
}