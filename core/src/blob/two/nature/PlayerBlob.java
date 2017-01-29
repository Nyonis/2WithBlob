package blob.two.nature;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;

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

    public Body b2dArmProjectile;

    private Vector2 armTarget;
    private boolean extend;
    public boolean locked;
    private float maxRopeLength;
    public RopeJoint b2dFigureArmJoint;


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
        maxRopeLength = 500.f;

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
    	figureBodyDef.fixedRotation = true;
    	
    	b2dFigureBody = b2dWorld.createBody(figureBodyDef);
    	
    	FixtureDef figureFixtureDef = new FixtureDef();
    	figureFixtureDef.shape = b2dFigureShape;
    	figureFixtureDef.density = 1.f;
    	figureFixtureDef.restitution = 0.f;
    	figureFixtureDef.friction = 0.4f;

        figureFixtureDef.filter.groupIndex = 1;
        b2dFigureBody = b2dFigureBody.createFixture(figureFixtureDef).getBody();
        b2dFigureBody.setUserData(GameStage.ID_PLAYER);
        b2dFigureShape.dispose();

    	
    	//Hier wird das Projektil erzeugt, dass f�r die Trefferdetektion des Armes zust�ndig ist
    	BodyDef armProjectileDef = new BodyDef();
    	armProjectileDef.type = BodyType.DynamicBody;
    	armProjectileDef.gravityScale = 0.f;
    	//TODO Richtigen Offset f�r die Zunge eintragen

    	CircleShape armProjectileCircle = new CircleShape();
    	armProjectileCircle.setRadius(1.f);
    	
    	FixtureDef armProjectileFixtureDef = new FixtureDef();
    	armProjectileFixtureDef.shape = armProjectileCircle;
    	armProjectileFixtureDef.density = 1.f;
    	armProjectileFixtureDef.isSensor = true;
    	
    	b2dArmProjectile = b2dWorld.createBody(armProjectileDef);
    	b2dArmProjectile.createFixture(armProjectileFixtureDef);
    	
    	armProjectileCircle.dispose();
    	
    	//Hier werden alle Joints f�r die Spielfigur definiert
    	RopeJointDef armBaseProjectileJointDef = new RopeJointDef();
    	armBaseProjectileJointDef.maxLength = 0.f;
    	armBaseProjectileJointDef.bodyA = b2dFigureBody;
    	armBaseProjectileJointDef.bodyB = b2dArmProjectile;
    	armBaseProjectileJointDef.localAnchorA.set(new Vector2());
    	armBaseProjectileJointDef.localAnchorB.set(new Vector2());
    	
    	b2dFigureArmJoint = (RopeJoint) b2dWorld.createJoint(armBaseProjectileJointDef);
    }
    
    public void activateExtendArm(Vector2 target) {
    	armTarget = target;
    	extend = true;
    }
    
    public void updateArmTarget(Vector2 target) {
    	armTarget = target;
    }
    
    public void deactivateExtendArm() {
    	extend = false;
    }
    
    private void extendArm() {
    	float currentJointLength = b2dFigureArmJoint.getMaxLength();
    	if(currentJointLength < maxRopeLength) {
    		b2dFigureArmJoint.setMaxLength(currentJointLength + maxRopeLength/4);
    	}
    	Vector2 distance = b2dArmProjectile.getPosition().cpy().sub(armTarget);
    	if(!(distance.len() < 1.f)) {
    		b2dArmProjectile.setLinearVelocity(armTarget.cpy().sub(b2dArmProjectile.getPosition()).nor().scl(maxRopeLength / 4));
    	} else {
    		b2dArmProjectile.setLinearVelocity(new Vector2());
    	}
    }
    
    private void retractArm() {
    	float currentJointLegth = b2dFigureArmJoint.getMaxLength();
    	if(currentJointLegth > 0.f) {
    		if(!locked) {
	    		b2dArmProjectile.setLinearVelocity(b2dFigureBody.getLinearVelocity().add(b2dFigureBody.getPosition().sub(b2dArmProjectile.getPosition()).nor().scl(maxRopeLength / 4)));
    		} else {
    			//Zus�tzliche Masse in Skalar, wegen Impuls, damit pendeln m�glich ist
    			b2dFigureBody.applyLinearImpulse(b2dArmProjectile.getPosition().sub(b2dFigureBody.getPosition()).nor().scl(b2dFigureBody.getMass() * maxRopeLength / 4), b2dFigureBody.getPosition(), true);
    		}
	    	b2dFigureArmJoint.setMaxLength(Math.max(0, b2dFigureArmJoint.getMaxLength() - maxRopeLength/4));
    	}
    }
    
    public void doPhysics() {
    	if(!locked && extend) {
    		extendArm();
    	} else {
    		retractArm();
    	}
    }


}