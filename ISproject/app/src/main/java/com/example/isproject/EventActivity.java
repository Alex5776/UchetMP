package com.example.isproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class EventActivity extends AppCompatActivity {

    private static final String EVENTS = "events";

    private static final String EVENT_ID = "event_id";

    DBHelper dbHelper;
    String SearchText;
    EditText SearchString;
    Button btnClear;
    ListView listView;

    String[] name;
    int[] id, place;
    Long[] date;

    MaterialButton AddButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        btnClear = (Button)findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(onClickListener());
        SearchString = findViewById(R.id.SearchText);
        dbHelper = new DBHelper(this);

        findId();
        displayData(dbHelper.GetAllEvent());
        AddButton = findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(EventActivity.this).inflate(R.layout.event_dialog_layout, null);
                TextInputEditText EventName = view1.findViewById(R.id.EventName);
                TextInputEditText EventPlace = view1.findViewById(R.id.EventPlace);
                DatePicker EventDate = view1.findViewById(R.id.EventDate);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(EventActivity.this)
                        .setTitle("Добавление мероприятия")
                        .setView(view1)
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                if (EventName.getText().toString().isEmpty() || EventPlace.getText().toString().isEmpty())
                                    Toast.makeText(EventActivity.this, "Поле не было заполнено!", Toast.LENGTH_SHORT).show();
                                else
                                {
                                    String mydate = Long.toString(EventDate.getDayOfMonth()) + "/" + Long.toString(EventDate.getMonth()+1) + "/" + Long.toString(EventDate.getYear());
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date datep = null;
                                    try {
                                        datep = sdf.parse(mydate);
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    EventData data = new EventData(null, Objects.requireNonNull(EventName.getText()).toString(), Integer.parseInt(EventPlace.getText().toString()), datep.getTime());
                                    boolean result = dbHelper.InsertEventRow(data);
                                    if (result){
                                        displayData(dbHelper.GetAllEvent());
                                        Toast.makeText(EventActivity.this, "Данные успешно добавлены!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                    else
                                        Toast.makeText(EventActivity.this, "Что-то пошло не так, данные не были добавлены!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }

        });


        SearchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchText = SearchString.getText().toString();
                if (!SearchText.isEmpty()) {
                    LinkedList<EventData> list = dbHelper.SearchEvent(SearchText);
                    if (list.isEmpty()) {
                        Toast.makeText(EventActivity.this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        displayData(list);
                }
                else
                    displayData(dbHelper.GetAllEvent());
                if (!SearchString.getText().toString().isEmpty()) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchString.setText("");
            }
        };
    }

    private void findId(){
        listView = findViewById(R.id.lv);
    }

    private void displayData(LinkedList<EventData> list){
        listView = findViewById(R.id.lv);
        int count = list.size();
        id = new int[count];
        name = new String[count];
        place = new int[count];
        date = new Long[count];

        int i = 0;
        for (EventData data:list) {
            id[i] = data.event_id;
            name[i] = data.event_name;
            place[i] = data.event_place;
            date[i] = data.event_date;
            i++;
        }
        EventActivity.CustAdapter custAdapter = new EventActivity.CustAdapter();
        listView.setAdapter(custAdapter);
    }

    public void MainButtonClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class CustAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView txtName, txtPlace, txtDate, tv1, tv2, tv3;
            ImageButton txtEdit, txtDelete;
            CardView cardView;
            convertView = LayoutInflater.from(EventActivity.this).inflate(R.layout.event_data_item, parent, false);
            txtName = convertView.findViewById(R.id.txt_name);
            txtPlace = convertView.findViewById(R.id.txt_place);
            txtDate = convertView.findViewById(R.id.txt_date);
            tv1 = convertView.findViewById(R.id.tv1);
            tv2 = convertView.findViewById(R.id.tv2);
            tv3 = convertView.findViewById(R.id.tv3);
            txtEdit = convertView.findViewById(R.id.txt_edit);
            txtDelete = convertView.findViewById(R.id.txt_delete_bin);
            cardView = convertView.findViewById(R.id.cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Random random = new Random();
                    //cardView.setCardBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                    GlobalVariable appState = ((GlobalVariable)getApplicationContext());
                    if (txtDelete.getVisibility() == View.GONE && appState.getAdminFlag())
                    {
                        txtDelete.setVisibility(View.VISIBLE);
                        txtEdit.setVisibility(View.VISIBLE);
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        tv3.setVisibility(View.GONE);
                        txtName.setVisibility(View.GONE);
                        txtPlace.setVisibility(View.GONE);
                        txtDate.setVisibility(View.GONE);}
                    else {
                        txtDelete.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        txtName.setVisibility(View.VISIBLE);
                        txtPlace.setVisibility(View.VISIBLE);
                        txtDate.setVisibility(View.VISIBLE);}
                }
            });
            txtName.setText(name[position]);
            txtPlace.setText(Integer.toString(place[position]) + " (осталось: " + Integer.toString(dbHelper.getPlace(name[position])) + ")");
            txtDate.setText(new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(date[position])));
            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = LayoutInflater.from(EventActivity.this).inflate(R.layout.event_dialog_layout, null);
                    TextInputEditText EventName = view1.findViewById(R.id.EventName);
                    TextInputEditText EventPlace = view1.findViewById(R.id.EventPlace);
                    DatePicker EventDate = view1.findViewById(R.id.EventDate);
                    EventName.setText(name[position]);
                    EventPlace.setText(Integer.toString(place[position]));
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(date[position]);
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    EventDate.updateDate(mYear, mMonth, mDay);
                    AlertDialog alertDialog = new MaterialAlertDialogBuilder(EventActivity.this)
                            .setTitle("Изменение мероприятия")
                            .setView(view1)
                            .setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    if (EventName.getText().toString().isEmpty() || EventPlace.getText().toString().isEmpty())
                                        Toast.makeText(EventActivity.this, "Поле не было заполнено!", Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        String mydate = Long.toString(EventDate.getDayOfMonth()) + "/" + Long.toString(EventDate.getMonth()+1) + "/" + Long.toString(EventDate.getYear());
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        Date datep = null;
                                        try {
                                            datep = sdf.parse(mydate);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                        EventData data = new EventData(id[position], Objects.requireNonNull(EventName.getText()).toString(), Integer.parseInt(EventPlace.getText().toString()), datep.getTime());
                                        boolean result = dbHelper.EventUpdateRow(data);
                                        if (result){
                                            displayData(dbHelper.GetAllEvent());
                                            Toast.makeText(EventActivity.this, "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                        else
                                            Toast.makeText(EventActivity.this, "Что-то пошло не так, данные не были изменены!", Toast.LENGTH_SHORT).show();}
                                }
                            }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }
            });
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                    builder.setMessage("Вы действительно хотите удалить запись?")
                            .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dbHelper.DeleteRow(EVENTS, EVENT_ID , id[position]);
                                    displayData(dbHelper.GetAllEvent());
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    builder.show();
                }
            });
            return convertView;
        }
    }
}