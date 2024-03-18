package lab.andersen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {

    private int id;
    private int userId;
    private String description;
    private Timestamp dateTime;
}
