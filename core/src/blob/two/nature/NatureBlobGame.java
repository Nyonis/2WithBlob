package blob.two.nature;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2D;

import blob.two.nature.MyInput.KeyPressHandler;

public class NatureBlobGame extends ApplicationAdapter {

    private GameStage stage;
    public TextureAtlas blobAtlas;


    private void initOpenGL() {
        // opengl setup
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void create() {
        super.create();
        initOpenGL();
        Box2D.init();

        Gdx.input.setInputProcessor(MyInput.getInstance());
        KeyPressHandler closer = new KeyPressHandler() {
            @Override
            public void press(boolean isDown, int key) {
            	if (key == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
            	}
            }
        };
        MyInput.getInstance().addKeyHandler(closer);

        AssetManager manager = new AssetManager();
        manager.setLoader(TextureAtlas.class, new TextureAtlasLoader(new InternalFileHandleResolver()));
        manager.load("blob.pack", TextureAtlas.class);
        manager.finishLoading();
        blobAtlas = manager.get("blob.pack", TextureAtlas.class);

        stage = new DemoStage(this);
    }

    @Override
    public void resize(int width, int height) {
        stage.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // make the current stage do stuff
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
