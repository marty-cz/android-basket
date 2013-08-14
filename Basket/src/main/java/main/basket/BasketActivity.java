package main.basket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import main.basket.dialogs.AddBasketItemDialog;
import main.basket.helper.MainHelper;
import main.basket.list.BasketListAdapter;
import main.basket.list.structure.BasketListItem;
import main.basket.list.structure.ShopListItem;

/** Created by martin on 29.7.13. */
public class BasketActivity extends BaseActivity {

  protected ShopListItem shop;

  protected void setBasket(ArrayList<BasketListItem> basket) {
    shop.setBasket((ArrayList<BasketListItem>) validateAndSortList(
        (basket == null) ? new ArrayList<BasketListItem>() : basket) );
    adapter.setListData(shop.getBasket());
    adapter.notifyDataSetChanged();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basket);
    int i = (int) getIntent().getSerializableExtra("weekNo");
    int j = (int) getIntent().getSerializableExtra("shopNo");
    shop = WeekActivity.weeks.getWeeks().get(i).getShops().get(j);

    TextView tv = (TextView) findViewById(R.id.position_label);
    tv.setText(shop.getHeadLine() + " (" + df.format(shop.getDateFrom().getTime())
        + " - " + df.format(shop.getDateTo().getTime()) + ")");

    shop.setBasket((ArrayList<BasketListItem>) validateAndSortList(shop.getBasket()));

    listView = (ListView) findViewById(R.id.lv_basket);
    listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    adapter = new BasketListAdapter(BasketActivity.this, shop.getBasket());

    listView.setAdapter(adapter);
    listView.setItemsCanFocus(true);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selectedItem == position) {
          editButtonClick();
        } else {
          selectedItem = position;
          showOpEditRemove = true;
          MainHelper.invalidateOptionsMenu(BasketActivity.this);
        }
      }
    });
    listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        showOpEditRemove = false;
        MainHelper.invalidateOptionsMenu(BasketActivity.this);
      }
    });
  }

  @Override
  protected void editButtonClick() {
    int i = listView.getCheckedItemPosition();
    if (i >= 0) {
      final BasketListItem item = shop.getBasket().get(i);
      AddBasketItemDialog dialog = new AddBasketItemDialog(this, item, R.style.DialogSlideAnim);
      dialog.setPositiveButtonAction(new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          setBasket(shop.getBasket());
        }
      });
      dialog.show();
    }
  }

  @Override
  protected void addButtonClick() {
    AddBasketItemDialog dialog = new AddBasketItemDialog(this, null, R.style.DialogSlideAnim);
    dialog.setPositiveButtonAction(new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        BasketListItem item = ((AddBasketItemDialog) dialog).getBasketItem();
        shop.addBasketItem(item);
        setBasket(shop.getBasket());

        listView.setItemChecked(shop.getBasket().indexOf(item), true);
        showOpEditRemove = true;
        MainHelper.invalidateOptionsMenu(BasketActivity.this);
      }
    });
    dialog.show();
  }

  @Override
  protected void removeButtonClick() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.are_u_sure)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            int i = listView.getCheckedItemPosition();
            if (i >= 0) {
              shop.removeBasketItem(i);
              setBasket(shop.getBasket());
              int size = shop.getBasket().size();
              if (size > 0) {
                if (i >= size) {
                  i = size - 1;
                }
                listView.setItemChecked(i, true);
                showOpEditRemove = true;
              } else {
                showOpEditRemove = false;
              }
              MainHelper.invalidateOptionsMenu(BasketActivity.this);
            }
          }
        })
        .setNegativeButton(android.R.string.no, null)
        .show();
  }

  @Override
  protected ArrayList<?> validateAndSortList(ArrayList<?> list) {
    ArrayList<BasketListItem> l = (ArrayList<BasketListItem>) list;
    // sort by date desc
    Collections.sort(l, new Comparator<BasketListItem>() {
      @Override
      public int compare(BasketListItem lhs, BasketListItem rhs) {
        int res = lhs.getHeadLine().compareTo(rhs.getHeadLine());
        if (res == 0) {
          res = (Double.compare(lhs.getPrice(), rhs.getPrice()));
        }
        return res;
      }
    });
    return list;
  }

}