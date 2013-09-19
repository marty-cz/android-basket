package main.basket.list.structure;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by martin on 27.7.13.
 */
public abstract class CommonListItem implements Serializable {

    protected String headLine;
    protected Date dateFrom = null;    // if null then not used
    protected Date dateTo = null;    // if null then not used
    protected String footLine = "";      // if empty then not used

    public CommonListItem(String headLine, int subItemCount, Date dateFrom, Date dateTo) {
        this.headLine = headLine;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public CommonListItem(String headLine, Date dateFrom) {
        this.headLine = headLine;
        this.dateFrom = dateFrom;
    }

    public CommonListItem(String headLine) {
        this.headLine = headLine;
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

    public abstract int getSubItemCount();

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
            if (dateFrom != null) {
                res += ", dateFrom=" + dateFrom;
            }
            if (dateTo != null) {
                res += ", dateTo=" + dateTo;
            }
        }
        return res + ']';
    }
}
