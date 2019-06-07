package com.example.to_do;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    LinearLayout aboutus, add_task, tasks, edit_task;
    EditText add_taskhead, add_description, add_date;
    Button add_taskbtn;
    DatabaseHelper databaseHelper;
    ListView task_listview;

    TextView tasks_no;

    ArrayList<String> task_listhead;
    ArrayList<String> task_listDesc;
    ArrayList<String> task_listExp;
    ArrayList<String> task_listId;
    int index;

    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int year;
    int month;
    int dateOfMonth;

    RelativeLayout detail_task;
    TextView detail_taskhead, detail_taskdesc, detail_taskexp;

    Button detail_deletebtn, detail_completebtn, detail_editbtn;

    EditText edit_taskhead, edit_description, edit_date;
    Button edit_taskbtn;


    RelativeLayout complete_task;
    TextView complete_no;
    ListView complete_listview;
    ArrayList<String> complete_listhead;
    ArrayList<String> complete_listDesc;
    ArrayList<String> complete_listExp;
    ArrayList<String> complete_listId;
    ArrayList<String> complete_listEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        task_listhead = new ArrayList<String>();
        task_listDesc = new ArrayList<String>();
        task_listExp = new ArrayList<String>();
        task_listId = new ArrayList<String>();

        task_listview = findViewById(R.id.task_listview);

        aboutus = findViewById(R.id.aboutus);
        aboutus.setVisibility(View.INVISIBLE);

        add_task = findViewById(R.id.add_task);
        add_task.setVisibility(View.INVISIBLE);

        tasks = findViewById(R.id.tasks);
        tasks_no = findViewById(R.id.tasks_no);

        detail_task = findViewById(R.id.detail_task);
        detail_task.setVisibility(View.INVISIBLE);

        databaseHelper = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewTasks();

        add_taskhead = findViewById(R.id.add_taskhead);
        add_description = findViewById(R.id.add_description);
        add_date = findViewById(R.id.add_date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            add_date.setShowSoftInputOnFocus(false);
        } else { // API 11-20
            add_date.setTextIsSelectable(true);
        }


        add_taskbtn = findViewById(R.id.add_taskbtn);
        add_taskbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                    add_function();
                }
        });

        add_date.setInputType(InputType.TYPE_NULL);
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_function();
            }
        });

        detail_taskhead = findViewById(R.id.detail_taskhead);
        detail_taskdesc = findViewById(R.id.detail_taskdesc);
        detail_taskexp = findViewById(R.id.detail_taskexp);
        task_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {detail_function(position);}
        });


        detail_deletebtn = findViewById(R.id.detail_deletebtn);
        detail_deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_function();
            }
        });

        detail_completebtn = findViewById(R.id.detail_completebtn);
        detail_completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completed_function();
            }
        });


        edit_task = findViewById(R.id.edit_task);
        edit_task.setVisibility(View.INVISIBLE);
        detail_editbtn = findViewById(R.id.detail_editbtn);
        detail_editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_function();
            }
        });
        edit_taskhead = findViewById(R.id.edit_taskhead);
        edit_description = findViewById(R.id.edit_description);

        edit_date = findViewById(R.id.edit_date);
        edit_date.setInputType(InputType.TYPE_NULL);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_function();
            }
        });


        complete_task = findViewById(R.id.complete_task);
        complete_task.setVisibility(View.INVISIBLE);
        complete_listview = findViewById(R.id.complete_listview);
        complete_listhead = new ArrayList<String>();
        complete_listDesc = new ArrayList<String>();
        complete_listExp = new ArrayList<String>();
        complete_listId = new ArrayList<String>();
        complete_listEnd = new ArrayList<String>();

    }

    /*====================  VIEW TASK  ====================*/

    public void viewTasks(){
        tasks_no = findViewById(R.id.tasks_no);
        task_listhead.clear();
        task_listDesc.clear();
        task_listExp.clear();
        task_listId.clear();
        Cursor res = databaseHelper.getListData();
        if(res.getCount() == 0){
            tasks_no.setVisibility(View.VISIBLE);

        }else {
            while (res.moveToNext()){
                task_listId.add(res.getString(0));
                task_listhead.add(res.getString(1));
                task_listDesc.add(res.getString(2));
                task_listExp.add(res.getString(3));
            }

            Collections.reverse(task_listId);
            Collections.reverse(task_listhead);
            Collections.reverse(task_listDesc);
            Collections.reverse(task_listExp);

            CustomAdapter customAdapter = new CustomAdapter();
            task_listview.setAdapter(customAdapter);
            tasks_no.setVisibility(View.GONE);
        }


    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return task_listhead.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_layout,null);

            TextView texthead = convertView.findViewById(R.id.textHead);
            texthead.setText(task_listhead.get(position));

            TextView textDesc = convertView.findViewById(R.id.textDesc);
            textDesc.setText(task_listDesc.get(position));

            TextView textExp = convertView.findViewById(R.id.textExp);
            textExp.setText(task_listExp.get(position));

            TextView textEnd = convertView.findViewById(R.id.textEnd);
            textEnd.setVisibility(View.GONE);

            TextView end = convertView.findViewById(R.id.end);
            end.setVisibility(View.GONE);

            return convertView;
        }
    }

    /*====================  END VIEW TASK  ====================*/



    /*====================  ADD TASK  ====================*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void add_function(){
        if (add_taskhead.getText().toString().matches("") || add_description.getText().toString().matches("") || add_date.getText().toString().matches("")){
            Toast.makeText(MainActivity.this,"Some fields are blank!",Toast.LENGTH_LONG).show();
        }else {
            boolean isInserted = databaseHelper.insertData(add_taskhead.getText().toString(),add_description.getText().toString(),add_date.getText().toString());
            if(isInserted) {
//                Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                add_task.setVisibility(View.INVISIBLE);
                viewTasks();
                tasks.setVisibility(View.VISIBLE);
                startNotification(add_date.getText().toString());
                add_taskhead.setText("");
                add_date.setText("");
                add_description.setText("");
            }
            else
                Toast.makeText(MainActivity.this,"Insertion Failed",Toast.LENGTH_LONG).show();
        }
    }
    public void date_function(){
        calendar = Calendar.getInstance();
        dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dayOfMonthS = Integer.toString(dayOfMonth);
                String monthS = Integer.toString(month+1);
                String yearS = Integer.toString(year);
                if(dayOfMonth<10){
                    dayOfMonthS = "0" + dayOfMonthS;
                }
                if (month<10){
                    monthS = "0" + monthS;
                }

                if(edit_task.getVisibility() == View.VISIBLE){
                    edit_date.setText(dayOfMonthS + "/" + monthS + "/" + yearS);
                }else {
                    add_date.setText(dayOfMonthS + "/" + monthS + "/" + yearS);
                }

            }
        },year,month,dateOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    /*====================  END ADD TASK  ====================*/



    /*====================  DETAIL TASK  ====================*/

    public void detail_function(int i){
        detail_task.setVisibility(View.VISIBLE);
        tasks.setVisibility(View.INVISIBLE);
        detail_taskhead.setText(task_listhead.get(i));
        detail_taskdesc.setText(task_listDesc.get(i));
        detail_taskexp.setText(task_listExp.get(i));
        index = i;
    }

    /*====================  END DETAIL TASK  ====================*/



    /*====================  DELETE TASK  =====================*/

    public void delete_function(){


            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete the task?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Integer res = databaseHelper.deleteData(task_listId.get(index));
                            if (res > 0) {
                                viewTasks();
                                detail_task.setVisibility(View.INVISIBLE);
                                add_task.setVisibility(View.INVISIBLE);
                                aboutus.setVisibility(View.INVISIBLE);
                                tasks.setVisibility(View.VISIBLE);
                            }else {
                                Toast.makeText(MainActivity.this, "Deletion Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


    }


    /*====================  END DELETE TASK  =====================*/




    /*====================  COMPLETE TASK  =====================*/

    public void completed_function(){
        boolean res = databaseHelper.completeData(task_listId.get(index),task_listhead.get(index),task_listDesc.get(index),task_listExp.get(index));
        if (res == true){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Congratulations")
                    .setMessage("Press exit to cancel.")
                    .setPositiveButton("Completed", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            viewTasks();
                            detail_task.setVisibility(View.INVISIBLE);
                            add_task.setVisibility(View.INVISIBLE);
                            aboutus.setVisibility(View.INVISIBLE);
                            tasks.setVisibility(View.INVISIBLE);
                            viewCompleteTasks();
                            complete_task.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("exit", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {
            Toast.makeText(MainActivity.this,"DID NOT COMPLETE", Toast.LENGTH_LONG).show();
        }

    }
    public void viewCompleteTasks(){
        complete_no = findViewById(R.id.complete_no);
        complete_listhead.clear();
        complete_listDesc.clear();
        complete_listExp.clear();
        complete_listId.clear();
        complete_listEnd.clear();
        Cursor res = databaseHelper.getCompleteData();
        if(res.getCount() == 0){
            complete_no.setText("No Completed Tasks");
            complete_no.setVisibility(View.VISIBLE);

        }else {
            while (res.moveToNext()){
                complete_listId.add(res.getString(0));
                complete_listhead.add(res.getString(1));
                complete_listDesc.add(res.getString(2));
                complete_listExp.add(res.getString(3));
                complete_listEnd.add(res.getString(5));
            }

            Collections.reverse(complete_listId);
            Collections.reverse(complete_listhead);
            Collections.reverse(complete_listDesc);
            Collections.reverse(complete_listExp);
            Collections.reverse(complete_listEnd);

            CustomAdapterComplete customAdapterComplete = new CustomAdapterComplete();
            complete_listview.setAdapter(customAdapterComplete);
            complete_no.setText("Completed Tasks");
        }


    }


    class CustomAdapterComplete extends BaseAdapter{

        @Override
        public int getCount() {
            return complete_listhead.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_layout,null);

            TextView texthead = convertView.findViewById(R.id.textHead);
            texthead.setText(complete_listhead.get(position));

            TextView textDesc = convertView.findViewById(R.id.textDesc);
            textDesc.setText(complete_listDesc.get(position));

            TextView textExp = convertView.findViewById(R.id.textExp);
            textExp.setText(complete_listExp.get(position));

            TextView textEnd = convertView.findViewById(R.id.textEnd);
            textEnd.setVisibility(View.VISIBLE);
            textEnd.setText(complete_listEnd.get(position));

            return convertView;
        }
    }
    /*====================  END COMPLETE TASK  =====================*/




    /*====================  EDIT TASK  =====================*/

    public void edit_function(){
        detail_task.setVisibility(View.INVISIBLE);
        edit_task.setVisibility(View.VISIBLE);
        edit_taskhead.setText(task_listhead.get(index));
        edit_description.setText(task_listDesc.get(index));
        edit_date.setText(task_listExp.get(index));
        edit_taskbtn = findViewById(R.id.edit_taskbtn);
        edit_taskbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.editData(task_listId.get(index),edit_taskhead.getText().toString(),edit_description.getText().toString(),edit_date.getText().toString());
                viewTasks();
                edit_task.setVisibility(View.INVISIBLE);
                detail_function(index);
            }
        });
    }

    /*====================  END EDIT TASK  =====================*/




    /*====================  MENU OPTIONS  =====================*/

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
                detail_task.setVisibility(View.INVISIBLE);
                add_task.setVisibility(View.INVISIBLE);
                aboutus.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.VISIBLE);
                edit_task.setVisibility(View.INVISIBLE);
                complete_task.setVisibility(View.INVISIBLE);
                return true;
            case R.id.menu_add:
                detail_task.setVisibility(View.INVISIBLE);
                add_task.setVisibility(View.VISIBLE);
                aboutus.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.INVISIBLE);
                add_taskhead.setText("");
                add_description.setText("");
                add_date.setText("");
                edit_task.setVisibility(View.INVISIBLE);
                complete_task.setVisibility(View.INVISIBLE);
                return true;
            case R.id.menu_completed:
                detail_task.setVisibility(View.INVISIBLE);
                aboutus.setVisibility(View.INVISIBLE);
                add_task.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.INVISIBLE);
                edit_task.setVisibility(View.INVISIBLE);
                viewCompleteTasks();
                complete_task.setVisibility(View.VISIBLE);
                return true;
            case R.id.menu_about:
                detail_task.setVisibility(View.INVISIBLE);
                aboutus.setVisibility(View.VISIBLE);
                add_task.setVisibility(View.INVISIBLE);
                tasks.setVisibility(View.INVISIBLE);
                edit_task.setVisibility(View.INVISIBLE);
                complete_task.setVisibility(View.INVISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*====================  END MENU OPTION  ====================*/

    Integer exitNum = 0;
    @Override
    public void onBackPressed(){

        if(tasks.getVisibility() == View.INVISIBLE && edit_task.getVisibility() == View.INVISIBLE){
            detail_task.setVisibility(View.INVISIBLE);
            add_task.setVisibility(View.INVISIBLE);
            aboutus.setVisibility(View.INVISIBLE);
            complete_task.setVisibility(View.INVISIBLE);
            tasks.setVisibility(View.VISIBLE);
            exitNum = 0;
        }else if (edit_task.getVisibility() == View.VISIBLE){
            edit_task.setVisibility(View.INVISIBLE);
            detail_task.setVisibility(View.VISIBLE);
            exitNum = 0;
        }else {
            if(exitNum == 1){
                exitNum = 0;
                super.onBackPressed();
            }else{
                Toast.makeText(MainActivity.this,"Press on more time to exit",Toast.LENGTH_LONG).show();
                exitNum += 1;
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startNotification(String dateExp){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateStart = dateFormat.format(date);
        String dateStop  = dateExp + " 00:00:00";//"08/06/2019 00:00:00";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+diff, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
//
    public String getTit(){
        return "One Task Left";
    }

    public String getTxt(){
        return "Deadline is today, come and complete it.";
    }


}
