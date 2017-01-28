package blob.two.nature;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by me on 28.01.17.
 */
public class AnimActor extends Actor {

    private TextureRegion currentFrame;
    private float stateTime;

    public Animation anim;

    public AnimActor(float stateTime) {
        this.stateTime = stateTime;
        currentFrame = new TextureRegion(new Texture("badlogic.jpg"));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = anim.getKeyFrame(stateTime);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(currentFrame, this.getX(), this.getY());

    }
}
