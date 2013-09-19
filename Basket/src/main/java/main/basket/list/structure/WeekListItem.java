package main.basket.list.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by martin on 28.7.13.
 */
public class WeekListItem extends CommonListItem implements Serializable {

    protected ArrayList<ShopListItem> shops;

    public WeekListItem(String headLine, Date dateFrom) {
        this(headLine, null, dateFrom);
    }

    public WeekListItem(String headLine, ArrayList<ShopListItem> shops, Date dateFrom) {
        super(headLine, dateFrom);
        this.shops = (shops != null) ? shops : new ArrayList<ShopListItem>();
    }

    public ArrayList<ShopListItem> getShops() {
        return shops;
    }

    public void setShops(ArrayList<ShopListItem> shops) {
        this.shops = shops;
    }

    public boolean addShop(ShopListItem item) {
        return shops.add(item);
    }

    public boolean removeShop(ShopListItem item) {
        return shops.remove(item);
    }

    public ShopListItem removeShop(int index) {
        return shops.remove(index);
    }

    @Override
    public int getSubItemCount() {
        return (shops != null) ? shops.size() : 0;
    }

}
