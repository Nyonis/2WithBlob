package blob.two.nature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import helper.MapConvertHelper;

/**
 * Created by me on 28.01.17.
 */
public class DemoStage extends GameStage {

    public static int TILE_SIZE = 64;
    private final Sound sound;

    //public MyInput input;

    public DemoStage(NatureBlobGame game, int level) {
        super(game, "Level" + level + ".tmx");
        MapConvertHelper.mapToCollisionBody(map, "WallObjects", b2dWorld, null);
        MapConvertHelper.mapToCollisionBody(map, "SpikeObjects", b2dWorld, GameStage.ID_DIE);

        final FileHandle soundFile = Gdx.files.internal("8bit_music_fadeout.mp3");
        sound = Gdx.audio.newSound(soundFile);
    	long id = sound.play(1f);
        sound.setLooping(id, true);
    }


    @Override
    public void act() {
        super.act();
        /*TODO ???
        if (input.isPressed(Input.Keys.LEFT)) {

        }
        if (input.isPressed(Input.Keys.RIGHT)) {

        }*/
    }

    // TODO delete?
    public void create() {}
}