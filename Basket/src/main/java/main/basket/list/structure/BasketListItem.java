package main.basket.list.structure;

/**
 * Created by martin on 29.7.13.
 */
public class BasketListItem extends CommonListItem {

    protected double price;
    protected boolean buyed;

    public BasketListItem(String name, double price) {
        this(name, price, false);
    }

    public BasketListItem(String name, double price, boolean buyed) {
        super(name, "");
        this.price = (price < 0.0) ? 0.0 : price;
        this.buyed = buyed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBuyed() {
        return buyed;
    }

    public void setBuyed(boolean buyed) {
        this.buyed = buyed;
    }

    @Override
    public int getSubItemCount() {
        return -1;
    }
}
