package database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MedicineDao {

    // 🔹 Medicine CRUD
    @Insert
    long insert(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("SELECT * FROM medicine_info")
    List<Medicine> getAllMedicines();

    @Query("SELECT * FROM medicine_info WHERE medicineId = :id")
    Medicine getMedicineById(int id);

    // Time CRUD
    @Insert
    void insertTime(MedicineTime time);

    @Insert
    void insertTimes(List<MedicineTime> times);

    @Query("SELECT * FROM medicine_time WHERE medicineId = :medicineId")
    List<MedicineTime> getTimesForMedicine(int medicineId);

    @Query("DELETE FROM medicine_time WHERE medicineId = :medicineId")
    void deleteTimesForMedicine(int medicineId);

    //  Relation (Medicine + Times)
    @Transaction
    @Query("SELECT * FROM medicine_info")
    List<MedicineWithTimes> getMedicinesWithTimes();

    @Transaction
    @Query("SELECT * FROM medicine_info WHERE medicineId = :id")
    MedicineWithTimes getMedicineWithTimesById(int id);
}