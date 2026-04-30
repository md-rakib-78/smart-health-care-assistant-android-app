package database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);


    @Query("SELECT * FROM user_info WHERE userId = 1 LIMIT 1")
    User getUser();


    @Query("DELETE FROM user_info")
    void deleteAll();
}