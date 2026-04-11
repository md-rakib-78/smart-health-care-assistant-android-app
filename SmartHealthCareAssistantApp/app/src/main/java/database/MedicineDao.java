package database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void insert(Medicine medicine);

    @Query("SELECT * FROM medicine_info")
    List<Medicine> getAllMedicines();
}