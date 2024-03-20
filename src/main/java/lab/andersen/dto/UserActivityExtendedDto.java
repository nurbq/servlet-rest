package lab.andersen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityExtendedDto {

    private int id;
    private int userId;
    private String description;
    private Timestamp dateTime;
    private String userName;
}
