package com.example.isproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class OutPutActivity extends AppCompatActivity {

    Button button1, button2, button3;
    DBHelper dbHelper;
    String fileName, fileExpansion;
    private File filePath = null;
    String ReportString, SupName, ProdName, SupDate;
    int SupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_out_put);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fileName = "Report";
        fileExpansion = ".docx";
    }

    public void OutPut1ButtonClick(View view){
        boolean result = true;
        CreateNewFile();
        dbHelper = new DBHelper(this);
        LinkedList<ReserveData> list = dbHelper.GetAllReserve();
        int i = 0;
        ReportString = "Отчёт бронирование.\n";
        for (ReserveData data:list) {
            SupName = data.guest_name;
            SupId = data.guest_id;
            SupDate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(dbHelper.GetGuestsDate(SupId)));
            ProdName = data.event_name;
            ReportString += "\n" + "ФИО: " + SupName + ";\n" + "Дата рождения: " + SupDate + ";\n" + "Мероприятие: " + ProdName + ";\n";
            i++;
        }

        try {
            XWPFDocument xwpfDocument = new XWPFDocument();
            XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
            XWPFRun xwpfRun = xwpfParagraph.createRun();
            if (ReportString.contains("\n")) {
                String[] lines = ReportString.split("\n");
                xwpfRun.setText(lines[0], 0);
                for(i=1;i<lines.length;i++){
                    xwpfRun.addBreak();
                    xwpfRun.setText(lines[i]);
                }
            } else {
                xwpfRun.setText(ReportString, 0);}
            xwpfRun.setFontSize(24);
            xwpfRun.setFontFamily("Times New Roman");

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            xwpfDocument.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            xwpfDocument.close();

        }
        catch (Exception e){
        result = false;
        }
        if (result)
            Toast.makeText(OutPutActivity.this, "Отчёт успешно сохранён!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(OutPutActivity.this, "Что-то пошло не так, отчёт не был сохранён.", Toast.LENGTH_SHORT).show();
    }



    public void OutPut2ButtonClick(View view){
        boolean result = true;
        CreateNewFile();
        dbHelper = new DBHelper(this);
        LinkedList<GuestData> list = dbHelper.GetAllGuests();
        int i = 0;
        Calendar c = Calendar.getInstance();
        int mYear;
        int mMonth;
        int mDay;
        ReportString = "Отчёт постояльцы.\n";
        for (GuestData data:list) {
            SupName = data.guest_name;
            ProdName = data.guest_phone;
            SupId = data.guest_id;
            SupDate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(dbHelper.GetGuestsDate(SupId)));
            ReportString += "\n" + "ФИО: " + SupName + ";\n" + "Номер телефона: " + ProdName + ";\n"
                    + "Дата рождения: " + SupDate
                    + ";\n";
        }

        try {
            XWPFDocument xwpfDocument = new XWPFDocument();
            XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
            XWPFRun xwpfRun = xwpfParagraph.createRun();
            if (ReportString.contains("\n")) {
                String[] lines = ReportString.split("\n");
                xwpfRun.setText(lines[0], 0);
                for(i=1;i<lines.length;i++){
                    xwpfRun.addBreak();
                    xwpfRun.setText(lines[i]);
                }
            } else {
                xwpfRun.setText(ReportString, 0);}
            xwpfRun.setFontSize(24);
            xwpfRun.setFontFamily("Times New Roman");

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            xwpfDocument.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            xwpfDocument.close();

        }
        catch (Exception e){
        result = false;
    }
        if (result)
            Toast.makeText(OutPutActivity.this, "Отчёт успешно сохранён!", Toast.LENGTH_SHORT).show();
        else
                Toast.makeText(OutPutActivity.this, "Что-то пошло не так, отчёт не был сохранён.", Toast.LENGTH_SHORT).show();
    }

    public void OutPut3ButtonClick(View view){
    boolean result = true;
        CreateNewFile();
        dbHelper = new DBHelper(this);
        LinkedList<EventData> list = dbHelper.GetAllEvent();
        int i = 0;
        Calendar c = Calendar.getInstance();
        int mYear;
        int mMonth;
        int mDay;
        ReportString = "Отчёт мероприятия.\n";
        for (EventData data:list) {
            SupName = data.event_name;
            ProdName = Integer.toString(data.event_place);
            SupId = data.event_id;
            SupDate = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(dbHelper.GetGuestsDate(SupId)));
            ReportString += "\n" + "Мероприятие: " + SupName + ";\n" + "Количество мест: " + ProdName + ";\n"
                    + "Дата проведения: " + SupDate
                    + ";\n";
        }

        try {
            XWPFDocument xwpfDocument = new XWPFDocument();
            XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
            XWPFRun xwpfRun = xwpfParagraph.createRun();
            if (ReportString.contains("\n")) {
                String[] lines = ReportString.split("\n");
                xwpfRun.setText(lines[0], 0);
                for(i=1;i<lines.length;i++){
                    xwpfRun.addBreak();
                    xwpfRun.setText(lines[i]);
                }
            } else {
                xwpfRun.setText(ReportString, 0);}
            xwpfRun.setFontSize(24);
            xwpfRun.setFontFamily("Times New Roman");

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            xwpfDocument.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            xwpfDocument.close();

        }
        catch (Exception e){
        result = false;
    }
        if (result)
            Toast.makeText(OutPutActivity.this, "Отчёт успешно сохранён!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(OutPutActivity.this, "Что-то пошло не так, отчёт не был сохранён.", Toast.LENGTH_SHORT).show();
    }

    public void MainButtonClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void CreateNewFile(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName + fileExpansion);
        int i = 0;
        if (filePath.exists()){
            do {
                i ++;
                filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName + Integer.toString(i) + fileExpansion);
            }while (filePath.exists());}
        try {
            filePath.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}