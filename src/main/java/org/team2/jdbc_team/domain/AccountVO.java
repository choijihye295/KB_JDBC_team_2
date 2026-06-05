package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountVO {
    private String accountNo;
    private Integer memberNo;
    private Integer instCode;
    private String accountName;
    private String accountType;
    private String instName;
    private BigInteger amount;
    private LocalDateTime openDate;

}
