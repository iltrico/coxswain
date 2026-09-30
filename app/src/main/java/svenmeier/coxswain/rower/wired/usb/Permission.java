package svenmeier.coxswain.rower.wired.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.core.content.ContextCompat;

/**
 * Request permission to use a {@link UsbDevice}.
 * <p>
 * Not used currently, since we restart an activity for intent USB_DEVICE_ATTACHED and
 * Android will ask the user for permissions automatically.
 */
public class Permission extends BroadcastReceiver {

	private static final String ACTION_USB_PERMISSION = "svenmeier.coxswain.USB_PERMISSION";

	private Context context;

	public Permission(Context context) {
		this.context = context;

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		ContextCompat.registerReceiver(context, this, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
	}

	public void destroy() {
		context.unregisterReceiver(this);
		context = null;
	}

	public void request(UsbDevice device) {
		UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

		manager.requestPermission(device, PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE));
	}

	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ACTION_USB_PERMISSION.equals(action)) {
			onRequested(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false));
		}
	}

	protected void onRequested(boolean granted) {
	}
}