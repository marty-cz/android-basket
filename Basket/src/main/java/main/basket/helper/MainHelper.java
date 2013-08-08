package main.basket.helper;


import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import main.basket.R;

/** Created by martin on 28.7.13. */
public class MainHelper {

  public static boolean invalidateOptionsMenu(Activity activity) {
    if (Build.VERSION.SDK_INT >= 11) {
      activity.invalidateOptionsMenu();
      return true;
    }
    return false;
  }

  public static void showCustomToast(Activity srcActivity, String msg, boolean ok) {
    showCustomToast(srcActivity, "", msg, ok);
  }

  public static void showCustomToast(Activity srcActivity, String msgFirst, String msg, boolean ok) {
    LayoutInflater inflater = srcActivity.getLayoutInflater();
    View layout = inflater.inflate(R.layout.custom_toast,
        (ViewGroup) srcActivity.findViewById(R.id.toast_layout_root));

    ImageView img = (ImageView) layout.findViewById(R.id.image);
    img.setImageResource((ok) ? R.drawable.ic_thumb_up : R.drawable.ic_thumb_down);

    TextView text = (TextView) layout.findViewById(R.id.text);
    text.setText(msg);

    TextView textFirst = (TextView) layout.findViewById(R.id.text_first);
    if (msgFirst == null || msgFirst.isEmpty()) {
      textFirst.setVisibility(View.GONE);
      textFirst.setText("");
    } else {
      textFirst.setVisibility(View.VISIBLE);
      textFirst.setText(msgFirst);
      if (ok == false) {
        text.setTextColor(Color.rgb(255, 48, 48));
      }
    }

    Toast toast = new Toast(srcActivity);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setView(layout);
    toast.show();
  }

}
