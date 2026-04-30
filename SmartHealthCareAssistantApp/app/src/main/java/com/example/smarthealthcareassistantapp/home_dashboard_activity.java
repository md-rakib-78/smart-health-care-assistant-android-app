package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import database.AppDatabase;
import database.MedicineTime;
import database.MedicineWithTimes;
import database.User;

public class home_dashboard_activity extends AppCompatActivity {

    LinearLayout profileicon1, profileicon2, my_medicines1, my_medicines2;
    ImageView profilePic;
    TextView nameText, dayStatus;
    RecyclerView recyclerView;
    RelativeLayout emptyImg;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_dashboard);

        requestPermissions(new String[]{
                android.Manifest.permission.POST_NOTIFICATIONS
        }, 1);


        profileicon1 = findViewById(R.id.profileIcon);
        profileicon2 = findViewById(R.id.profileIcon2);
        my_medicines1 = findViewById(R.id.med11);
        my_medicines2 = findViewById(R.id.med12);
        recyclerView = findViewById(R.id.recyclerViewMed);
        emptyImg = findViewById(R.id.emptyImage);
        nameText = findViewById(R.id.nameText);
        dayStatus = findViewById(R.id.dayStatus);
        profilePic = findViewById(R.id.profilePic);

        dayStatus.setText(getGreetingMessage());

        loadUserData();

        profileicon1.setOnClickListener(v -> startActivity(new Intent(home_dashboard_activity.this, profile_activity.class)));
        profileicon2.setOnClickListener(v -> startActivity(new Intent(home_dashboard_activity.this, profile_activity.class)));
        my_medicines1.setOnClickListener(v -> startActivity(new Intent(home_dashboard_activity.this, add_medicine.class)));
        my_medicines2.setOnClickListener(v -> startActivity(new Intent(home_dashboard_activity.this, add_medicine.class)));

        loadMedicineData();
    }

    private void loadUserData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            User user = db.userDao().getUser();

            runOnUiThread(() -> {
                if (user != null) {
                    nameText.setText(user.name);
                    if (user.image != null) {
                        File file = new File(user.image);
                        if (file.exists()) {
                            profilePic.setImageURI(Uri.fromFile(file));
                        }
                    }
                }
            });
        }).start();
    }

    private void loadMedicineData() {
        new Thread(() -> {
            createArrayList();
            runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(new MedicineAdapter());

                if (arrayList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyImg.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyImg.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    public String getGreetingMessage() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 12) return "Good Morning \uD83C\uDF05";
        else if (hour >= 12 && hour < 17) return "Good Afternoon \u2600\uFE0F";
        else if (hour >= 17 && hour < 21) return "Good Evening \uD83C\uDF07";
        else return "Good Night \uD83C\uDF19";
    }

    private void createArrayList() {
        arrayList.clear();
        AppDatabase db = AppDatabase.getDatabase(this);
        List<MedicineWithTimes> list = db.medicineDao().getMedicinesWithTimes();
        long now = System.currentTimeMillis();
        List<HashMap<String, String>> tempList = new ArrayList<>();

        for (MedicineWithTimes item : list) {
            for (MedicineTime t : item.times) {
                long timeMillis = convertToMillis(t.time);
                if (timeMillis == -1) continue;

                if (timeMillis < now) {
                    timeMillis += 24 * 60 * 60 * 1000;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("medicine", item.medicine.MedicineName);
                map.put("time", t.time);
                map.put("timeMillis", String.valueOf(timeMillis));
                tempList.add(map);
            }
        }

        Collections.sort(tempList, (a, b) -> Long.compare(Long.parseLong(a.get("timeMillis")), Long.parseLong(b.get("timeMillis"))));

        for (HashMap<String, String> m : tempList) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("title", m.get("medicine"));
            hashMap.put("time", m.get("time"));
            hashMap.put("Status", "Upcoming");
            arrayList.add(hashMap);
        }
    }

    private long convertToMillis(String time) {
        if (time == null) return -1;
        try {
            String cleanTime = time.toUpperCase().trim();
            boolean isPM = cleanTime.contains("PM");
            boolean isAM = cleanTime.contains("AM");

            String timePart = cleanTime.replace("AM", "").replace("PM", "").trim();
            String[] parts = timePart.split(":");
            if (parts.length < 2) return -1;

            int hour = Integer.parseInt(parts[0].trim());
            int minute = Integer.parseInt(parts[1].trim());

            if (isPM && hour < 12) hour += 12;
            else if (isAM && hour == 12) hour = 0;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTimeInMillis();
        } catch (Exception e) {
            return -1;
        }
    }

    private class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
        @NonNull
        @Override
        public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_medicine, parent, false);
            return new MedicineViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
            HashMap<String, String> map = arrayList.get(position);
            holder.title.setText(map.get("title"));
            holder.time.setText(map.get("time"));
            holder.status.setText(map.get("Status"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class MedicineViewHolder extends RecyclerView.ViewHolder {
            TextView title, time, status;
            public MedicineViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.titleMed);
                time = itemView.findViewById(R.id.time);
                status = itemView.findViewById(R.id.statusMed);
            }
        }
    }
}
