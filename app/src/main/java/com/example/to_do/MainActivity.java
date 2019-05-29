package com.example.to_do;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout aboutus;
    LinearLayout add_task;
    LinearLayout tasks;
    EditText add_taskhead, add_description, add_date;
    Button add_taskbtn;
    DatabaseHelper databaseHelper;
    ListView task_listview;

    TextView tasks_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aboutus = findViewById(R.id.aboutus);
        aboutus.setVisibility(View.INVISIBLE);

        add_task = findViewById(R.id.add_task);
        add_task.setVisibility(View.INVISIBLE);

        tasks = findViewById(R.id.tasks);
        tasks_no = findViewById(R.id.tasks_no);

        databaseHelper = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewTasks();

        add_taskhead = findViewById(R.id.add_taskhead);
        add_description = findViewById(R.id.add_description);
        add_date = findViewById(R.id.add_date);
        add_taskbtn = findViewById(R.id.add_taskbtn);
        add_taskbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_taskhead.getText().toString().matches("") || add_description.getText().toString().matches("") || add_date.getText().toString().matches("")){
                    Toast.makeText(MainActivity.this,"Some fields are blank!",Toast.LENGTH_LONG).show();
                }else {
                    boolean isInserted = databaseHelper.insertData(add_taskhead.getText().toString(),add_description.getText().toString(),add_date.getText().toString());
                    if(isInserted) {
                        Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                        add_task.setVisibility(View.INVISIBLE);
                        viewTasks();
                        tasks.setVisibility(View.VISIBLE);
                    }
                    else
                        Toast.makeText(MainActivity.this,"Insertion Failed",Toast.LENGTH_LONG).show();
                }
                }
        });

    }

    public void viewTasks(){
        tasks_no = findViewById(R.id.tasks_no);
        Cursor res = databaseHelper.getListData();
        if(res.getCount() == 0){
            tasks_no.setVisibility(View.VISIBLE);

        }else {
            ArrayList<String> task_list = new ArrayList<String>();

            while (res.moveToNext()){
                task_list.add(res.getString(1));
            }

            task_listview = findViewById(R.id.task_listview);
            ListAdapter listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, task_list);
            task_listview.setAdapter(listAdapter);
            tasks_no.setText("To Do's");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        aboutus = findViewById(R.id.aboutus);
        add_task = findViewById(R.id.add_task);
        tasks = findViewById(R.id.tasks);
        switch (item.getItemId()){
            case R.id.menu_task:
                add_task.setVisibility(View.INVISIBLE);
                aboutus.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.VISIBLE);
                return true;
            case R.id.menu_add:
                add_task.setVisibility(View.VISIBLE);
                aboutus.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.INVISIBLE);
                return true;
            case R.id.menu_completed:
                aboutus.setVisibility(View.INVISIBLE);
                add_task.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.INVISIBLE);
                return true;
            case R.id.menu_about:
                aboutus.setVisibility(View.VISIBLE);
                add_task.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.INVISIBLE);
                //Toast.makeText(MainActivity.this,"About us", Toast.LENGTH_LONG).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
