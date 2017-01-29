package blob.two.nature;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import blob.two.nature.MyInput.KeyPressHandler;
import blob.two.nature.MyInput.MouseHandler;
import helper.MapConvertHelper;

import static helper.MapConvertHelper.WORLD_FAC;

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
    public boolean retractDone;
	private boolean reachedMaxLength;


	public PlayerBlob(TextureAtlas blobAtlas, Shape b2dFigureShape, World b2dWorld) {
        this(b2dFigureShape, b2dWorld);
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (int i = 0; i < NUM_BLOB_IDLE_REGIONS; i++) {
            TextureAtlas.AtlasRegion region = blobAtlas.findRegion("Blob00" + i);
            if (region != null)
                regions.add(region);
        }

        figure = new AnimActor(regions);
        hand = new AnimActor(new Texture("zunge.png"));
        maxRopeLength = 500.f * WORLD_FAC;

        hand.setPosition(0, 0);
		this.addActor(hand);
		this.addActor(figure);
        hand.scaleBy(2, 1);

        KeyPressHandler keyHandler = new KeyPressHandler() {
			@Override
			public void press(boolean isDown, int key) {
				if (key == Input.Keys.O) {
					releaseLock();
				}
			}
        };
        MouseHandler mouseHandler = new MouseHandler() {
			@Override
			public void mouseAction(int x, int y, boolean isDown, boolean isLeft, boolean isDrag) {
				if (isLeft) {
					if (isDown) {
						GameStage stage = (GameStage) getStage();
						Vector3 v = stage.camera.unproject(new Vector3(x, y, 0));
						activateExtendArm(new Vector2(v.x, v.y).scl(WORLD_FAC));
					} else if (isDrag) {
						GameStage stage = (GameStage) getStage();
						Vector3 v = stage.camera.unproject(new Vector3(x, y, 0));
						resetArmTarget(new Vector2(v.x, v.y).scl(WORLD_FAC));
					} else { // isUp
						deactivateExtendArm();
					}
				} else { // isRight
					if (isDown) {
						setRetractArm(true);
					} else if (!isDrag) { // isUp
						setRetractArm(false);
					}
				}
			}
        };
        MyInput.getInstance().addKeyHandler(keyHandler);
        MyInput.getInstance().addMouseHandler(mouseHandler);
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
    	figureFixtureDef.friction = 0.04f;

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
    	armProjectileCircle.setRadius(0.f);
    	
    	FixtureDef armProjectileFixtureDef = new FixtureDef();
    	armProjectileFixtureDef.shape = armProjectileCircle;
    	armProjectileFixtureDef.density = 0f;

    	armProjectileFixtureDef.isSensor = true;
    	//armProjectileFixtureDef.filter.groupIndex = 1;
    	
    	b2dArmProjectile = b2dWorld.createBody(armProjectileDef);
    	b2dArmProjectile.createFixture(armProjectileFixtureDef);
    	b2dArmProjectile.setUserData(ID_ARMPROJECTILE);
    	
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
    	if(retractDone) {
	    	armTarget = target;
	    	b2dFigureArmJoint.setMaxLength(maxRopeLength);
	    	extend = true;
    	}
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
    		//L�sche Bewegung in Y-Rchtg um "hochspringen" zu vermeiden
    		Vector2 vel = b2dFigureBody.getLinearVelocity();
    		b2dFigureBody.setLinearVelocity(vel.x, 0);
     	}
    }
    
    public void releaseLock() {    	
    	locked = false;
    	retractDone= false;
    }
    
    public void lock() {
    	if(retractDone)
    		locked = true;
    }
    
    private void extendArm() {
    	Vector2 distance = armTarget.cpy().sub(b2dArmProjectile.getPosition());
		Vector2 rope = b2dArmProjectile.getPosition().cpy().sub(b2dFigureBody.getPosition());
		reachedMaxLength = MathUtils.isEqual(rope.len(), maxRopeLength - 5);

		if (distance.len() < 1.f || reachedMaxLength) {
            b2dArmProjectile.setLinearVelocity(new Vector2());
        } else {
            b2dArmProjectile.setLinearVelocity(distance.nor().scl(maxRopeLength / 2));
        }

	}
    
    private void retractArm() {
    	//float currentJointLegth = b2dFigureArmJoint.getMaxLength();
    	Vector2 distance = b2dArmProjectile.getPosition().cpy().sub(b2dFigureBody.getPosition());
    	if(!(distance.len() < 1.f)) {
    		if(!locked) {
	    		b2dArmProjectile.setLinearVelocity(b2dFigureBody.getLinearVelocity().add(b2dFigureBody.getPosition().sub(b2dArmProjectile.getPosition()).nor().scl(maxRopeLength / 2)));
    		} else {
    			//Zus�tzliche Masse in Skalar, wegen Kraft, damit pendeln m�glich ist
    			b2dFigureBody.applyForce(b2dArmProjectile.getPosition().sub(b2dFigureBody.getPosition()).nor().scl(b2dFigureBody.getMass() * (maxRopeLength / 2 )), b2dFigureBody.getPosition(), true);
    		}
    	} else {
	    	b2dFigureArmJoint.setMaxLength(0.f);
	    	retractDone = true;
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