package blob.two.nature;

import com.badlogic.gdx.graphics.Texture;
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

    public void changeNumber(int digit){
        //((TextureRegionDrawable)this.getDrawable());
    }

    private static TextureRegion getRegion(int digit) {
        if(texture == null)
            texture = new Texture("zahlen.png");
        int x = digit %4;
        int y = digit / 4;
        return new TextureRegion(texture, x, y, 512/4, 384/3 );
    }
}
