package org.apache.cordova.plugin.Brightness;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;

/**
 * @author Evgeniy Lukovsky
 *
 */
public class BrightnessPlugin extends CordovaPlugin {
	public enum Action{
		setBrightness,
		getBrightness
	}
	
	private class SetTask implements Runnable{
		private Activity target = null;
		private LayoutParams lp = null;
		@Override
		public void run() {
			target.getWindow().setAttributes(lp);
		}
		public void setParams(Activity act, LayoutParams params){
			this.target = act;
			this.lp = params;
		}
	}


	/* (non-Javadoc)
	 * @see org.apache.cordova.CordovaPlugin#execute(java.lang.String, org.json.JSONArray, org.apache.cordova.CallbackContext)
	 */
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		System.out.println("plugin has been started");
		boolean result = false;

		switch(Action.valueOf(action)){
		case setBrightness: result = true;
			setBrightness(args, callbackContext);
			break;
		case getBrightness: result = true;
			getBrightness(args, callbackContext);
			break;
		}
		return result;
	}

	/**
	 * @param args
	 * @param callbackContext
	 * @return
	 */
	private boolean setBrightness(JSONArray args, CallbackContext callbackContext) {
		try {
			Activity activity = cordova.getActivity();
			WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
			String value = args.getString(0);
			double brightness = Double.parseDouble(value);
			layoutParams.screenBrightness = (float) brightness;
			SetTask task = new SetTask();
			task.setParams(activity, layoutParams);
			activity.runOnUiThread(task);
			callbackContext.success("OK");

		} catch (NullPointerException e) {
			System.out.println("Null pointer exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		} catch (JSONException e) {
			System.out.println("JSONException exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
		System.out.println("All went fine.");
		return true;
	}

	/**
	 * @param args
	 * @param callbackContext
	 * @return
	 */
	private boolean getBrightness(JSONArray args, CallbackContext callbackContext) {
		try {
			Activity activity = cordova.getActivity();
			WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
			Double brightness = (double) layoutParams.screenBrightness;
			callbackContext.success(brightness.toString());

		} catch (NullPointerException e) {
			System.out.println("Null pointer exception");
			System.out.println(e.getMessage());
			callbackContext.error(e.getMessage());
			return false;
		}
		System.out.println("All went fine.");
		return true;
	}

}