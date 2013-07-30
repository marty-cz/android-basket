package main.basket.list.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import main.basket.enums.ShopsEnum;

/** Created by martin on 28.7.13. */
public class ShopListItem extends CommonListItem implements Serializable {

  protected ShopsEnum shop = null;
  protected ArrayList<BasketListItem> basket;

  public ShopListItem(ShopsEnum shop, Date dateFrom, Date dateTo) {
    this(shop, null, dateFrom, dateTo);
  }

  public ShopListItem(ShopsEnum shop, ArrayList<BasketListItem> basket, Date dateFrom, Date dateTo) {
    super(shop.getName(), ((basket != null) ? basket.size() : 0), dateFrom, dateTo);
    this.shop = shop;
    this.basket = (basket != null) ? basket : new ArrayList<BasketListItem>();
  }

  public ShopsEnum getShop() {
    return shop;
  }

  public void setShop(ShopsEnum shop) {
    this.shop = shop;
  }

  public ArrayList<BasketListItem> getBasket() {
    return basket;
  }

  public void setBasket(ArrayList<BasketListItem> basket) {
    this.basket = basket;
  }


  public boolean addBasketItem(BasketListItem item) {
    return basket.add(item);
  }
  /*
    public boolean addBasketItem(int location, BasketListItem item) {
      return basket.add(location, item);
    }
  */
  public boolean removeBasketItem(BasketListItem item) {
    return basket.remove(item);
  }

  public BasketListItem removeBasketItem(int index) {
    return basket.remove(index);
  }

  @Override
  public int getSubItemCount() {
    return (basket != null) ? basket.size() : 0;
  }

  @Override
  public void setSubItemCount(int subItemCount) {
  }
}
