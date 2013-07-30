package main.basket.enums;

import java.util.Calendar;
import main.basket.R;

/** Created by martin on 28.7.13. */
public enum ShopsEnum {

  SHOP_INTERSPAR("Interspar", R.drawable.ic_launcher, Calendar.WEDNESDAY),
  SHOP_TESCO("Tesco", R.drawable.ic_launcher, Calendar.WEDNESDAY),
  SHOP_BILLA("Billa", R.drawable.ic_launcher, Calendar.WEDNESDAY),
  SHOP_LIDL("Lidl", R.drawable.ic_launcher, Calendar.MONDAY),
  SHOP_KAUFLAND("Kaufland", R.drawable.ic_launcher, Calendar.THURSDAY);

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
