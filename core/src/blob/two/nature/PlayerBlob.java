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

    public final Integer ID_ARMPROJECTILE = GameStage.ID_ARMPROJECTILE;
    public Body b2dArmProjectile;

    private Vector2 armTarget;
    private boolean extend;
    private boolean retract;
    private float maxRopeLength;
    public RopeJoint b2dFigureArmJoint;
    
    public boolean locked;


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

    	
    	//Hier wird das Projektil erzeugt, dass fï¿½r die Trefferdetektion des Armes zustï¿½ndig ist
    	BodyDef armProjectileDef = new BodyDef();
    	armProjectileDef.type = BodyType.DynamicBody;
    	armProjectileDef.gravityScale = 0.f;
    	//TODO Richtigen Offset fï¿½r die Zunge eintragen

    	CircleShape armProjectileCircle = new CircleShape();
    	armProjectileCircle.setRadius(0.f);
    	
    	FixtureDef armProjectileFixtureDef = new FixtureDef();
    	armProjectileFixtureDef.shape = armProjectileCircle;
    	armProjectileFixtureDef.density = 1.f;
    	armProjectileFixtureDef.isSensor = true;
    	
    	b2dArmProjectile = b2dWorld.createBody(armProjectileDef);
    	b2dArmProjectile.createFixture(armProjectileFixtureDef);
    	b2dArmProjectile.setUserData(ID_ARMPROJECTILE);
    	
    	armProjectileCircle.dispose();
    	
    	//Hier werden alle Joints fï¿½r die Spielfigur definiert
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
    	b2dFigureArmJoint.setMaxLength(maxRopeLength);
    	extend = true;
    }
    
    public void deactivateExtendArm() {
    	Vector2 distance = b2dArmProjectile.getPosition().cpy().sub(b2dFigureBody.getPosition());
    	b2dFigureArmJoint.setMaxLength(distance.len());
    	extend = false;
    }
    
    public void resetArmTarget(Vector2 target) {
    	if(extend)
    		armTarget = target;
    }
    
    public void addDeltaToArmTarget(Vector2 delta) {
    	if(extend)
    		armTarget = armTarget.add(delta);
    }
    
    public void setRetractArm(boolean retract) {
    	this.retract = retract;
    	
    	if(!retract) {
    		Vector2 distance = b2dArmProjectile.getPosition().cpy().sub(b2dFigureBody.getPosition());
    		b2dFigureArmJoint.setMaxLength(distance.len());
    		//Lösche Bewegung in Y-Rchtg um "hochspringen" zu vermeiden
    		Vector2 vel = b2dFigureBody.getLinearVelocity();
    		b2dFigureBody.setLinearVelocity(vel.x, 0);
     	}
    }
    
    public void releaseLock() {    	
    	locked = false;
    }
    
    public void lock() {
    	locked = true;
    }
    
    private void extendArm() {
    	Vector2 distance = b2dArmProjectile.getPosition().cpy().sub(armTarget);
    	if(!(distance.len() < 1.f)) {
    		b2dArmProjectile.setLinearVelocity(armTarget.cpy().sub(b2dArmProjectile.getPosition()).nor().scl(maxRopeLength / 4));
    	} else {
    		b2dArmProjectile.setLinearVelocity(new Vector2());
    	}
    }
    
    private void retractArm() {
    	//float currentJointLegth = b2dFigureArmJoint.getMaxLength();
    	Vector2 distance = b2dArmProjectile.getPosition().cpy().sub(b2dFigureBody.getPosition());
    	if(!(distance.len() < 1.f)) {
    		if(!locked) {
	    		b2dArmProjectile.setLinearVelocity(b2dFigureBody.getLinearVelocity().add(b2dFigureBody.getPosition().sub(b2dArmProjectile.getPosition()).nor().scl(maxRopeLength / 4)));
    		} else {
    			//Zusï¿½tzliche Masse in Skalar, wegen Kraft, damit pendeln mï¿½glich ist
    			b2dFigureBody.applyForce(b2dArmProjectile.getPosition().sub(b2dFigureBody.getPosition()).nor().scl(b2dFigureBody.getMass() * (maxRopeLength / 4 ) * (maxRopeLength / 4 )), b2dFigureBody.getPosition(), true);
    		}
    	} else {
	    	b2dFigureArmJoint.setMaxLength(0.f);
    	}
    }
    
    public void doPhysics() {
    	if(locked) {
    		b2dArmProjectile.setType(BodyType.StaticBody);
    	} else {
        	b2dArmProjectile.setType(BodyType.DynamicBody);
    	}
    	if(extend) {
    		extendArm();
    	} else if (retract) {
    		retractArm();
    	} else if (!locked && !extend) {
    		//Auto retract
    		retractArm();
    	}
    }


}