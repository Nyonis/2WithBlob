package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by me on 28.01.17.
 */
public abstract class GameStage extends Stage {

    private float w, h;
    public OrthographicCamera camera;

    private OrthogonalTiledMapRenderer renderer;
    public TiledMap map;
    public PlayerBlob playerBlob;
    public PlayerNature playerNature;
    private World b2dWorld;
    private Box2DDebugRenderer b2dDebugRenderer;


    public GameStage(String mapName) {

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        // init camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        setViewport(new FitViewport(20*w, 20*h, camera));

        //Init physics with -10 units gravity in the y-axis
        //MUST be called before loadMap()
        initPhysics(new Vector2(0.0f, -10.0f));
        
        // always render a tile map
        loadMap(mapName);
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);
        
        b2dDebugRenderer = new Box2DDebugRenderer();
        


        // have playerBlob by default
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);
        playerBlob = new PlayerBlob(circle, b2dWorld);
        this.addActor(playerBlob);

        // no visual player!!
        playerNature = new PlayerNature();

        create();
    }
    
    private void initPhysics(Vector2 gravity) {
    	b2dWorld = new World(gravity, true);
    	
    	//TODO Durch die echten Böden ersetzen
    	
    	// Create our body definition
    	BodyDef groundBodyDef = new BodyDef();  
    	// Set its world position
    	groundBodyDef.position.set(new Vector2(0, 10));  

    	// Create a body from the defintion and add it to the world
    	Body groundBody = b2dWorld.createBody(groundBodyDef);  

    	// Create a polygon shape
    	PolygonShape groundBox = new PolygonShape();  
    	// Set the polygon shape as a box which is twice the size of our view port and 20 high
    	// (setAsBox takes half-width and half-height as arguments)
    	groundBox.setAsBox(camera.viewportWidth, 10.0f);
    	// Create a fixture from our polygon shape and add it to our ground body  
    	groundBody.createFixture(groundBox, 0.0f); 
    	// Clean up after ourselves
    	groundBox.dispose();
    }


    private void loadMap(String name) {
        map = new TmxMapLoader().load(name);
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
    	b2dWorld.step(1/60f, 6, 2);
    }

    @Override
    public void draw() {
        // render tiles first
        camera.update();
        renderer.setView(camera);
        renderer.render();
        b2dDebugRenderer.render(b2dWorld, camera.combined);

        
        playerBlob.setPosition(playerBlob.b2dFigureBody.getPosition().x, playerBlob.b2dFigureBody.getPosition().y);
        // make the scene draw stuff
        super.draw();
        
        
        doPhysicsStep();
    }

    public void resize(int width, int height) {
        w = width;
        h = height;

        getViewport().update(width, height, true);
    }

}
