package main.basket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      final Button btn = (Button) findViewById(R.id.button);
      btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(MainActivity.this, WeekActivity.class);
          startActivity(intent);
        }
      });



      final ListView listview = (ListView) findViewById(R.id.listView);
      String[] values = new String[] {"Android", "iPhone", "WindowsMobile",
          "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
          "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
          "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
          "Android", "iPhone", "WindowsMobile"};

      final ArrayList<String> list = new ArrayList<String>();
      for (int i = 0; i < values.length; ++i) {
        list.add(values[i]);
      }
    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }


  public void addButtonClick(View view) {

  }

}
