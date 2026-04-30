package database;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MedicineWithTimes {

    @Embedded
    public Medicine medicine;

    @Relation(parentColumn = "medicineId", entityColumn = "medicineId")
    public List<MedicineTime> times;
}
