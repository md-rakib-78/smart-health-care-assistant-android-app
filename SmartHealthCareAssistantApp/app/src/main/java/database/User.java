package database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info")
public class User {

    public String name;
    public String image;
    public String dateOfBirth;
    public String gender;
    public String blood;
    public String weight;
    public String height;
    @PrimaryKey()
    public String email;
    public String password;
}