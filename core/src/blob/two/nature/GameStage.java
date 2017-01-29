package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

import static helper.MapConvertHelper.WORLD_FAC;
import static helper.MapConvertHelper.WORLD_SCALE;

/**
 * Created by me on 28.01.17.
 */
public abstract class GameStage extends MyStage {

    public static final Integer ID_PLAYER = 1;
    public static final Integer ID_DIE = 2;
    public static final Integer ID_ARMPROJECTILE = 3;
    private final OrthographicCamera cameraP;
    private final Number has;
    private final Number max;
    private final Group gCounter;
    private final NatureBlobGame game;
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
    private int collectibleCounter;
    private int eatCounter;


    public GameStage(NatureBlobGame natureBlobGame, String mapName) {
        this.game = natureBlobGame;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        // init camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.zoom = 2f;
        camera.update();

        cameraP = new OrthographicCamera();
        cameraP.setToOrtho(false, w, h);
        cameraP.zoom = camera.zoom * WORLD_FAC;
        cameraP.update();

        setViewport(new FitViewport(w, h, camera));

        //Init physics with -10 units gravity in the y-axis
        //MUST be called before loadMap()

        initPhysics(new Vector2(0f, -10.0f));
        // always render a tile map
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);

        //b2dDebugRenderer = new Box2DDebugRenderer();


        PolygonShape shapeBlob = new PolygonShape();
        Vector2 textureDelta = new Vector2(64, 64);
        Vector2[] vertices = new Vector2[7];
        vertices[0] = new Vector2(104.f, 101.f).sub(textureDelta).scl(WORLD_FAC);
        vertices[1] = new Vector2(52.f, 104.f).sub(textureDelta).scl(WORLD_FAC);
        vertices[2] = new Vector2(-2.f, 49.f).sub(textureDelta).scl(WORLD_FAC);
        vertices[3] = new Vector2(11.f, 29.f).sub(textureDelta).scl(WORLD_FAC);
        vertices[4] = new Vector2(83.f, 24.f).sub(textureDelta).scl(WORLD_FAC);
        vertices[5] = new Vector2(106.f, 34.f).sub(textureDelta).scl(WORLD_FAC);
        vertices[6] = new Vector2(113.f, 70.f).sub(textureDelta).scl(WORLD_FAC);
        shapeBlob.set(vertices);
        playerBlob = new PlayerBlob(natureBlobGame.blobAtlas, shapeBlob, b2dWorld);
        this.addActor(playerBlob);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(128 * WORLD_FAC, 20 * WORLD_FAC);
        playerNature = new PlayerNature(shape, b2dWorld);
        this.addActor(playerNature);


        gCounter = new Group();
        has = new Number(0);
        max = new Number(3);
        Number slash = new Number(12);
        max.change(5);
        float size = 40;
        slash.setPosition(size, 0);
        max.setPosition(size * 2, 0);
        gCounter.addActor(has);
        gCounter.addActor(max);
        gCounter.addActor(slash);
        addActor(gCounter);

        loadMap(mapName);

        // always render a tile map
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);

        MyInput.getInstance().scrollHandler = new MyInput.ScrollHandler() {
            @Override
            public void scroll(int amount) {
                camera.zoom += amount/10f;
                cameraP.zoom = camera.zoom * WORLD_FAC;
                camera.update();
                cameraP.update();
            }
        };

        gCounter.toFront();
        create();
    }

    @SuppressWarnings("static-access")
    private void initPhysics(Vector2 gravity) {
        b2dWorld = new World(gravity, true);
        b2dWorld.setVelocityThreshold(1000.f);
    }


    private void loadMap(String name) {
        map = new TmxMapLoader().load(name);
        layerCount = map.getLayers().getCount();

        collectibleCounter = 0;

        // trigger, foreground, wallobjects
        preLayers = new int[layerCount - 4];
        for (int i = 0; i < layerCount - 4; i++) {
            preLayers[i] = i;
        }
        foreGroundLayer = (TiledMapTileLayer) map.getLayers().get("Foreground");
        TiledMapTileLayer trigersLayer = (TiledMapTileLayer) map.getLayers().get("Triggers");
        float ts = trigersLayer.getTileWidth();

        for (int i = 0; i < trigersLayer.getHeight(); i++) {
            for (int j = 0; j < trigersLayer.getWidth(); j++) {
                TiledMapTileLayer.Cell c = trigersLayer.getCell(j, i);
                if (c != null) {
                    MapProperties ps = c.getTile().getProperties();
                    if (!ps.containsKey("Trigger"))
                        continue;

                    String key = ps.get("Trigger", String.class);
                    // TODO no exact positions
                    float x = trigersLayer.getTileWidth() * j;
                    float y = trigersLayer.getTileHeight() * i;

                    if (key.equals("Blob")) {
                        // set postion via physic!!

                        playerBlob.b2dFigureBody.setTransform(x * WORLD_FAC, y * WORLD_FAC, 0);
                        playerBlob.b2dArmProjectile.setTransform(x * WORLD_FAC, y * WORLD_FAC, 0);

                    } else if (key.equals("Cloud")) {
                        playerNature.hitbox.setTransform(x * WORLD_FAC, y * WORLD_FAC, 0);
                    } else if (key.equals("Collectible")) {
                        collectibleCounter++;
                        CircleShape s = new CircleShape();
                        s.setRadius(32 * WORLD_FAC);
                        Item a = new Item(b2dWorld, s, new Texture("kugel.png"));
                        a.setPos(x + ts / 2, y + ts / 2);
                        addActor(a);
                    } else {
                        System.out.println("Unknown key: " + key);
                    }
                }
            }
        }
        max.change(collectibleCounter);
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
        b2dWorld.step(1 / 60f, 3, 1);
    }

    private void eat() {

        eatCounter++;
        has.change(eatCounter);
        if (eatCounter == collectibleCounter){
            System.out.println("Won!!!");
            game.showMenu();
        }
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

        playerBlob.setPosition(playerBlob.b2dFigureBody.getPosition().x * WORLD_SCALE, playerBlob.b2dFigureBody.getPosition().y * WORLD_SCALE);

        Vector2 end = playerBlob.b2dArmProjectile.getPosition().cpy();
        Vector2 dir = playerBlob.b2dFigureBody.getPosition().cpy().sub(end);
        dir = dir.scl(WORLD_SCALE);
        float angle = (float) Math.atan2(-dir.y, -dir.x);
        angle *= MathUtils.radiansToDegrees;
        playerBlob.hand.setScaleX(dir.len() / playerBlob.hand.currentFrame.getRegionWidth());
        playerBlob.hand.setRotation(angle);

        // move cloud if it gets out of screen
        playerNature.hitbox.setTransform(
                Math.max(playerNature.hitbox.getPosition().x, (camera.position.x - this.getViewport().getScreenWidth() / 2 * camera.zoom)* WORLD_FAC),
                Math.max(playerNature.hitbox.getPosition().y, (camera.position.y - this.getViewport().getScreenHeight() / 2 * camera.zoom)* WORLD_FAC), 0f);
        playerNature.hitbox.setTransform(
                Math.min(playerNature.hitbox.getPosition().x, (camera.position.x + this.getViewport().getScreenWidth() / 2 * camera.zoom)* WORLD_FAC),
                Math.min(playerNature.hitbox.getPosition().y, (camera.position.y + this.getViewport().getScreenHeight() / 2 * camera.zoom)* WORLD_FAC), 0f);

        playerNature.setPosition(playerNature.hitbox.getPosition().x* WORLD_SCALE, playerNature.hitbox.getPosition().y* WORLD_SCALE);

        boolean needsFlip = false;
        if ((angle > 90 || angle < -90) && !playerBlob.figure.currentFrame.isFlipX()) {
            needsFlip = true;
        }
        if ((angle < 90 && angle > -90) && playerBlob.figure.currentFrame.isFlipX()) {
            needsFlip = true;
        }

        if (dir.len() > 30 && needsFlip)
            playerBlob.figure.currentFrame.flip(true, false);

        Vector3 oldCameraPosition = camera.position.cpy();
        camera.position.set(playerBlob.getX(), playerBlob.getY(), 0);
        Vector3 cameraDelta = camera.position.cpy().sub(oldCameraPosition);
        playerBlob.addDeltaToArmTarget(new Vector2(cameraDelta.x, cameraDelta.y).scl(1 / camera.zoom * WORLD_FAC));

        camera.update();
        cameraP.position.set(camera.position.x * WORLD_FAC, camera.position.y * WORLD_FAC, 0);
        cameraP.update();
        renderer.setView(camera);
        renderer.render(preLayers);
        //b2dDebugRenderer.render(b2dWorld, cameraP.combined);
        gCounter.setPosition(camera.position.x - w / 2 * camera.zoom + 10, camera.position.y + h / 2 * camera.zoom - 60);

        // make the scene draw stuff
        super.draw();

        renderer.render(new int[]{3});

        doPhysicsStep();
        b2dWorld.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Object a = contact.getFixtureA().getBody().getUserData();
                Object b = contact.getFixtureB().getBody().getUserData();
                if (a != null && b != null) {
                    if (isItem(a, b)) {
                        toDestroy.add(((Item) a));
                        eat();
                    } else if (isItem(b, a)) {
                        toDestroy.add(((Item) b));
                        eat();
                    } else if (isDie(a, b))
                        die();
                } else if (a != null || b != null) {
                    if (isArmCollision(a, b))
                        playerBlob.lock();
                    else if (isArmCollision(b, a))
                        playerBlob.lock();
                }
            }




            @Override // TODO
            public void endContact(Contact contact) {
            }

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
        game.showMenu();
    }


    private boolean isDie(Object a, Object b) {
        return (a.equals(ID_PLAYER) && b.equals(ID_DIE)) || (b.equals(ID_PLAYER) && a.equals(ID_DIE));
    }

    private boolean isItem(Object a, Object b) {
        return (b.equals(ID_PLAYER) && a instanceof Item);

    }

    private boolean isArmCollision(Object a, Object b) {
        return (a != null && a.equals(ID_ARMPROJECTILE));
    }


    @Override
    public void addInput() {
        Gdx.input.setInputProcessor(MyInput.getInstance());
    }
}
