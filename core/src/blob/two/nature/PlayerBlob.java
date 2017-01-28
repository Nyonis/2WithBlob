package blob.two.nature;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by me on 28.01.17.
 */
public class PlayerBlob extends Group{

    public AnimActor figure;
    public AnimActor hand;
    
    public Body b2dFigureBody;

    public PlayerBlob() {
        figure = new AnimActor();
        hand = new AnimActor();

        hand.setPosition(20,20);
        this.addActor(figure);
        //this.addActor(hand);
    }
    
    public PlayerBlob(Shape b2dFigureShape, World b2dWorld) {
    	figure = new AnimActor();
    	hand = new AnimActor();
    	
    	createBody(b2dFigureShape, b2dWorld);
    	
    	hand.setPosition(20, 20);
    	this.addActor(figure);
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