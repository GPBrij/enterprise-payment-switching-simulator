package za.co.goalpostbrij.epss.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "routing_rules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutingRule {

    @Id
    private UUID id;

    @Column(name = "bin_prefix", nullable = false, unique = true, length = 8)
    private String binPrefix;

    @Column(name = "issuer_bank", nullable = false, length = 100)
    private String issuerBank;

    @Column(name = "route_code", nullable = false, length = 50)
    private String routeCode;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
