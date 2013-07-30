package main.basket.list;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import main.basket.R;
import main.basket.enums.ShopsEnum;
import main.basket.list.structure.ShopListItem;

/** Created by martin on 27.7.13. */
public class ShopEnumListAdapter extends BaseListAdapter {

  public ShopEnumListAdapter(Context context, ArrayList<ShopsEnum> listData) {
    super(context, listData);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.shop_enum_row_list_view_layout, null);
      holder = new ViewHolder();
      holder.iconView      = (ImageView) convertView.findViewById(R.id.shop_icon);
      holder.headlineView  = (TextView) convertView.findViewById(R.id.title);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    ShopsEnum item = (ShopsEnum) listData.get(position);

    Resources res = layoutInflater.getContext().getResources();

    holder.iconView.setImageDrawable(res.getDrawable(item.getIconId()));
    holder.headlineView.setText(item.getName());

    return convertView;
  }

  static class ViewHolder {
    ImageView iconView;
    TextView headlineView;
  }

}