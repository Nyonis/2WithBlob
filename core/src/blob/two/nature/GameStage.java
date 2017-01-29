package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/**
 * Created by me on 28.01.17.
 */
public abstract class GameStage extends MyStage {

    public static final Integer ID_PLAYER = 2;
    public static final Integer ID_DIE = 2;
    public static final Integer ID_ARMPROJECTILE = 3;
    public OrthographicCamera camera;

    private OrthogonalTiledMapRenderer renderer;
    public TiledMap map;
    public PlayerBlob playerBlob;
    public PlayerNature playerNature;
    public World b2dWorld;
    private Box2DDebugRenderer b2dDebugRenderer;
    private int layerCount;
    private int[] preLayers;
    private TiledMapTileLayer foreGroundLayer;
    private ArrayList<Item> toDestroy = new ArrayList<Item>();


    public GameStage(NatureBlobGame natureBlobGame, String mapName) {

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        // init camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        //camera.zoom = 2f;
        camera.update();
        setViewport(new FitViewport(w, h, camera));

        //Init physics with -10 units gravity in the y-axis
        //MUST be called before loadMap()
        initPhysics(new Vector2(0f, -100.0f));
        
        // always render a tile map
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);
        
        b2dDebugRenderer = new Box2DDebugRenderer();


        PolygonShape shapeBlob = new PolygonShape();
        Vector2 textureDelta = new Vector2(64, 64);
        Vector2[] vertices= new Vector2[7];
        vertices[0] = new Vector2(104.f,101.f).sub(textureDelta);	
        vertices[1] = new Vector2(52.f,104.f).sub(textureDelta);	
        vertices[2] = new Vector2(-2.f,49.f).sub(textureDelta);	
        vertices[3] = new Vector2(11.f,29.f).sub(textureDelta);	
        vertices[4] = new Vector2(83.f,24.f).sub(textureDelta);	
        vertices[5] = new Vector2(106.f,34.f).sub(textureDelta);	
        vertices[6] = new Vector2(113.f,70.f).sub(textureDelta);
        shapeBlob.set(vertices);
        playerBlob = new PlayerBlob(natureBlobGame.blobAtlas, shapeBlob, b2dWorld);
        this.addActor(playerBlob);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(128, 20);
        playerNature = new PlayerNature(shape, b2dWorld);
        this.addActor(playerNature);

        loadMap(mapName);

        // always render a tile map
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);

        create();
    }

    private void initPhysics(Vector2 gravity) {
        b2dWorld = new World(gravity, true);
    }


    private void loadMap(String name) {
        map = new TmxMapLoader().load(name);
        layerCount = map.getLayers().getCount();

        // trigger, foreground, wallobjects
        preLayers = new int[layerCount - 4];
        for (int i = 0; i < layerCount - 4; i++) {
            preLayers[i] = i;
        }
        foreGroundLayer = (TiledMapTileLayer) map.getLayers().get("Foreground");
        TiledMapTileLayer trigersLayer = (TiledMapTileLayer) map.getLayers().get("Triggers");

        for (int i = 0; i < trigersLayer.getHeight(); i++) {
            for (int j = 0; j < trigersLayer.getWidth(); j++) {
                TiledMapTileLayer.Cell c = trigersLayer.getCell(j, i);
                if (c != null) {
                    MapProperties ps = c.getTile().getProperties();
                    if (!ps.containsKey("Trigger"))
                        continue;

                    String key = ps.get("Trigger", String.class);
                    float x = trigersLayer.getTileWidth() * j;
                    float y = trigersLayer.getTileHeight() * i;

                    if (key.equals("Blob")) {
                        // set postion via physic!!
                        playerBlob.b2dFigureBody.setTransform(x, y, 0);
                        playerBlob.b2dArmProjectile.setTransform(x, y, 0);

                    } else if (key.equals("Cloud")) {
                        playerNature.hitbox.setTransform(x, y, 0);
                    } else {
                        System.out.println("Unknown key: " + key);
                    }
                }
            }
        }
        //map.getProperties().get("bubber", String.class);
        //MapLayers layers = map.getLayers();
        //TiledMapTileLayer l = (TiledMapTileLayer) layers.get(0);
        //TiledMapTileLayer.Cell c = l.getCell(0, 0);
        //c.getTile().getProperties();
//        map.getTileSets().getTile(0).getProperties();
    }

    /**
     * Initial stuff for subclasses in here!
     */
    public abstract void create();

    public void doPhysicsStep() {

    	//TODO Dynamisch an die Framerate anpassen
    	playerBlob.doPhysics();
    	b2dWorld.step(1/60f, 3, 1);
    }

    @Override
    public void draw() {
        // render tiles first

        if (toDestroy.size() > 0) {
            for (Item e : toDestroy) {
                e.consume();
            }
            toDestroy.clear();
        }
        
        playerBlob.setPosition(playerBlob.b2dFigureBody.getPosition().x, playerBlob.b2dFigureBody.getPosition().y);
 
        // move cloud if it gets out of screen
        playerNature.hitbox.setTransform(
        		Math.max(playerNature.hitbox.getPosition().x, camera.position.x - this.getViewport().getScreenWidth()/2*camera.zoom),
        		Math.max(playerNature.hitbox.getPosition().y, camera.position.y - this.getViewport().getScreenHeight()/2*camera.zoom), 0f);
        playerNature.hitbox.setTransform(
        		Math.min(playerNature.hitbox.getPosition().x, camera.position.x + this.getViewport().getScreenWidth()/2*camera.zoom),
        		Math.min(playerNature.hitbox.getPosition().y, camera.position.y + this.getViewport().getScreenHeight()/2*camera.zoom), 0f);
        
        playerNature.setPosition(playerNature.hitbox.getPosition().x, playerNature.hitbox.getPosition().y);

        Vector3 oldCameraPosition = camera.position.cpy();
        camera.position.set(playerBlob.getX(), playerBlob.getY(), 0);
        Vector3 cameraDelta = camera.position.cpy().sub(oldCameraPosition);
        playerBlob.addDeltaToArmTarget(new Vector2(cameraDelta.x, cameraDelta.y));

        camera.update();
        renderer.setView(camera);
        renderer.render(preLayers);
        b2dDebugRenderer.render(b2dWorld, camera.combined);

        // make the scene draw stuff
        super.draw();

        renderer.renderTileLayer(foreGroundLayer);

        doPhysicsStep();
        b2dWorld.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a != null && b != null) {
                    if (isItem(a, b))
                        toDestroy.add(((Item) a));
                    else if (isItem(b, a))
                        toDestroy.add(((Item) b));
                    else if (isDie(a, b))
                        die();
                } else if (a != null || b != null) {
                	System.out.println("Contact detected 2");
                	if (isArmCollision(a, b))
                		playerBlob.lock();
                    else if (isArmCollision(b, a))
                    	playerBlob.lock();
                }
            }


            @Override // TODO
            public void endContact(Contact contact) {}

            @Override
            public void postSolve(Contact arg0, ContactImpulse arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void preSolve(Contact arg0, Manifold arg1) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void die() {
        Gdx.app.exit();
    }


    private boolean isDie(Object a, Object b) {
        return (a.equals(ID_PLAYER) && b.equals(ID_DIE)) || (b.equals(ID_PLAYER) && a.equals(ID_DIE));
    }

    private boolean isItem(Object a, Object b) {
        return  (b.equals(ID_PLAYER) && a instanceof Item);

    }
    
    private boolean isArmCollision(Object a, Object b) {
    	return (a != null && a.equals(ID_ARMPROJECTILE));
    }


    @Override
    public void addInput() {
        Gdx.input.setInputProcessor(MyInput.getInstance());
    }
}
