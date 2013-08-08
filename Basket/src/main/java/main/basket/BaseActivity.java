package main.basket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import main.basket.list.BaseListAdapter;

/** Created by martin on 28.7.13. */
public abstract class BaseActivity extends Activity {

  protected boolean showOpEditRemove = false;
  protected BaseListAdapter adapter = null;
  protected SimpleDateFormat df = new SimpleDateFormat("d. MMM");
  protected int selectedItem = -1;
  protected ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (adapter != null) {
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    for (int i = 0; i < menu.size(); i++) {
      MenuItem mi = menu.getItem(i);
      if ((mi.getItemId() == R.id.action_edit) || (mi.getItemId() == R.id.action_remove)) {
        mi.setVisible(showOpEditRemove);
      }
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_add:
        addButtonClick();
        return true;
      case R.id.action_remove:
        removeButtonClick();
        return true;
      case R.id.action_edit:
        editButtonClick();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /** Called when the user clicks the Edit button */
  protected abstract void editButtonClick();

  /** Called when the user clicks the Add button */
  protected abstract void addButtonClick();

  /** Called when the user clicks the Remove button */
  protected abstract void removeButtonClick();

  protected abstract ArrayList<?> validateAndSortList(ArrayList<?> list);

}