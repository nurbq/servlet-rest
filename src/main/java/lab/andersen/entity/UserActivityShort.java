package lab.andersen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityShort {

    private String userName;
    private String description;
    private LocalDateTime dateTime;
}
