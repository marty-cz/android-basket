package main.basket.enums;

import java.util.Calendar;

import main.basket.R;

/**
 * Created by martin on 28.7.13.
 */
public enum ShopsEnum {

    SHOP_INTERSPAR("Interspar", R.drawable.interspar, Calendar.WEDNESDAY),
    SHOP_TESCO("Tesco", R.drawable.tesco, Calendar.WEDNESDAY),
    SHOP_BILLA("Billa", R.drawable.billa, Calendar.WEDNESDAY),
    SHOP_ALBERT("Albert", R.drawable.albert, Calendar.WEDNESDAY),
    SHOP_LIDL("Lidl", R.drawable.lidl, Calendar.MONDAY),
    SHOP_KAUFLAND("Kaufland", R.drawable.kaufland, Calendar.THURSDAY),
    SHOP_OTHER("Other", R.drawable.other, Calendar.MONDAY);

    private String name;
    private int iconId;
    private int beggingDay;

    ShopsEnum(String name, int iconId, int beggingDay) {
        this.name = name;
        this.iconId = iconId;
        this.beggingDay = beggingDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public int getBeggingDay() {
        return beggingDay;
    }

    @Override
    public String toString() {
        return name;
    }
}
