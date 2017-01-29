package blob.two.nature;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.controllers.mappings.Xbox;

public class MyController extends ControllerAdapter {
	
	private static MyController instance;
	
	private boolean initialized;
	private List<Controller> connectedControllers;
	private Controller activeController;
	
	public static MyController getInstance() {
		if(instance == null) {
			instance = new MyController();
		}
		
		return instance;
	}
	
	private MyController() {
		connectedControllers = new ArrayList<Controller>();
		initialize();
	}
	
	private void initialize () {
		if (initialized) return;
		// print the currently connected controllers to the console
		System.out.println("Controllers: " + Controllers.getControllers().size);
		int i = 0;
		for (Controller controller : Controllers.getControllers()) {
			System.out.println("#" + i++ + ": " + controller.getName());
			if(controller.getName().toLowerCase().contains("xbox")) {
				System.out.println("Supported Controller found: " + controller.getName());
				connectedControllers.add(controller);
				if(activeController == null) {
					activeController = controller;
				}
			}
		}
		if (Controllers.getControllers().size == 0) System.out.println("No controllers attached");

		// setup the listener that prints events to the console
		Controllers.addListener(this);
		initialized = true;
	}
	
	@Override
	public void connected(Controller controller) {
		if(controller.getName().toLowerCase().contains("xbox")) {
			System.out.println("Supported Controller found: " + controller.getName());
			connectedControllers.add(controller);
			if(activeController == null) {
				activeController = controller;
			}
		}
	}
	
	@Override
	public void disconnected(Controller controller) {
		if(controller.equals(activeController)) {
			activeController.removeListener(this);
		}

		connectedControllers.remove(controller);
		
		if(connectedControllers.size() > 0) {
			activeController = connectedControllers.get(0);
			activeController.addListener(this);
		} else {
			activeController = null;
		}
	}
	
	public boolean addListenerToActiveController(ControllerListener listener) {
		if(activeController != null) {
			activeController.addListener(listener);
			return true;
		}
		
		return false;
	}
	
	public boolean removeListenerToActiveController(ControllerListener listener) {
		if(activeController != null) {
			activeController.removeListener(listener);
			return true;
		}
		
		return false;
	}

// This code was taken from http://www.java-gaming.org/index.php?topic=29223.0
// With thanks that is!

public class XBox360Pad
{
    /*
     * It seems there are different versions of gamepads with different ID 
     Strings.
     * Therefore its IMO a better bet to check for:
     * if (controller.getName().toLowerCase().contains("xbox") &&
                   controller.getName().contains("360"))
     *
     * Controller (Gamepad for Xbox 360)
       Controller (XBOX 360 For Windows)
       Controller (Xbox 360 Wireless Receiver for Windows)
       Controller (Xbox wireless receiver for windows)
       XBOX 360 For Windows (Controller)
       Xbox 360 Wireless Receiver
       Xbox Receiver for Windows (Wireless Controller)
       Xbox wireless receiver for windows (Controller)
     */
    //public static final String ID = "XBOX 360 For Windows (Controller)";
    public static final int BUTTON_X = 2;
    public static final int BUTTON_Y = 3;
    public static final int BUTTON_A = 0;
    public static final int BUTTON_B = 1;
    public static final int BUTTON_BACK = 6;
    public static final int BUTTON_START = 7;
    /*public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;*/
    public static final int BUTTON_LB = 4;
    public static final int BUTTON_L3 = 8;
    public static final int BUTTON_RB = 5;
    public static final int BUTTON_R3 = 9;
    public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
    public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
    public static final int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
    public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
    public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
    public static final int AXIS_RIGHT_TRIGGER = 4; //value 0 to -1f
}


}
