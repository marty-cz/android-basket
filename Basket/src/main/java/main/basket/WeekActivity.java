package main.basket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;

import java.nio.channels.ScatteringByteChannel;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import main.basket.helper.MainHelper;
import main.basket.list.WeekListAdapter;
import main.basket.list.structure.ShopListItem;
import main.basket.list.structure.WeekListItem;

/** Created by martin on 27.7.13. */
public class WeekActivity extends BaseActivity {

  private ArrayList<WeekListItem> weekList = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_week);

    weekList = (ArrayList<WeekListItem>) validateAndSortList(getListData());
    listView = (ListView) findViewById(R.id.lv_week);
    adapter = new WeekListAdapter(WeekActivity.this, weekList);

    listView.setAdapter(adapter);
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
  }

  /** Called when the user clicks the Add button */
  public void addButtonClick() {
    Calendar cal = Calendar.getInstance();
    Dialog d = new DatePickerDialog(WeekActivity.this, new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            WeekListItem item = new WeekListItem("week", new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime());
            weekList.add(item);
            weekList = (ArrayList<WeekListItem>) validateAndSortList(weekList);
            adapter.setListData(weekList);
            listView.setAdapter(adapter);

            listView.setItemChecked(findWeekIndexInList(weekList, item), true);
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
    builder.setMessage("Are you sure?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            int i = listView.getCheckedItemPosition();
            if (i >= 0) {
              weekList.remove(i);
              adapter.setListData(weekList);
              listView.setAdapter(adapter);
              if (weekList.size() > 0) {
                if (i >= weekList.size()) {
                  i = weekList.size() - 1;
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
        .setNegativeButton("No", null)
        .show();
  }

  /** Called when the user clicks the Edit button */
  @Override
  protected void editButtonClick() {
    // open
 //   Toast.makeText(WeekActivity.this, "open shop", Toast.LENGTH_SHORT).show();

    Intent myIntent = new Intent(WeekActivity.this, ShopActivity.class);
    myIntent.putExtra("week", weekList.get(listView.getCheckedItemPosition())); //Optional parameters
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
          if (lhs.getDateFrom() == null) {
            return 0;
          }
          return 1;
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

  private ArrayList<WeekListItem> getListData() {
    ArrayList<WeekListItem> results = new ArrayList<WeekListItem>();
    Calendar today = Calendar.getInstance();

    WeekListItem newsData = new WeekListItem("28. týden", today.getTime());
    results.add(newsData);

    today.add(Calendar.DATE, -7);
    newsData = new WeekListItem("29. týden", today.getTime());
    results.add(newsData);

    today.add(Calendar.DATE, 14);
    newsData = new WeekListItem("29. týden", today.getTime());
    results.add(newsData);

    today.add(Calendar.DATE, -28);
    newsData = new WeekListItem("29. týden", today.getTime());
    results.add(newsData);

    return results;
  }
}