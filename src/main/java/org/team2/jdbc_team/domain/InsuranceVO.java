package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceVO {
    String insurance_name;
    String inst_name;
    long monthly_payment;
    Date maturity_date;
    String insurance_account;
}
