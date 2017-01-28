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
    private float animTime = 0;

    public Animation anim;


    public AnimActor(Texture tex) {
        currentFrame = new TextureRegion(tex);
        this.setPosition(currentFrame.getRegionWidth()/-2, currentFrame.getRegionHeight()/-2);
    }


    public AnimActor(Array<? extends TextureRegion> regions) {
        this.currentFrame = regions.get(0);
        this.anim = new Animation(.2f, regions, Animation.PlayMode.LOOP);
        this.setPosition(currentFrame.getRegionWidth()/-2, currentFrame.getRegionHeight()/-2);
    }

    @Override
    public void act(float delta) {
        animTime += Gdx.graphics.getDeltaTime();
        if (anim != null) {
            currentFrame = anim.getKeyFrame(animTime);
            this.setPosition(currentFrame.getRegionWidth()/-2, currentFrame.getRegionHeight()/-2);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, this.getX(), this.getY());
    }
}
