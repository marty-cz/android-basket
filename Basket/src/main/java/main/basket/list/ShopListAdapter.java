package main.basket.list;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import main.basket.R;
import main.basket.list.structure.ShopListItem;

/**
 * Created by martin on 27.7.13.
 */
public class ShopListAdapter extends BaseListAdapter {

    public ShopListAdapter(Context context, ArrayList<ShopListItem> listData) {
        super(context, listData);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.shop_row_list_view_layout, null);
            holder = new ViewHolder();
            holder.iconView = (ImageView) convertView.findViewById(R.id.shop_icon);
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.itemCountView = (TextView) convertView.findViewById(R.id.item_count);
            holder.dateView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShopListItem item = (ShopListItem) listData.get(position);

        if (item.getDateFrom() == null)
            return convertView;
        // set the end date
        if (item.getDateTo() == null) {
            // and add six days
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getDateFrom());
            cal.add(Calendar.DAY_OF_YEAR, 6);
            item.setDateTo(cal.getTime());
        }

        Resources res = layoutInflater.getContext().getResources();

        holder.iconView.setImageDrawable(res.getDrawable(item.getShop().getIconId()));
        holder.headlineView.setText(item.getHeadLine());
        holder.itemCountView.setText(res.getString(R.string.items_label) + ": " + ((item.getSubItemCount() >= 0) ? item.getSubItemCount() : 0));
        holder.dateView.setText(df.format(item.getDateFrom().getTime()) + " - " + df.format(item.getDateTo().getTime()));

        return convertView;
    }

    static class ViewHolder {
        ImageView iconView;
        TextView headlineView;
        TextView itemCountView;
        TextView dateView;
    }

}