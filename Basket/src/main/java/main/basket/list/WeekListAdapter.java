package main.basket.list;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import main.basket.R;
import main.basket.list.structure.WeekListItem;

/** Created by martin on 27.7.13. */
public class WeekListAdapter extends BaseListAdapter {

  public WeekListAdapter(Context context, ArrayList<WeekListItem> listData) {
    super(context, listData);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      convertView = layoutInflater.inflate(R.layout.week_row_list_view_layout, null);
      holder = new ViewHolder();
      holder.headlineView  = (TextView) convertView.findViewById(R.id.title);
      holder.itemCountView = (TextView) convertView.findViewById(R.id.item_count);
      holder.dateView      = (TextView) convertView.findViewById(R.id.date);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    WeekListItem item = (WeekListItem) listData.get(position);

    if (item.getDateFrom() == null)
      return convertView;
    // set the date
    Calendar cal = Calendar.getInstance();
    cal.setTime(item.getDateFrom());

    Resources res = layoutInflater.getContext().getResources();

    holder.headlineView.setText(cal.get(Calendar.WEEK_OF_YEAR) + ". " + res.getString(R.string.week_label));
    holder.itemCountView.setText(res.getString(R.string.shops_label) + ": " + ((item.getSubItemCount() >= 0) ? item.getSubItemCount() : 0));

    // "calculate" the start date of the week
    Calendar first = (Calendar) cal.clone();
    first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
    // and add six days to the end date
    Calendar last = (Calendar) first.clone();
    last.add(Calendar.DAY_OF_YEAR, 6);

    holder.dateView.setText(df.format(first.getTime()) + " - " + df.format(last.getTime()));

    return convertView;
  }

  static class ViewHolder {
    TextView headlineView;
    TextView itemCountView;
    TextView dateView;
  }

}