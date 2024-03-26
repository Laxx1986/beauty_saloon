package dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
@AllArgsConstructor
public class ServiceProviderDTO {
    private long serviceProviderId;
    private String serviceProviderName;
}
