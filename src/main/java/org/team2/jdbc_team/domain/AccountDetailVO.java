package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailVO {
    private String accountType;
    private String name;
    private int amount;
}