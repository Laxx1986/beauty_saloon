package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Builder
@Getter
@AllArgsConstructor
public class ServiceLengthDTO {
    private long serviceLengthId;
    private Timestamp length;
}
