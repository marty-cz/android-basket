package main.basket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import main.basket.enums.ShopsEnum;
import main.basket.helper.MainHelper;
import main.basket.list.BaseListAdapter;
import main.basket.list.ShopEnumListAdapter;
import main.basket.list.ShopListAdapter;
import main.basket.list.structure.ShopListItem;
import main.basket.list.structure.WeekListItem;

/** Created by martin on 28.7.13. */
public abstract class BaseActivity extends Activity {

  protected boolean showOpEditRemove = false;
  protected BaseListAdapter adapter = null;
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
      if ((mi.getItemId() == R.id.action_edit) || (mi.getItemId() == R.id.action_remove))
        mi.setVisible(showOpEditRemove);
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