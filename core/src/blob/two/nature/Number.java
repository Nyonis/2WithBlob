package blob.two.nature;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by me on 29.01.17.
 */
public class Number extends Image {

    private static Texture texture;

    public Number(int digit) {
        super(getRegion(digit));
        float s = .5f;
        setScale(s, s);
    }

    public void change(int digit){
        int x = digit %4;
        int y = digit / 4;
        if (digit > 9){
            x = 2;
            y = 2;
        }
        ((TextureRegionDrawable)this.getDrawable()).setRegion(new TextureRegion(new Texture("zahlen.png"), x * 128, y * 128, 128, 128));

    }

    public static TextureRegion getRegion(int digit){

        int x = digit %4;
        int y = digit / 4;
        if (digit > 9){
            x = 2;
            y = 2;
        }
        return new TextureRegion(new Texture("zahlen.png"), x * 128, y * 128, 128, 128);
    }


}
