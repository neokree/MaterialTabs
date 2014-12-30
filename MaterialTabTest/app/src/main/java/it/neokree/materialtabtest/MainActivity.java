package it.neokree.materialtabtest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by neokree on 30/12/14.
 */
public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> list = new ArrayList<String>();

        list.add("Text Tab");
        list.add("Swipable Text Tabs (more than 3)");
        list.add("Icon Tab");
        list.add("Swipable Icon Tab (more than 5)");

        this.setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        this.getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch(position) {
            case 0:
                intent = new Intent(this,TextTabActivity.class);
                break;
            case 1:
                intent = new Intent(this,SwipableTextTabActivity.class);
                break;
            case 2:
                intent = new Intent(this,IconTabActivity.class);
                break;
            case 3:
                intent = new Intent(this,SwipableIconTabActivity.class);
                break;
            default:
                intent = null;
        }
        startActivity(intent);
    }
}
