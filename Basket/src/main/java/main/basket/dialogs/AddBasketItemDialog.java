package main.basket.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import main.basket.R;
import main.basket.helper.DecimalDigitsInputFilter;
import main.basket.list.structure.BasketListItem;

/** Created by martin on 28.7.13. */
public class AddBasketItemDialog extends Dialog implements android.view.View.OnClickListener {

  protected Activity c;
  protected Dialog d;
  protected Button yes, no;
  protected BasketListItem item;
  protected OnClickListener okButtonListener = null;

  public AddBasketItemDialog(Activity a, BasketListItem item, int theme) {
    super(a, theme);
    this.c = a;
    this.item = item;
  }

  public BasketListItem getBasketItem() {
    return item;
  }

  public void setBasketItem(BasketListItem item) {
    this.item = item;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle((item == null) ? R.string.add_product : R.string.edit_product);
    setContentView(R.layout.add_basket_item_dialog_layout);

    yes = (Button) findViewById(R.id.btn_yes);
    no = (Button) findViewById(R.id.btn_no);
    TextView nameView = (TextView) findViewById(R.id.item_name_in);
    TextView priceView = (TextView) findViewById(R.id.item_price_in);

    yes.setOnClickListener(this);
    no.setOnClickListener(this);
    if (item == null) {
      item = new BasketListItem("", 0.0);
    }
    nameView.setText(item.getHeadLine());
    priceView.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
    priceView.setText(String.valueOf(item.getPrice()));

    WindowManager.LayoutParams params = getWindow().getAttributes();
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    getWindow().setAttributes(params);
  }

  public void setPositiveButtonAction(final OnClickListener listener) {
    okButtonListener = listener;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_yes:
        TextView nameView = (TextView) findViewById(R.id.item_name_in);
        TextView priceView = (TextView) findViewById(R.id.item_price_in);

        String err = "";
        try {
          Double price = Double.valueOf(priceView.getText().toString());
          if (nameView.getText().toString().trim().length() > 0) {
            item.setHeadLine(nameView.getText().toString().trim());
            item.setPrice(price);
          } else {
            err = getContext().getString(R.string.err_empty_product);
          }
        } catch (Exception e) {
          err = getContext().getString(R.string.err_price_num);
        }
        if (err.length() > 0) {
          new AlertDialog.Builder(c)
              .setTitle(R.string.error)
              .setMessage(err)
              .setIcon(android.R.drawable.stat_notify_error)
              .setPositiveButton(android.R.string.ok,
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                      dialog.dismiss();
                    }
                  })
              .show();
        } else {
          if (okButtonListener != null) {
            okButtonListener.onClick(this, v.getId());
          }
          dismiss();
        }
        break;
      default:
        dismiss();
    }
  }

}