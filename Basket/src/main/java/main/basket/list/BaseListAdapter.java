package main.basket.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/** Created by martin on 27.7.13. */
public abstract class BaseListAdapter extends BaseAdapter {

  protected ArrayList<?> listData;
  protected Context context;
  protected LayoutInflater layoutInflater;
  protected SimpleDateFormat df = new SimpleDateFormat("dd. MMM yy");

  public BaseListAdapter(Context context, ArrayList<?> listData) {
    this.listData = listData;
    this.context = context;
    layoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return listData.size();
  }

  @Override
  public Object getItem(int position) {
    return listData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public ArrayList<?> getListData() {
    return listData;
  }

  public void setListData(ArrayList<?> listData) {
    this.listData = listData;
  }

  @Override
  public abstract View getView(int position, View convertView, ViewGroup parent);

}