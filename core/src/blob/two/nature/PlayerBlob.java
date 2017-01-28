package blob.two.nature;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by me on 28.01.17.
 */
public class PlayerBlob extends Group{

    public AnimActor figure;
    public AnimActor hand;

    public PlayerBlob() {
        figure = new AnimActor();
        hand = new AnimActor();

        hand.setPosition(20,20);
        this.addActor(figure);
        //this.addActor(hand);
    }


}
