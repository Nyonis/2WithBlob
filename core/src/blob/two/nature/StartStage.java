package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static java.awt.SystemColor.text;

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
    private Table table;
    private TextButton startButton;
    private TextButton playerButton;
    private TextButton settingsButton;
    private Skin uiSkin;
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
    }

    private void makeUI() {

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        table = new Table();
        table.setWidth(this.getWidth());
        table.align(Align.center);
        font = new BitmapFont(Gdx.files.internal("doctor_azul_2.0.fnt"),Gdx.files.internal("doctor_azul_2.0.png"),false);

        table.setPosition(0, Gdx.graphics.getHeight() / 1.5f);
        startButton = new TextButton("Hallo?", uiSkin);
        startButton.setWidth(100);
        startButton.setHeight(50);
        playerButton = new TextButton("....", uiSkin);
        settingsButton = new TextButton("Einstellungen", uiSkin);

        startButton.setLayoutEnabled(true);
        startButton.setTouchable(Touchable.enabled);

        addActor(startButton);

        playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Spieler");
                //game.showPlayerScreen();
            }
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Spieler");
                //game.showPlayerScreen();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Spieler");
                //game.showPlayerScreen();
            }
        });

        startButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
                System.out.println("down!!!!");
                return true;
            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Released");
                System.out.println("down!!!!");

            }
        });


         layout = new GlyphLayout(font, "Press any key to start");
        fontX =  (w - layout.width) / 2;
         fontY = (h + layout.height) / 2;



        //addActor(table);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
       super.touchDown(screenX, screenY, pointer, button);
        System.out.println(screenX + " "  + screenY);
        if(new Rectangle(startButton.getX(), startButton.getY(), startButton.getWidth(), startButton.getHeight()).contains(screenX, screenY)){
            System.out.println("FUCKING HIT!!!!");
        }
        return false;
    }

    @Override
    public boolean keyUp(int keyCode) {
        super.keyUp(keyCode);
        System.out.println("key up " + keyCode);
        startGame();
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

    public void startGame() {
        game.startGame();
    }


    @Override
    public void addInput() {
        Gdx.input.setInputProcessor(this);
    }
}
