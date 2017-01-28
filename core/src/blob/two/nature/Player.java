package blob.two.nature;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by me on 28.01.17.
 */
public class Player extends Group{

    public AnimActor figure;
    public Actor hand;

    public Player() {
        figure = new AnimActor();
        this.addActor(figure);
    }


}
