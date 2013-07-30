package main.basket.helper;


import android.app.Activity;
import android.os.Build;

/** Created by martin on 28.7.13. */
public class MainHelper {

  public static boolean invalidateOptionsMenu(Activity activity) {
    if (Build.VERSION.SDK_INT >= 11) {
      activity.invalidateOptionsMenu();
      return true;
    }
    return false;
  }

}
