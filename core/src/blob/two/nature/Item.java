package blob.two.nature;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import helper.MapConvertHelper;

import java.util.Map;

/**
 * Created by me on 28.01.17.
 */
public class Item extends Group {

    private final World world;
    public AnimActor actor;
    private final Body body;

    public Item(World w, Shape s, Texture tex) {
        this.world = w;
        actor = new AnimActor(tex);
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef fDef = new FixtureDef();
        fDef.shape = s;
        fDef.filter.groupIndex = 1;
        fDef.isSensor = true;
        this.body = w.createBody(bDef);
        this.body.createFixture(fDef).getBody();
        this.body.setUserData(this);
        addActor(actor);
    }

    public void consume(){
        world.destroyBody(this.body);
        this.remove();
    }

    public void setPos(float x, float y) {
        body.setTransform(x * MapConvertHelper.WORLD_FAC, y* MapConvertHelper.WORLD_FAC, 0);
        this.setPosition(x, y);
    }

}
