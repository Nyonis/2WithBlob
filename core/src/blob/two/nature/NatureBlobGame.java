package blob.two.nature;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class NatureBlobGame extends ApplicationAdapter {

    private GameStage stage;


    private void initOpenGL() {
        // opengl setup
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(1, 0, 0, 1);
    }


    @Override
    public void create() {
        super.create();
        initOpenGL();

        stage = new DemoStage();
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
