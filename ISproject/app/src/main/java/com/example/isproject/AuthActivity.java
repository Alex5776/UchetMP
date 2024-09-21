package com.example.isproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;

import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    private static final String AUTH = "auth";
    private static final String AUTH_ID = "auth_id";

    DBHelper dbHelper;
    String SearchText;
    EditText SearchString;
    Button btnClear;
    ListView listView;

    String[] login, password, grant;
    int[] id;

    MaterialButton AddButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        btnClear = (Button)findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(onClickListener());
        SearchString = findViewById(R.id.SearchText);
        dbHelper = new DBHelper(this);
        findId();
        displayData(dbHelper.GetAllAuth());
        AddButton = findViewById(R.id.AddButton);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(AuthActivity.this).inflate(R.layout.auth_dialog_layout, null);
                TextInputEditText AuthLogin = view1.findViewById(R.id.AuthLogin);
                TextInputEditText AuthPassword = view1.findViewById(R.id.AuthPassword);
                AutoCompleteTextView authGrant = view1.findViewById(R.id.GrantItem);
                String [] items = {"Пользователь", "Администратор"};
                ArrayAdapter<String> itemadapter = new ArrayAdapter<>(AuthActivity.this, R.layout.items_list, items);
                authGrant.setAdapter(itemadapter);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(AuthActivity.this)
                        .setTitle("Добавление пользователя")
                        .setView(view1)
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                AuthData data = new AuthData(null, Objects.requireNonNull(AuthLogin.getText()).toString(), Objects.requireNonNull(AuthPassword.getText()).toString(), authGrant.getText().toString());
                                if (data.authLogin.isEmpty() || data.authPassword.isEmpty() || data.authGrant.isEmpty())
                                    Toast.makeText(AuthActivity.this, "Одно из полей не было заполнено!", Toast.LENGTH_SHORT).show();
                                else if (dbHelper.checkAuth(data.authLogin, 0))
                                {
                                boolean result = dbHelper.InsertAuthRow(data);
                                if (result){
                                    displayData(dbHelper.GetAllAuth());
                                    Toast.makeText(AuthActivity.this, "Данные успешно добавлены!", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                                else
                                    Toast.makeText(AuthActivity.this, "Что-то пошло не так, данные не были добавлены!", Toast.LENGTH_SHORT).show();}
                                else
                                    Toast.makeText(AuthActivity.this, "Пользователь с таким именем уже существует!", Toast.LENGTH_SHORT).show();
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
                    LinkedList<AuthData> list = dbHelper.SearchAuth(SearchText);
                    if (list.isEmpty()) {
                        Toast.makeText(AuthActivity.this, "Ничего не найдено!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        displayData(list);
                }
                else
                    displayData(dbHelper.GetAllAuth());
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

    private OnClickListener onClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchString.setText("");
            }
        };
    }

    private void findId(){
        listView = findViewById(R.id.lv);
    }

    private void displayData(LinkedList<AuthData> list){
        listView = findViewById(R.id.lv);
        int count = list.size();
        id = new int[count];
        login = new String[count];
        password = new String[count];
        grant = new String[count];
        int i = 0;
        for (AuthData data:list) {
            id[i] = data.auth_id;
            login[i] = data.authLogin;
            password[i] = data.authPassword;
            grant[i] = data.authGrant;
            i++;
        }
        CustAdapter custAdapter = new CustAdapter();
        listView.setAdapter(custAdapter);
    }

    public void MainButtonClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

   private class CustAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return login.length;
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
            TextView txtLogin, txtPassword, txtGrant;
            ImageButton txtEdit, txtDelete;
            CardView cardView;
            convertView = LayoutInflater.from(AuthActivity.this).inflate(R.layout.auth_data_item, parent, false);
            txtLogin = convertView.findViewById(R.id.txt_login);
            txtGrant = convertView.findViewById(R.id.txt_grant);
            txtEdit = convertView.findViewById(R.id.txt_edit);
            txtDelete = convertView.findViewById(R.id.txt_delete_bin);
            cardView = convertView.findViewById(R.id.cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Random random = new Random();
                    //cardView.setCardBackgroundColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                    if (txtDelete.getVisibility() == View.GONE)
                    {
                    txtDelete.setVisibility(View.VISIBLE);
                    txtEdit.setVisibility(View.VISIBLE);
                    txtLogin.setVisibility(View.GONE);
                    txtGrant.setVisibility(View.GONE);}
                    else {
                        txtDelete.setVisibility(View.GONE);
                        txtEdit.setVisibility(View.GONE);
                        txtLogin.setVisibility(View.VISIBLE);
                        txtGrant.setVisibility(View.VISIBLE);}
                }
            });
            txtLogin.setText(login[position]);
            txtGrant.setText(grant[position]);
            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = LayoutInflater.from(AuthActivity.this).inflate(R.layout.auth_dialog_layout, null);
                    TextInputEditText AuthLogin = view1.findViewById(R.id.AuthLogin);
                    AuthLogin.setText(login[position]);
                    TextInputEditText AuthPassword = view1.findViewById(R.id.AuthPassword);
                    AutoCompleteTextView authGrant = view1.findViewById(R.id.GrantItem);
                    authGrant.setText(grant[position]);
                    String [] items = {"Пользователь", "Администратор"};
                    ArrayAdapter<String> itemadapter = new ArrayAdapter<>(AuthActivity.this, R.layout.items_list, items);
                    authGrant.setAdapter(itemadapter);
                    AlertDialog alertDialog = new MaterialAlertDialogBuilder(AuthActivity.this)
                            .setTitle("Изменение пользователя")
                            .setView(view1)
                            .setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    AuthData data = new AuthData(id[position], Objects.requireNonNull(AuthLogin.getText()).toString(), Objects.requireNonNull(AuthPassword.getText()).toString(), authGrant.getText().toString());
                                    if (data.authLogin.isEmpty() || data.authPassword.isEmpty() || data.authGrant.isEmpty())
                                        Toast.makeText(AuthActivity.this, "Одно из полей не было заполнено!", Toast.LENGTH_SHORT).show();
                                    else if (dbHelper.checkAuth(data.authLogin, id[position]))
                                    {
                                        boolean result = dbHelper.AuthUpdateRow(data);
                                        if (result){
                                            displayData(dbHelper.GetAllAuth());
                                            Toast.makeText(AuthActivity.this, "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                        else
                                            Toast.makeText(AuthActivity.this, "Что-то пошло не так, данные не были изменены!", Toast.LENGTH_SHORT).show();}
                                    else
                                        Toast.makeText(AuthActivity.this, "Пользователь с таким именем уже существует!", Toast.LENGTH_SHORT).show();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                    builder.setMessage("Вы действительно хотите удалить запись?")
                            .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dbHelper.DeleteRow(AUTH, AUTH_ID , id[position]);
                            displayData(dbHelper.GetAllAuth());
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