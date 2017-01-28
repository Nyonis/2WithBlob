package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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


    public GameStage(String mapName) {

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        // init camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        setViewport(new FitViewport(w, h, camera));

        // always render a tile map
        loadMap(mapName);
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);

        // have playerBlob by default
        playerBlob = new PlayerBlob();
        this.addActor(playerBlob);

        // no visual player!!
        playerNature = new PlayerNature();

        create();
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

    @Override
    public void draw() {
        // render tiles first
        camera.update();
        renderer.setView(camera);
        renderer.render();

        // make the scene draw stuff
        super.draw();
    }

    public void resize(int width, int height) {
        w = width;
        h = height;

        getViewport().update(width, height, true);
    }

}
