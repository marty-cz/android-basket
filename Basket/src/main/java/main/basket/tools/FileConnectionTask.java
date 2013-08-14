package main.basket.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;

import main.basket.WeekActivity;
import main.basket.helper.MainHelper;
import main.basket.list.structure.StructureWrapper;
import main.basket.R;

/** Created by martin on 7.8.13. */
public class FileConnectionTask extends AsyncTask<String, Integer, Object> {

  private ProgressDialog dialog;
  private Activity srcActivity;
  private boolean upload;
  private boolean save;
  private String message;
  private boolean connectionPart;
  private StructureWrapper deserializedObj = null;
  private int retValue = -1;  // 0 means ok, everything else is failure

  public FileConnectionTask(Activity activity, boolean upload, boolean save) {
    srcActivity = activity;
    this.upload = upload;
    this.save = save;
    message = "...";
  }

  @Override
  protected void onPreExecute() {
    dialog = new ProgressDialog(srcActivity);
    Drawable mDrawable = srcActivity.getResources().getDrawable(android.R.drawable.ic_popup_sync);
  //  TextView tv = (TextView) dialog.findViewById(android.R.id.title);
    // color = CurrentColor - (WhiteColor+1)
    // color = 16711680-(17677215+1)  => color < 0 !!
    // CurentColor = color + (WhiteColor+1)
  //  int color = tv.getCurrentTextColor() + (17677215 + 1);
    mDrawable.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);

    dialog.setIcon(mDrawable);
    dialog.setTitle(srcActivity.getString(R.string.sync));
    dialog.setMessage(message);
    dialog.setIndeterminate(false);
    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    dialog.setProgress(0);
    dialog.setCancelable(false);
    dialog.show();
  }

  @Override
  protected Object doInBackground(String... params) {
    retValue = -1;
    try {
      boolean res = false;
      if (upload) {
        connectionPart = false;
        message = srcActivity.getString(R.string.serializing);
       srcActivity.runOnUiThread(new ChangeMsgAndStyle(message, ProgressDialog.STYLE_SPINNER));
        res = Serializer.getInstance().serializeToJson(params[0], WeekActivity.weeks, StructureWrapper.class);
        if (!save && res) {
          connectionPart = true;
          message = srcActivity.getString(R.string.uploading);
          srcActivity.runOnUiThread(new ChangeMsgAndStyle(message, ProgressDialog.STYLE_HORIZONTAL));
          res = Connection.getInstance().sendFile(params[0], this);
        }
      } else {
        if (!save) {
          connectionPart = true;
          message = srcActivity.getString(R.string.downloading);
          srcActivity.runOnUiThread(new ChangeMsgAndStyle(message, ProgressDialog.STYLE_HORIZONTAL));
          res = Connection.getInstance().receiveFile(params[0], this);
        } else {
          res = true;
        }
        if (res) {
          connectionPart = false;
          message = srcActivity.getString(R.string.deserializing);
          srcActivity.runOnUiThread(new ChangeMsgAndStyle(message, ProgressDialog.STYLE_SPINNER));
          deserializedObj = (StructureWrapper) Serializer.getInstance().deserializeFromJson(params[0],
                                 StructureWrapper.class);
          res = (deserializedObj != null);
        }
      }
      return res;
    } catch (Exception e) {
      return e;
    }
  }

  public void setProgress(final Integer... values) {
    publishProgress(values);
  }

  public void setMax(final int max) {
    srcActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        dialog.setMax(max);
      }
    });
  }

  public void setUnit(final String unit) {
    srcActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (Build.VERSION.SDK_INT >= 11) {
          dialog.setProgressNumberFormat("%1d/%2d " + unit);
        } else {
          dialog.setMessage(message + " (" + srcActivity.getString(R.string.in) + " " + unit + ")");
        }
      }
    });
  }

  @Override
  protected void onProgressUpdate(final Integer... progress) {
    srcActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        dialog.setProgress(progress[0]);
      }
    });
  }

  @Override
  protected void onPostExecute(Object result) {
    if (dialog.isShowing()) {
      dialog.dismiss();
    }

    String msg = "";
    String msgFirst = "";
    retValue = -1;
    if (result instanceof Exception) {
      msg = ((Exception) result).getLocalizedMessage();
    } else if (result instanceof Boolean) {
      Connection con = Connection.getInstance();
      Serializer ser = Serializer.getInstance();
      if (save) {
        if (upload) {
          msg = ((Boolean)result) ? srcActivity.getString(R.string.save_ok) : ser.getErrString();
        } else {
          msg = ((Boolean)result) ? srcActivity.getString(R.string.load_ok) : ser.getErrString();
        }
        retValue   = ((Boolean)result) ? 0 : -1;
      } else if (connectionPart) {
        msg        = con.getResponseStr();
        retValue   = con.getResponseCode();
        if ((Boolean)result) {
          retValue = 0;
          if (upload == false) { // download
            msg = srcActivity.getString(R.string.down_ok);
          } else if (con.getResponseCode() == 200) { // upload
            msg = srcActivity.getString(R.string.sync_ok);
          } else { // error in upload
            msgFirst = srcActivity.getString(R.string.save_ok);
            retValue = con.getResponseCode();
            retValue = (retValue == 0) ? -1 : retValue;
          }
        } else { // error (exception catch)
          if (upload) {
            msgFirst = srcActivity.getString(R.string.save_ok);
          }
          retValue = (retValue == 0) ? -1 : retValue;
        }
      } else {
        msg        = ((Boolean)result) ? srcActivity.getString(R.string.sync_ok) : ser.getErrString();
        retValue   = ((Boolean)result) ? 0 : -1;
      }

      if ((upload == false) && (retValue == 0) && (srcActivity instanceof WeekActivity)) {
        ((WeekActivity) srcActivity).setWeeks(deserializedObj);
      }
    } else {
      msg = srcActivity.getString(R.string.err_unknown_result);
    }
    MainHelper.showCustomToast(srcActivity, msgFirst, msg, (retValue == 0));
  }

  public int getResultValue() {
    return retValue;
  }



  private class ChangeMsgAndStyle implements Runnable {

    private String message = "";
    private int style;

    private ChangeMsgAndStyle(String message, int style) {
      this.message = message;
      this.style = style;
    }

    @Override
    public void run() {
      dialog.setMessage(message);
      ProgressBar bar = (ProgressBar) dialog.findViewById(android.R.id.progress);
      if (bar != null) {
        if (style == ProgressDialog.STYLE_SPINNER) {
          bar.setIndeterminate(true);
          if (Build.VERSION.SDK_INT >= 11) {
            dialog.setProgressNumberFormat(null);
            dialog.setProgressPercentFormat(null);
          }
        } else {
          bar.setIndeterminate(false);
          if (Build.VERSION.SDK_INT >= 11) {
            dialog.setProgressNumberFormat("%1d/%2d");
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMaximumFractionDigits(0);
            dialog.setProgressPercentFormat(nf);
          }
        }
      }
    }
  }
}