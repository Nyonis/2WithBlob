package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by me on 28.01.17.
 */
public class AnimActor extends Actor {

    private TextureRegion currentFrame;
    private float animTime;

    public Animation anim;

    public AnimActor() {
        this.animTime = 0;
        currentFrame = new TextureRegion(new Texture("Blob002.png"));
    }

    public AnimActor(Array<? extends TextureRegion> regions) {
        this.currentFrame = regions.get(0);
        this.anim = new Animation(.2f, regions, Animation.PlayMode.LOOP);
    }

    @Override
    public void act(float delta) {
        animTime += Gdx.graphics.getDeltaTime();
        if (anim != null) {
            currentFrame = anim.getKeyFrame(animTime);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, this.getX(), this.getY());
    }
}
