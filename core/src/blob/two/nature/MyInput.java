package blob.two.nature;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by me on 28.01.17.
 */
public class MyInput implements InputProcessor {

	private static MyInput theOne;

	public static MyInput getInstance() {
		if (theOne == null) {
			theOne = new MyInput();
		}
		return theOne;
	}

    public HashMap<Integer, Boolean> pressed;
    public List<KeyPressHandler> keyHandlers;
    public List<MouseHandler> mouseHandlers;
    private boolean leftDown, rightDown;

	private MyInput() {
		pressed = new HashMap<Integer, Boolean>();
		keyHandlers = new LinkedList<KeyPressHandler>();
		mouseHandlers = new LinkedList<MouseHandler>();
	}

	public void clear(){
	    keyHandlers.clear();
	    mouseHandlers.clear();
	    pressed.clear();
    }

    public interface KeyPressHandler {
        public void press(boolean isDown, int key);
    }

    public interface MouseHandler {
    	public void mouseAction(int x, int y, boolean isDown, boolean isLeft, boolean isDrag);
    }

    public void addKeyHandler(KeyPressHandler handler) {
        keyHandlers.add(handler);
    }

    public void addMouseHandler(MouseHandler handler) {
    	mouseHandlers.add(handler);
    }

    @Override
    public boolean keyDown(int keycode) {
        pressed.put(keycode, true);
        for (KeyPressHandler handler : keyHandlers) {
            handler.press(true, keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressed.put(keycode, false);
        for (KeyPressHandler handler : keyHandlers) {
            handler.press(false, keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // no mulit pointer support
        if (pointer != 0) {
            return false;
        }

        if (button == Input.Buttons.LEFT) {
            leftDown = true;
            for (MouseHandler handler : mouseHandlers) {
            	handler.mouseAction(screenX, screenY, true, true, false);
            }
        } else if (button == Input.Buttons.RIGHT) {
            rightDown = true;
            for (MouseHandler handler : mouseHandlers) {
            	handler.mouseAction(screenX, screenY, true, false, false);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // no mulit pointer support
        if (pointer != 0) {
            return false;
        }

        if (button == Input.Buttons.LEFT) {
            leftDown = false;
            for (MouseHandler handler : mouseHandlers) {
            	handler.mouseAction(screenX, screenY, false, true, false);
            }
        } else if (button == Input.Buttons.RIGHT) {
            rightDown = false;
            for (MouseHandler handler : mouseHandlers) {
            	handler.mouseAction(screenX, screenY, false, false, false);
            }
        }
        return false;
    }

    public boolean isPressed(int keycode) {
        Boolean p = pressed.get(keycode);
        return p != null ? p : false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // no mulit pointer support
    	if(pointer != 0) {
    		return false;
    	}
    	
    	if(leftDown) {
            for (MouseHandler handler : mouseHandlers) {
            	handler.mouseAction(screenX, screenY, false, true, true);
            }
    	}
    	
    	if(rightDown) {
            for (MouseHandler handler : mouseHandlers) {
            	handler.mouseAction(screenX, screenY, false, false, true);
            }
    	}
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
