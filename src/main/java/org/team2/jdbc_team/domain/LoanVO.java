package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanVO {
    private String loan_name;
    private String inst_name;
    private long loan_amount;
    private BigDecimal interest_rate;
    private Date loan_date;
    private String loan_account;
}
