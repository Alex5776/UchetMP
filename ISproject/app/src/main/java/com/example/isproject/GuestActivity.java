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

public class GuestActivity extends AppCompatActivity {

    private static final String GUESTS = "guests";
    private static final String GUEST_ID = "guest_id";

    DBHelper dbHelper;
    String SearchText;
    EditText SearchString;
    Button btnClear;
    ListView listView;
    String[] name, phone;
    Long[] date;
    int[] id;
    MaterialButton AddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        btnClear = (Button)findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(onClickListener());
        SearchString = findViewById(R.id.SearchText);
        dbHelper = new DBHelper(this);
        findId();
        displayData(dbHelper.GetAllGuests());
        AddButton = findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(GuestActivity.this).inflate(R.layout.guest_dialog_layout, null);
                TextInputEditText guestFIO = view1.findViewById(R.id.GuestFIO);
                EditText guestPhone = view1.findViewById(R.id.GuestPhone);
                DatePicker guestDate = view1.findViewById(R.id.GuestDate);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(GuestActivity.this)
                        .setTitle("Добавление постояльца")
                        .setView(view1)
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                if (guestFIO.getText().toString().isEmpty() || guestPhone.getText().toString().isEmpty())
                                    Toast.makeText(GuestActivity.this, "Поле не было заполнено!", Toast.LENGTH_SHORT).show();
                                else
                                {
                                    String mydate = Long.toString(guestDate.getDayOfMonth()) + "/" + Long.toString(guestDate.getMonth()+1) + "/" + Long.toString(guestDate.getYear());
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date datep = null;
                                    try {
                                        datep = sdf.parse(mydate);
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    GuestData data = new GuestData(null, Objects.requireNonNull(guestFIO.getText()).toString(), guestPhone.getText().toString(), datep.getTime());
                                    boolean result = dbHelper.InsertGuestRow(data);
                                    if (result){
                                        displayData(dbHelper.GetAllGuests());
                                        Toast.makeText(GuestActivity.this, "Данные успешно добавлены!", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                    else
                                        Toast.makeText(GuestActivity.this, "Что-то пошло не так, данные не были добавлены!", Toast.LENGTH_SHORT).show();}
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
                    LinkedList<GuestData> list = dbHelper.SearchGuest(SearchText);
                    if (list.isEmpty()) {
                        Toast.makeText(GuestActivity.this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        displayData(list);
                }
                else
                    displayData(dbHelper.GetAllGuests());
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

    private void displayData(LinkedList<GuestData> list){
        listView = findViewById(R.id.lv);
        int count = list.size();
        id = new int[count];
        name = new String[count];
        phone = new String[count];
        date = new Long[count];

        int i = 0;
        for (GuestData data:list) {
            id[i] = data.guest_id;
            name[i] = data.guest_name;
            phone[i] = data.guest_phone;
            date[i] = data.guest_date;
            i++;
        }
        GuestActivity.CustAdapter custAdapter = new GuestActivity.CustAdapter();
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
            TextView txtName, txtPhone, txtDate, tv1, tv2, tv3;
            ImageButton txtEdit, txtDelete;
            CardView cardView;
            convertView = LayoutInflater.from(GuestActivity.this).inflate(R.layout.guest_data_item, parent, false);
            txtName = convertView.findViewById(R.id.txt_name);
            txtEdit = convertView.findViewById(R.id.txt_edit);
            txtPhone = convertView.findViewById(R.id.txt_phone);
            txtDate = convertView.findViewById(R.id.txt_date);
            txtDelete = convertView.findViewById(R.id.txt_delete_bin);
            tv1 = convertView.findViewById(R.id.tv1);
            tv2 = convertView.findViewById(R.id.tv2);
            tv3 = convertView.findViewById(R.id.tv3);
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
                        txtPhone.setVisibility(View.GONE);
                        txtDate.setVisibility(View.GONE);}
                    else {
                        txtDelete.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        txtName.setVisibility(View.VISIBLE);
                        txtPhone.setVisibility(View.VISIBLE);
                        txtDate.setVisibility(View.VISIBLE);}
                }
            });
            txtName.setText(name[position]);
            txtPhone.setText(phone[position]);
            txtDate.setText(new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(date[position])));
            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = LayoutInflater.from(GuestActivity.this).inflate(R.layout.guest_dialog_layout, null);
                    TextInputEditText guestFIO = view1.findViewById(R.id.GuestFIO);
                    guestFIO.setText(name[position]);
                    EditText guestPhone = view1.findViewById(R.id.GuestPhone);
                    guestPhone.setText(phone[position]);
                    DatePicker guestDate = view1.findViewById(R.id.GuestDate);
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(date[position]);
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    guestDate.updateDate(mYear, mMonth, mDay);
                    AlertDialog alertDialog = new MaterialAlertDialogBuilder(GuestActivity.this)
                            .setTitle("Изменение постояльца")
                            .setView(view1)
                            .setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    if (guestFIO.getText().toString().isEmpty() || guestPhone.getText().toString().isEmpty())
                                        Toast.makeText(GuestActivity.this, "Поле не было заполнено!", Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        String mydate = Long.toString(guestDate.getDayOfMonth()) + "/" + Long.toString(guestDate.getMonth()+1) + "/" + Long.toString(guestDate.getYear());
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        Date datep = null;
                                        try {
                                            datep = sdf.parse(mydate);
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }
                                        GuestData data = new GuestData(id[position], Objects.requireNonNull(guestFIO.getText()).toString(), guestPhone.getText().toString(), datep.getTime());
                                        boolean result = dbHelper.GuestUpdateRow(data);
                                        if (result){
                                            displayData(dbHelper.GetAllGuests());
                                            Toast.makeText(GuestActivity.this, "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                        else
                                            Toast.makeText(GuestActivity.this, "Что-то пошло не так, данные не были изменены!", Toast.LENGTH_SHORT).show();}
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(GuestActivity.this);
                    builder.setMessage("Вы действительно хотите удалить запись?")
                            .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dbHelper.DeleteRow(GUESTS, GUEST_ID , id[position]);
                                    displayData(dbHelper.GetAllGuests());
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