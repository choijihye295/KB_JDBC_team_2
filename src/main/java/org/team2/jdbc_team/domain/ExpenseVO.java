package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseVO {

    private String category;
    private int amount;
    private int totalAmount;


    public ExpenseVO(String category, int amount) {
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String toString() {

        if (totalAmount > 0) {
            double percentage = ((double) amount / totalAmount) * 100;
            return "• %s : %,d원 (%.1f%%)".formatted(category, amount, percentage);
        }
        return "• %s : %,d원".formatted(category, amount);
    }
}