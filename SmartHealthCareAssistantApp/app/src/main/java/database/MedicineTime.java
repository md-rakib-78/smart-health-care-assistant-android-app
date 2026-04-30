package database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "medicine_time",
        foreignKeys = @ForeignKey(
                entity = Medicine.class,
                parentColumns = "medicineId",
                childColumns = "medicineId",
                onDelete = ForeignKey.CASCADE
        )
)
public class MedicineTime {

    @PrimaryKey(autoGenerate = true)
    public int timeId;

    public int medicineId;
    public String time;   // "08:00"
    public int alarmId;   // IMPORTANT

    public MedicineTime(int medicineId, String time, int alarmId) {
        this.medicineId = medicineId;
        this.time = time;
        this.alarmId = alarmId;
    }
}
