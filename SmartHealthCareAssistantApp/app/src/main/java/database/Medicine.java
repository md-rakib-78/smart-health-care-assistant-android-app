package database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine_info")
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String MedicineName;
    public String startDate;
    public String endDate;
    public String frequency;
    public String times;
}