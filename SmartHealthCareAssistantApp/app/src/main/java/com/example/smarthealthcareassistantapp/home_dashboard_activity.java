package com.example.smarthealthcareassistantapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.Time;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import database.AppDatabase;
import database.Medicine;
import database.User;

public class home_dashboard_activity extends AppCompatActivity {

    LinearLayout profileicon1,profileicon2,my_medicines1,my_medicines2;

    RecyclerView recyclerView;
    RelativeLayout emptyImg;
    TextView emptyText,nameText;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_dashboard);

        profileicon1 = findViewById(R.id.profileIcon);
        profileicon2 = findViewById(R.id.profileIcon2);
        my_medicines1 = findViewById(R.id.med11);
        my_medicines2 = findViewById(R.id.med12);
        recyclerView = findViewById(R.id.recyclerViewMed);
        emptyImg = findViewById(R.id.emptyImage);
        nameText = findViewById(R.id.nameText);

        //Read user table From Room Database
        AppDatabase db = AppDatabase.getDatabase(this);
        List<User> list = db.userDao().getAllUsers();
        nameText.setText(list.get(0).name+"\uD83D\uDC4B");



        //------------------------------------------ Button Action Section ---------------
        profileicon1.setOnClickListener(v->{
            startActivity(new Intent(home_dashboard_activity.this, profile_activity.class));
        });

        profileicon2.setOnClickListener(v->{
            startActivity(new Intent(home_dashboard_activity.this, profile_activity.class));
        });

        my_medicines1.setOnClickListener(v->{
            startActivity(new Intent(home_dashboard_activity.this, my_medicines.class));
        });

        my_medicines2.setOnClickListener(v->{
            startActivity(new Intent(home_dashboard_activity.this, my_medicines.class));
        });


        createArrayList();

        //Set Recycle View
        recyclerView.setAdapter(new MedicineAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if(arrayList.size() == 0){
            recyclerView.setVisibility(View.GONE);
            emptyImg.setVisibility(View.VISIBLE);


        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.GONE);
        }



    }


    private void hashMapMedAdd(String title, String time, String Status) {


        hashMap = new HashMap<>();
        hashMap.put("title", title);
        hashMap.put("time", time);
        hashMap.put("Status", Status);
        arrayList.add(hashMap);
    }

    private void createArrayList() {

        //Read Medicine table From Room Database
        AppDatabase db = AppDatabase.getDatabase(this);

        List<Medicine> list = db.medicineDao().getAllMedicines();

        for (Medicine m : list) {
            hashMapMedAdd(m.MedicineName, m.times, m.frequency);
        }

    }

    // Adapter For Medicine Recycle View
    private class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>{


        @NonNull
        @Override
        public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.listitem_medicine, parent, false);
            return new MedicineViewHolder(view);
        }


        private class MedicineViewHolder extends RecyclerView.ViewHolder {

            TextView title, time, status;

            public MedicineViewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.titleMed);
                time = itemView.findViewById(R.id.time);
                status = itemView.findViewById(R.id.statusMed);
            }

        }


        @Override
        public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {

            hashMap = arrayList.get(position);
            holder.title.setText(hashMap.get("title"));
            holder.time.setText(hashMap.get("time"));
            holder.status.setText(hashMap.get("Status"));
        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }

}