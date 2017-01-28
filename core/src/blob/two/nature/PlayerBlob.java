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
        BodyDef figureBodyDef = new BodyDef();

        figureBodyDef.type = BodyType.DynamicBody;

        figureBodyDef.position.set(100, 500);

        b2dFigureBody = b2dWorld.createBody(figureBodyDef);

        FixtureDef figureFixtureDef = new FixtureDef();
        figureFixtureDef.shape = b2dFigureShape;
        figureFixtureDef.density = 1.f;
        figureFixtureDef.restitution = 0.f;
        figureFixtureDef.friction = 0.4f;

        b2dFigureBody.createFixture(figureFixtureDef);

        b2dFigureShape.dispose();
    }
}