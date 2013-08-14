package main.basket;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;

import main.basket.tools.FileConnectionTask;
import main.basket.helper.MainHelper;
import main.basket.list.WeekListAdapter;
import main.basket.list.structure.StructureWrapper;
import main.basket.list.structure.WeekListItem;
import main.basket.tools.Serializer;

/** Created by martin on 27.7.13. */
public class WeekActivity extends BaseActivity {

  public static StructureWrapper weeks = null;
  public static String basketFileName = "basket.json";

  public void setWeeks(StructureWrapper weeks) {
    WeekActivity.weeks = (weeks == null) ? new StructureWrapper() : weeks;
    WeekActivity.weeks.setWeeks((ArrayList<WeekListItem>) validateAndSortList(WeekActivity.weeks.getWeeks()));
    adapter.setListData(weeks.getWeeks());
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_week);

    listView = (ListView) findViewById(R.id.lv_week);
    weeks   = new StructureWrapper();
    adapter = new WeekListAdapter(WeekActivity.this, weeks.getWeeks());

    listView.setAdapter(adapter);
    listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selectedItem == position) {
          editButtonClick();
        } else {
          selectedItem = position;
          showOpEditRemove = true;
          MainHelper.invalidateOptionsMenu(WeekActivity.this);
        }
      }
    });
    listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        showOpEditRemove = false;
        MainHelper.invalidateOptionsMenu(WeekActivity.this);
      }
    });
    new FileConnectionTask(WeekActivity.this, false, true).execute(getDownloadStorageDirPath() + "/" + basketFileName);
  }

  /** Called when the user clicks the Add button */
  public void addButtonClick() {
    Calendar cal = Calendar.getInstance();
    Dialog d = new DatePickerDialog(WeekActivity.this, new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            WeekListItem item = new WeekListItem("week", new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime());
            weeks.addWeek(item);
            setWeeks(weeks);

            listView.setItemChecked(findWeekIndexInList(weeks.getWeeks(), item), true);
            showOpEditRemove = true;
            MainHelper.invalidateOptionsMenu(WeekActivity.this);
          }
        },
        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    d.show();
  }

  /** Called when the user clicks the Remove button */
  @Override
  protected void removeButtonClick() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.are_u_sure)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            int i = listView.getCheckedItemPosition();
            if (i >= 0) {
              weeks.removeWeek(i);
              setWeeks(weeks);
              int size = weeks.getWeeks().size();
              if (size > 0) {
                if (i >= size) {
                  i = size - 1;
                }
                listView.setItemChecked(i, true);
                showOpEditRemove = true;
              } else {
                showOpEditRemove = false;
              }
              MainHelper.invalidateOptionsMenu(WeekActivity.this);
            }
          }
        })
        .setNegativeButton(android.R.string.no, null)
        .show();
  }

  /** Called when the user clicks the Edit button */
  @Override
  protected void editButtonClick() {
    // open
    Intent myIntent = new Intent(WeekActivity.this, ShopActivity.class);
    // myIntent.putExtra("week", weeks.getWeeks().get(listView.getCheckedItemPosition())); //Optional parameters
    myIntent.putExtra("weekNo", listView.getCheckedItemPosition()); //Optional parameters
    WeekActivity.this.startActivity(myIntent);
  }

  @Override
  protected ArrayList<?> validateAndSortList(ArrayList<?> list) {
    // sort by date desc
    ArrayList<WeekListItem> l = (ArrayList<WeekListItem>) list;
    Collections.sort(l, new Comparator<WeekListItem>() {
      @Override
      public int compare(WeekListItem lhs, WeekListItem rhs) {
        if (rhs.getDateFrom() == null) {
          return (lhs.getDateFrom() == null) ? 0 : 1;
        }
        return rhs.getDateFrom().compareTo(lhs.getDateFrom());
      }
    });
    // remove duplicate
    int lastWeek = 0;
    int lastYear = 0;
    Iterator<WeekListItem> it = l.iterator();
    while (it.hasNext()) {
      WeekListItem item = it.next();
      if (item.getDateFrom() == null) {
        it.remove();
      } else {
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.getDateFrom());
        if (cal.get(Calendar.YEAR) != lastYear && cal.get(Calendar.WEEK_OF_YEAR) == lastWeek) {
          it.remove();
        } else {
          lastWeek = cal.get(Calendar.WEEK_OF_YEAR);
          lastYear = cal.get(Calendar.YEAR);
        }
      }
    }
    return list;
  }


  private int findWeekIndexInList(ArrayList<WeekListItem> list, WeekListItem item) {
    int index = list.indexOf(item);

    if (index < 0) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(item.getDateFrom());
      int week = cal.get(Calendar.WEEK_OF_YEAR);
      int year = cal.get(Calendar.YEAR);

      for (int i = 0; i < list.size(); i++) {
        cal.setTime(list.get(i).getDateFrom());
        if (cal.get(Calendar.WEEK_OF_YEAR) == year && cal.get(Calendar.WEEK_OF_YEAR) == week) {
          return i;
        }
      }
    }
    return index;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    boolean res = super.onCreateOptionsMenu(menu);
    if (res) {
      for (int i = 0; i < menu.size(); i++) {
        MenuItem mi = menu.getItem(i);
        if ((mi.getItemId() == R.id.action_download) || (mi.getItemId() == R.id.action_upload)) {
          mi.setVisible(true);
        }
      }
    }
    return res;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_upload:
        new FileConnectionTask(WeekActivity.this,  true, false).execute(getDownloadStorageDirPath() + "/" + basketFileName);
        return true;
      case R.id.action_download:
        new FileConnectionTask(WeekActivity.this, false, false).execute(getDownloadStorageDirPath() + "/" + basketFileName);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  protected String getDownloadStorageDirPath() {
    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    if (file.exists() || file.mkdirs()) {
      return file.getAbsolutePath();
    }
    MainHelper.showCustomToast(WeekActivity.this, getString(R.string.err_dir_download), false);
    return "";
  }

}