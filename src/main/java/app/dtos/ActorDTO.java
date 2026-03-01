package app.dtos;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActorDTO {

    private Long id;
    private String name;
}