package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by me on 29.01.17.
 */
public class StartStage extends MyStage {

    private final NatureBlobGame game;
    private final OrthographicCamera camera;
    private final Texture splash;
    private final SpriteBatch sb;
    private final int w;
    private final int h;
    private final float sx;
    private final float sy;

    private BitmapFont font;
    private float fontY, fontX;
    private GlyphLayout layout;

    public StartStage(NatureBlobGame natureBlobGame) {
        this.game = natureBlobGame;
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        // init camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        //camera.zoom = 2f;
        camera.update();
        setViewport(new FitViewport(w, h, camera));


        sb = new SpriteBatch();
        splash = new Texture("TitelBlob.png");
        sx = (w - splash.getWidth()) / 2;
        sy = (h - splash.getHeight()) / 2;

        makeUI();

        MyController.getInstance().addListenerToActiveController(new ControllerAdapter() {
            @Override
            public boolean buttonUp(Controller controller, int buttonIndex) {
                startGame(1);
                return super.buttonUp(controller, buttonIndex);
            }
        });
    }

    private void makeUI() {
        font = new BitmapFont(Gdx.files.internal("doctor_azul_2.0.fnt"), Gdx.files.internal("doctor_azul_2.0.png"), false);
        layout = new GlyphLayout(font, "Press X in (0, 1, 2 ...) to to start level X");
        fontX = (w - layout.width) / 2;
        fontY = (h + layout.height) / 2;


        //addActor(table);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean keyUp(int keyCode) {
        super.keyUp(keyCode);
        if (keyCode < Input.Keys.NUM_0 || keyCode < Input.Keys.NUM_9)
            startGame(keyCode - Input.Keys.NUM_0);
        return false;
    }

    @Override
    public void draw() {
        sb.begin();
        sb.draw(splash, sx, sy);
        font.draw(sb, layout, fontX, fontY);
        sb.end();
        super.draw();

    }

    public void startGame(int level) {
        game.startGame(level);
    }


    @Override
    public void addInput() {
        Gdx.input.setInputProcessor(this);
    }
}
