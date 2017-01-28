package blob.two.nature;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;

/**
 * Created by me on 28.01.17.
 */
public class MyInput implements InputProcessor {

    public HashMap<Integer, Boolean> pressed = new HashMap<Integer, Boolean>();
    public HashMap<Integer, KeyPressHandler> handlers = new HashMap<Integer, KeyPressHandler>();

    public static interface KeyPressHandler {
        public void press(boolean isDown);
    }

    public void addHandler(int keycode, KeyPressHandler handler) {
        handlers.put(keycode, handler);
    }

    @Override
    public boolean keyDown(int keycode) {
        pressed.put(keycode, true);
        KeyPressHandler h = handlers.get(keycode);
        if (h != null) {
            h.press(true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressed.put(keycode, false);
        KeyPressHandler h = handlers.get(keycode);
        if (h != null) {
            h.press(false);
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // no mulit pointer support
        if (pointer != 0)
            return false;

        if (button == Input.Buttons.LEFT) {

            onLeftClick();
        } else if (button == Input.Buttons.RIGHT) {
            onRightClick();
        }


        return false;
    }

    public boolean isPressed(int keycode) {
        Boolean p = pressed.get(keycode);
        return p != null ? p : false;
    }

    public void onRightClick() {
        System.out.println("Right click");
    }

    public void onLeftClick() {
        System.out.println("Left click");
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


}
