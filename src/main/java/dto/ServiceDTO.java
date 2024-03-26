package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import model.ServiceLength;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ServiceDTO {
    private long serviceId;
    private String serviceName;
    private int serviceLength;
    private int servicePrice;
}
