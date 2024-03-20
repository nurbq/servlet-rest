package lab.andersen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityShortDto {

    private String userName;
    private String description;
    private LocalDateTime dateTime;
}
