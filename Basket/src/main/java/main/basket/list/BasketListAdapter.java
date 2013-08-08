package main.basket.list;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import main.basket.R;
import main.basket.list.structure.BasketListItem;

/** Created by martin on 27.7.13. */
public class BasketListAdapter extends BaseListAdapter {

  public BasketListAdapter(Context context, ArrayList<BasketListItem> listData) {
    super(context, listData);
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.basket_row_list_view_layout, null);
      holder = new ViewHolder();
      holder.headlineView  = (TextView) convertView.findViewById(R.id.title);
      holder.priceView = (TextView) convertView.findViewById(R.id.price);
      holder.buyedCheckBox = (CheckBox) convertView.findViewById(R.id.buyed);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    BasketListItem item = (BasketListItem) listData.get(position);

    Resources res = layoutInflater.getContext().getResources();

    holder.headlineView.setText(item.getHeadLine());
    holder.priceView.setText(String.format("%4.2f ",item.getPrice()) + context.getString(R.string.price_unit));
    holder.buyedCheckBox.setChecked(item.isBuyed());

    return convertView;
  }

  static class ViewHolder {
    TextView headlineView;
    TextView priceView;
    CheckBox buyedCheckBox;
  }

}