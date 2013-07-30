package main.basket.list.structure;

import android.app.Activity;

import java.io.Serializable;
import java.util.Date;

/** Created by martin on 27.7.13. */
public class CommonListItem implements Serializable {

  protected String headLine;
  protected int subItemCount = -1; // if -1 then not used
  protected Date dateFrom = null;  // if null then not used
  protected Date dateTo = null;    // if null then not used
  protected String footLine = "";  // if empty then not used

  public CommonListItem(String headLine, int subItemCount, Date dateFrom, Date dateTo) {
    this.headLine = headLine;
    this.subItemCount = subItemCount;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }

  public CommonListItem(String headLine, int subItemCount, Date dateFrom) {
    this.headLine = headLine;
    this.subItemCount = subItemCount;
    this.dateFrom = dateFrom;
  }

  public CommonListItem(String headLine, int subItemCount) {
    this.headLine = headLine;
    this.subItemCount = subItemCount;
  }

  public CommonListItem(String headLine, String footLine) {
    this.headLine = headLine;
    this.footLine = footLine;
  }

  public String getHeadLine() {
    return headLine;
  }

  public void setHeadLine(String headLine) {
    this.headLine = headLine;
  }

  public String getFootLine() {
    return footLine;
  }

  public void setFootLine(String footLine) {
    this.footLine = footLine;
  }

  public int getSubItemCount() {
    return subItemCount;
  }

  public void setSubItemCount(int subItemCount) {
    this.subItemCount = subItemCount;
  }

  public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

  @Override
  public String toString() {
    String res = "[" + "headLine='" + headLine + '\'';
    if (footLine != "") {
      res += ", footLine='" + footLine + '\'';
    } else {
      if (subItemCount >= 0) {
        res += ", subItemCount=" + subItemCount;
      }
      if (dateFrom != null) {
        res += ", dateFrom=" + dateFrom;
      }
      if (dateTo != null) {
        res += ", dateTo=" + dateTo;
      }
    }
    return res +']';
  }
}
