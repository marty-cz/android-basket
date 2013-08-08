package main.basket.list.structure;

import java.io.Serializable;
import java.util.ArrayList;

/** Created by martin on 6.8.13. */
public class StructureWrapper implements Serializable {

  protected ArrayList<WeekListItem> weeks;

  public StructureWrapper() {
    this(null);
  }

  public StructureWrapper(ArrayList<WeekListItem> weeks) {
    this.weeks =  (weeks == null) ? new ArrayList<WeekListItem>() : weeks;
  }

  public ArrayList<WeekListItem> getWeeks() {
    return weeks;
  }

  public void setWeeks(ArrayList<WeekListItem> weeks) {
    this.weeks = weeks;
  }

  public boolean addWeek(WeekListItem item) {
    return weeks.add(item);
  }

  public boolean removeWeek(WeekListItem item) {
    return weeks.remove(item);
  }

  public WeekListItem removeWeek(int index) {
    return weeks.remove(index);
  }

}
