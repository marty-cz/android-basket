package main.basket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import main.basket.enums.ShopsEnum;
import main.basket.helper.MainHelper;
import main.basket.list.ShopEnumListAdapter;
import main.basket.list.ShopListAdapter;
import main.basket.list.structure.ShopListItem;
import main.basket.list.structure.WeekListItem;

/** Created by martin on 28.7.13. */
public class ShopActivity extends BaseActivity {

  private WeekListItem week;

  protected void setShops(ArrayList<ShopListItem> shops) {
    week.setShops((ArrayList<ShopListItem>) validateAndSortList(
        (shops == null) ? new ArrayList<ShopListAdapter>() : shops) );
    adapter.setListData(week.getShops());
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shop);
    int i = (int) getIntent().getSerializableExtra("weekNo");
    week = WeekActivity.weeks.getWeeks().get(i);

    TextView tv = (TextView) findViewById(R.id.position_label);
    tv.setText(week.getHeadLine() + " (" + df.format(week.getDateFrom().getTime())
        + " - " + df.format(week.getDateTo().getTime()) + ")");

    week.setShops((ArrayList<ShopListItem>) validateAndSortList(week.getShops()));

    listView = (ListView) findViewById(R.id.lv_shop);
    adapter = new ShopListAdapter(ShopActivity.this, week.getShops());

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
          MainHelper.invalidateOptionsMenu(ShopActivity.this);
        }
      }
    });
    listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        showOpEditRemove = false;
        MainHelper.invalidateOptionsMenu(ShopActivity.this);
      }
    });
  }

  /** Called when the user clicks the Edit button */
  public void editButtonClick() {
    // open
 //      Toast.makeText(ShopActivity.this, "open shop", Toast.LENGTH_SHORT).show();

    Intent myIntent = new Intent(ShopActivity.this, BasketActivity.class);
   // myIntent.putExtra("shop", week.getShops().get(listView.getCheckedItemPosition())); //Optional parameters
    myIntent.putExtra("weekNo", (int) getIntent().getSerializableExtra("weekNo")); //Optional parameters
    myIntent.putExtra("shopNo", listView.getCheckedItemPosition()); //Optional parameters
    ShopActivity.this.startActivity(myIntent);
  }

  /** Called when the user clicks the Add button */
  @Override
  protected void addButtonClick() {
    final ShopEnumListAdapter shopAdapter = new ShopEnumListAdapter(ShopActivity.this,
        new ArrayList<ShopsEnum>(Arrays.asList(ShopsEnum.values())));

    ArrayList<ShopsEnum> rmList = new ArrayList<ShopsEnum>();
    for (ShopListItem item : week.getShops()) {
      rmList.add(item.getShop());
    }
    shopAdapter.getListData().removeAll(rmList);

    AlertDialog.Builder b = new AlertDialog.Builder(this);
    b.setTitle(R.string.choose_shop);
    b.setAdapter(shopAdapter, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        ShopsEnum shop = (ShopsEnum) shopAdapter.getItem(which);

        Calendar cal = Calendar.getInstance();
        cal.setTime(week.getDateFrom());
        while (cal.get(Calendar.DAY_OF_WEEK) != shop.getBeggingDay()) {
          cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        ShopListItem item = new ShopListItem(shop, cal.getTime(), null);
        week.addShop(item);
        setShops(week.getShops());

        listView.setItemChecked(week.getShops().indexOf(item), true);
        showOpEditRemove = true;
        MainHelper.invalidateOptionsMenu(ShopActivity.this);
      }
    });
    b.show();
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
              week.removeShop(i);
              setShops(week.getShops());
              int size = week.getShops().size();
              if (size > 0) {
                if (i >= size) {
                  i = size - 1;
                }
                listView.setItemChecked(i, true);
                showOpEditRemove = true;
              } else {
                showOpEditRemove = false;
              }
              MainHelper.invalidateOptionsMenu(ShopActivity.this);
            }
          }
        })
        .setNegativeButton(android.R.string.no, null)
        .show();
  }

  @Override
  protected ArrayList<?> validateAndSortList(ArrayList<?> list) {
    ArrayList<ShopListItem> l = (ArrayList<ShopListItem>) list;
    // sort by date desc
    Collections.sort(l, new Comparator<ShopListItem>() {
      @Override
      public int compare(ShopListItem lhs, ShopListItem rhs) {
        return lhs.getShop().getName().compareTo(rhs.getShop().getName());
      }
    });
    return list;
  }

}