package com.bb.tododemo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.allyants.notifyme.NotifyMe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTasksActivity extends AppCompatActivity {

    private MaterialToolbar addTaskToolbar;
    private EditText addTopic, addNotes, addDate, addTime;
    private MaterialButton addSaveBtn;
    private int pdate, pmonth, pyear, phour, pmin;

    private void initView() {
        addTaskToolbar = findViewById(R.id.addTaskToolbar);
        addTopic = findViewById(R.id.addTopic);
        addNotes = findViewById(R.id.addNotes);
        addDate = findViewById(R.id.addDate);
        addTime = findViewById(R.id.addTime);
        addSaveBtn = findViewById(R.id.addSaveBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        initView();

        Bundle extra = getIntent().getExtras();
        switch (extra.getInt("from")) {
            case 1:
                personal();
                break;
            case 2:
                work();
                break;
            default:
                Toast.makeText(this, "Ran into error with bundle for the add activity", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void personal() {

        setSupportActionBar(addTaskToolbar);

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTasksActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pdate = dayOfMonth;
                        pmonth = month;
                        pyear = year;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(pyear, pmonth, pdate);
                        addDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
                    }
                }, pyear, pmonth, pdate);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTasksActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        phour = hourOfDay;
                        pmin = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, phour, pmin);
                        addTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, 12, 0, false);
                timePickerDialog.updateTime(phour, pmin);
                timePickerDialog.show();
            }
        });

        addSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference personal = db.collection("personal");

                Tasks tasks = new Tasks(addTopic.getText().toString(), addNotes.getText().toString(),
                        addDate.getText().toString(), addTime.getText().toString(), false);

                personal.add(tasks).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Snackbar notify = Snackbar.make(addSaveBtn.getRootView(), "Save Successful! Want to be Notified? Notifications are still beta!",
                                BaseTransientBottomBar.LENGTH_LONG);

                        notify.setAction("Notify Me", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fullDT = addDate.getText().toString().concat(" ").concat(addTime.getText().toString());
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                                Date dateFull = new Date();
                                try {
                                    dateFull = simpleDateFormat.parse(fullDT);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                                        .title(addTopic.getText().toString())
                                        .content(addNotes.getText().toString())
                                        .color(255, 255, 255, 255)
                                        .led_color(255, 255, 255, 255)
                                        .time(dateFull)
                                        .addAction(new Intent(), "Dismiss", true)
                                        .key("test")
                                        .large_icon(R.drawable.ic_alarm)
                                        .build();

                                Snackbar.make(addSaveBtn.getRootView(),
                                        "Relax!! You would be notified on time!! Provided don't kill the app! Please do bear!!!" +
                                                "Maybe updated in the next version",
                                        BaseTransientBottomBar.LENGTH_INDEFINITE)
                                        .setAction("Exit", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                            }
                                        }).show();
                            }
                        }).show();
                        if (!notify.isShown()) {
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTasksActivity.this, "Save Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("FirebaseFailure", e.getMessage());
                    }
                });

            }
        });

    }

    private void work() {

        setSupportActionBar(addTaskToolbar);

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTasksActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pdate = dayOfMonth;
                        pmonth = month;
                        pyear = year;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(pyear, pmonth, pdate);
                        addDate.setText(DateFormat.format("dd/MM/yyyy", calendar));
                    }
                }, pyear, pmonth, pdate);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTasksActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        phour = hourOfDay;
                        pmin = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, phour, pmin);
                        addTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, 12, 0, false);
                timePickerDialog.updateTime(phour, pmin);
                timePickerDialog.show();
            }
        });

        addSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference work = db.collection("work");

                Tasks tasks = new Tasks(addTopic.getText().toString(), addNotes.getText().toString(),
                        addDate.getText().toString(), addTime.getText().toString(), false);

                work.add(tasks).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Snackbar notify = Snackbar.make(addSaveBtn.getRootView(), "Save Successful! Want to be Notified? Notifications are still beta!",
                                BaseTransientBottomBar.LENGTH_LONG);

                        notify.setAction("Notify Me", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fullDT = addDate.getText().toString().concat(" ").concat(addTime.getText().toString());
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
                                Date dateFull = new Date();
                                try {
                                    dateFull = simpleDateFormat.parse(fullDT);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                                        .title(addTopic.getText().toString())
                                        .content(addNotes.getText().toString())
                                        .color(255, 255, 255, 255)
                                        .led_color(255, 255, 255, 255)
                                        .time(dateFull)
                                        .addAction(new Intent(), "Dismiss", true)
                                        .key("test")
                                        .large_icon(R.drawable.ic_alarm)
                                        .build();

                                Snackbar.make(addSaveBtn.getRootView(),
                                        "Relax!! You would be notified on time!! Provided don't kill the app! Please do bear!!!" +
                                                "Maybe updated in the next version",
                                        BaseTransientBottomBar.LENGTH_INDEFINITE)
                                        .setAction("Exit", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                            }
                                        }).show();
                            }
                        }).show();
                        if (!notify.isShown()) {
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTasksActivity.this, "Save Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("FirebaseFailure", e.getMessage());
                    }
                });
            }
        });
    }

}