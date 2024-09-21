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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class ReserveActivity extends AppCompatActivity {

    private static final String RESERVE = "reserve";
    private static final String RESERVE_ID = "reserve_id";

    DBHelper dbHelper;
    String SearchText;
    EditText SearchString;
    Button btnClear;
    ListView listView;
    AutoCompleteTextView DateList;
    String [] SupItems, ProdItems;
    int[] SupID, ProdID, DupID;
    String[] SupName, ProdName, DupDate;
    int[] id, sid, pid;
    Long[] date;
    boolean visibleFlag = false;
    int sidBuf, pidBuf;

    MaterialButton AddButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        btnClear = (Button)findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(onClickListener());
        SearchString = findViewById(R.id.SearchText);
        dbHelper = new DBHelper(this);
        findId();
        displayData(dbHelper.GetAllReserve());
        AddButton = findViewById(R.id.AddButton);

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(ReserveActivity.this).inflate(R.layout.reserve_dialog_layout, null);
                AutoCompleteTextView GuestList = view1.findViewById(R.id.guestItem);
                DateList = view1.findViewById(R.id.dateItem);
                AutoCompleteTextView EventList = view1.findViewById(R.id.eventItem);
                SupProdLoad();
                ArrayAdapter<String> Supitemadapter = new ArrayAdapter<>(ReserveActivity.this, R.layout.items_list, SupItems);
                GuestList.setAdapter(Supitemadapter);
                ArrayAdapter<String> Proditemadapter = new ArrayAdapter<>(ReserveActivity.this, R.layout.items_list, ProdItems);
                EventList.setAdapter(Proditemadapter);
                GuestList.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        LinkedList<GuestData> DupList = dbHelper.GetDupGuest(GuestList.getText().toString());
                        int DupCount = DupList.size();
                        TextInputLayout TextDateLayout = view1.findViewById(R.id.TextDateLayout);
                        if (DupCount > 1){
                        TextDateLayout.setVisibility(View.VISIBLE);
                        visibleFlag = true;
                        DupID = new int[DupCount];
                        DupDate = new String[DupCount];
                        int i = 0;
                        for (GuestData data : DupList) {
                            DupID[i] = data.guest_id;
                            String mydate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(data.guest_date));
                            DupDate[i] = mydate;
                            i++;
                        }
                        ArrayAdapter<String> GuestDateitemadapter = new ArrayAdapter<>(ReserveActivity.this, R.layout.items_list, DupDate);
                        DateList.setAdapter(GuestDateitemadapter);
                        }
                        else
                        {
                            TextDateLayout.setVisibility(View.GONE);
                            visibleFlag = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(ReserveActivity.this)
                        .setTitle("Добавление бронирования")
                        .setView(view1)
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                sidBuf = dbHelper.GetGuestId(GuestList.getText().toString());
                                String nStr =  EventList.getText().toString().substring(0, EventList.getText().toString().lastIndexOf("(")-1);
                                pidBuf = dbHelper.GetEventId(nStr);
                                if (GuestList.getText().toString().isEmpty() || EventList.getText().toString().isEmpty())
                                    Toast.makeText(ReserveActivity.this, "Одно из полей не было заполнено!", Toast.LENGTH_SHORT).show();
                                else if (!visibleFlag || !DateList.getText().toString().isEmpty()){
                                    int itemindex;
                                    if (visibleFlag)
                                        itemindex = DupID[Arrays.asList(DupDate).indexOf(DateList.getText().toString())];
                                    else
                                        itemindex = sidBuf;
                                    ReserveData data = new ReserveData(null, itemindex, GuestList.getText().toString(), pidBuf, nStr);
                                    if (dbHelper.checkPlace(data.event_name, 0))
                                    {
                                        boolean result = dbHelper.InsertReserveRow(data);
                                        if (result){
                                            displayData(dbHelper.GetAllReserve());
                                            Toast.makeText(ReserveActivity.this, "Данные успешно добавлены!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                        else {
                                            Toast.makeText(ReserveActivity.this, "Что-то пошло не так, данные не были добавлены!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                {
                                    Toast.makeText(ReserveActivity.this, "Все места на мероприятие заняты!", Toast.LENGTH_SHORT).show();
                                }
                                }
                                else{
                                    Toast.makeText(ReserveActivity.this, "Вы не выбрали дату рождения!", Toast.LENGTH_SHORT).show();
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
                    LinkedList<ReserveData> list = dbHelper.SearchReserve(SearchText);
                    if (list.isEmpty()) {
                        Toast.makeText(ReserveActivity.this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        displayData(list);
                }
                else
                    displayData(dbHelper.GetAllReserve());
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
    public void SupProdLoad(){
        LinkedList<GuestData> SupList = dbHelper.GetAllGuestsNotDup();
        int SupCount = SupList.size();
        SupID = new int[SupCount];
        SupItems = new String[SupCount];
        int i = 0;
        for (GuestData data:SupList) {
            SupID[i] = data.guest_id;
            SupItems[i] = data.guest_name;
            i++;
        }
        LinkedList<EventData> ProdList = dbHelper.GetAllEvent();
        int ProdCount = ProdList.size();
        ProdID = new int[ProdCount];
        ProdItems = new String[ProdCount];
        i = 0;
        for (EventData data:ProdList) {
            ProdID[i] = data.event_id;
            ProdItems[i] = data.event_name + " (" + Integer.toString(dbHelper.getPlace(data.event_name)) + " мест)";
            i++;
        }
    }
    private void findId(){
        listView = findViewById(R.id.lv);
    }

    private void displayData(LinkedList<ReserveData> list){
        listView = findViewById(R.id.lv);
        int count = list.size();
        id = new int[count];
        sid = new int[count];
        SupName = new String[count];
        date = new Long[count];
        pid = new int[count];
        ProdName = new String[count];
        int i = 0;
        for (ReserveData data:list) {
            id[i] = data.reserve_id;
            sid[i] = data.guest_id;
            SupName[i] = data.guest_name;
            date[i] = dbHelper.GetGuestsDate(sid[i]);
            pid[i] = data.event_id;
            ProdName[i] = data.event_name;
            i++;
        }
        ReserveActivity.CustAdapter custAdapter = new ReserveActivity.CustAdapter();
        listView.setAdapter(custAdapter);
    }

    public void MainButtonClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class CustAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return id.length;
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
            TextView txtEvent, txtGuest, txtDate, tv1, tv2, tv3;
            ImageButton txtEdit, txtDelete;
            CardView cardView;
            convertView = LayoutInflater.from(ReserveActivity.this).inflate(R.layout.reserve_data_item, parent, false);
            txtGuest = convertView.findViewById(R.id.txt_guest);
            txtDate = convertView.findViewById(R.id.txt_date);
            txtEvent = convertView.findViewById(R.id.txt_event);
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
                        txtGuest.setVisibility(View.GONE);
                        txtDate.setVisibility(View.GONE);
                        txtEvent.setVisibility(View.GONE);
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        tv3.setVisibility(View.GONE);}
                    else {
                        txtDelete.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        txtGuest.setVisibility(View.VISIBLE);
                        txtDate.setVisibility(View.VISIBLE);
                        txtEvent.setVisibility(View.VISIBLE);
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);}
                }
            });
            txtGuest.setText(SupName[position]);
            txtDate.setText(new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(date[position])));
            txtEvent.setText(ProdName[position]);
            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = LayoutInflater.from(ReserveActivity.this).inflate(R.layout.reserve_dialog_layout, null);
                    AutoCompleteTextView GuestList = view1.findViewById(R.id.guestItem);
                    DateList = view1.findViewById(R.id.dateItem);
                    DateList.setText(new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(date[position])));
                    GuestList.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            LinkedList<GuestData> DupList = dbHelper.GetDupGuest(GuestList.getText().toString());
                            int DupCount = DupList.size();
                            TextInputLayout TextDateLayout = view1.findViewById(R.id.TextDateLayout);
                            if (DupCount > 1){
                                TextDateLayout.setVisibility(View.VISIBLE);
                                visibleFlag = true;
                                DupID = new int[DupCount];
                                DupDate = new String[DupCount];
                                int i = 0;
                                for (GuestData data : DupList) {
                                    DupID[i] = data.guest_id;
                                    String mydate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(data.guest_date));
                                    DupDate[i] = mydate;
                                    i++;
                                }
                                ArrayAdapter<String> GuestDateitemadapter = new ArrayAdapter<>(ReserveActivity.this, R.layout.items_list, DupDate);
                                DateList.setAdapter(GuestDateitemadapter);
                            }
                            else
                            {
                                TextDateLayout.setVisibility(View.GONE);
                                visibleFlag = false;
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    GuestList.setText(SupName[position]);
                    AutoCompleteTextView EventList = view1.findViewById(R.id.eventItem);
                    EventList.setText(ProdName[position]);
                    SupProdLoad();
                    ArrayAdapter<String> Supitemadapter = new ArrayAdapter<>(ReserveActivity.this, R.layout.items_list, SupItems);
                    GuestList.setAdapter(Supitemadapter);
                    ArrayAdapter<String> Proditemadapter = new ArrayAdapter<>(ReserveActivity.this, R.layout.items_list, ProdItems);
                    EventList.setAdapter(Proditemadapter);

                    AlertDialog alertDialog = new MaterialAlertDialogBuilder(ReserveActivity.this)
                            .setTitle("Изменение бронирования")
                            .setView(view1)
                            .setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    sidBuf = dbHelper.GetGuestId(GuestList.getText().toString());
                                    String nStr = EventList.getText().toString();
                                    if (nStr.lastIndexOf("(")-1 != -2)
                                        nStr =  EventList.getText().toString().substring(0, EventList.getText().toString().lastIndexOf("(")-1);
                                    pidBuf = dbHelper.GetEventId(nStr);
                                    if (GuestList.getText().toString().isEmpty() || EventList.getText().toString().isEmpty())
                                        Toast.makeText(ReserveActivity.this, "Одно из полей не было заполнено!", Toast.LENGTH_SHORT).show();
                                    else if (!visibleFlag || !DateList.getText().toString().isEmpty()){
                                        int itemindex;
                                        if (visibleFlag)
                                            itemindex = DupID[Arrays.asList(DupDate).indexOf(DateList.getText().toString())];
                                        else
                                            itemindex = sidBuf;
                                    {
                                        ReserveData data = new ReserveData(id[position], itemindex, GuestList.getText().toString(), pidBuf, nStr);
                                        if (dbHelper.checkPlace(data.event_name, id[position])) {
                                            boolean result = dbHelper.ReserveUpdateRow(data);
                                            if (result) {
                                                displayData(dbHelper.GetAllReserve());
                                                Toast.makeText(ReserveActivity.this, "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                            }
                                            else
                                            {
                                                Toast.makeText(ReserveActivity.this, "Что-то пошло не так, данные не были изменены!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(ReserveActivity.this, "Все места на мероприятие заняты!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    }
                                    else{
                                        Toast.makeText(ReserveActivity.this, "Вы не выбрали дату рождения!", Toast.LENGTH_SHORT).show();
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
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReserveActivity.this);
                    builder.setMessage("Вы действительно хотите удалить запись?")
                            .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dbHelper.DeleteRow(RESERVE, RESERVE_ID , id[position]);
                                    displayData(dbHelper.GetAllReserve());
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