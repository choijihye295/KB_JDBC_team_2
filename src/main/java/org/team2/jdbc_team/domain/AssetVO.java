package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetVO {

    private int totalAsset;

    private int totalLoan;

    public int getNetAsset() {
        return totalAsset - totalLoan;
    }

    @Override
    public String toString() {

        return """
                
                총 자산 : %d
                총 부채 : %d
                순 자산 : %d
                """.formatted(
                totalAsset,
                totalLoan,
                getNetAsset()
        );
    }
}