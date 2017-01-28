package blob.two.nature;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * Created by me on 28.01.17.
 */
public class PlayerBlob extends Group {

    private static final int NUM_BLOB_IDLE_REGIONS = 6;
    public AnimActor figure;
    public AnimActor hand;

    public Body b2dFigureBody;

    public PlayerBlob(TextureAtlas blobAtlas, Shape b2dFigureShape, World b2dWorld) {
        this(b2dFigureShape, b2dWorld);
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (int i = 0; i < NUM_BLOB_IDLE_REGIONS; i++) {
            TextureAtlas.AtlasRegion region = blobAtlas.findRegion("Blob00" + i);
            if (region != null)
                regions.add(region);
        }

        figure = new AnimActor(regions);
        //hand = new AnimActor();

        //hand.setPosition(20, 20);
        this.addActor(figure);
        //this.addActor(hand);
    }

    public PlayerBlob(Shape b2dFigureShape, World b2dWorld) {
        createBody(b2dFigureShape, b2dWorld);
    }

    private void createBody(Shape b2dFigureShape, World b2dWorld) {
        BodyDef bDef = new BodyDef();

        bDef.type = BodyType.DynamicBody;
        bDef.position.set(100, 500);
        bDef.fixedRotation = true;

        b2dFigureBody = b2dWorld.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = b2dFigureShape;
        fDef.density = 1.f;
        fDef.restitution = 0.f;
        fDef.friction = 0.4f;
        fDef.filter.groupIndex = 1;
        b2dFigureBody = b2dFigureBody.createFixture(fDef).getBody();
        b2dFigureBody.setUserData(GameStage.ID_PLAYER);
        b2dFigureShape.dispose();
    }
}