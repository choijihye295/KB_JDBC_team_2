package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAssetVO {
    private String yearMonth;
    private int netAsset;
}