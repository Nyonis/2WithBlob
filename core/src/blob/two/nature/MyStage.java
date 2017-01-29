package blob.two.nature;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by me on 29.01.17.
 */
public abstract class MyStage extends Stage{

    public abstract void addInput();
    public float w, h;

    public void resize(int width, int height) {
        w = width;
        h = height;

        getViewport().update(width, height, true);
    }

}
