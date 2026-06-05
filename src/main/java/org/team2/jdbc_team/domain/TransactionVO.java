package org.team2.jdbc_team.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionVO {
    private Integer transactionId;      // transaction_id INT
    private LocalDateTime transactionDate;   // transaction_date DATETIME
    private Integer memberNo;           // member_no INT
    private Long amount;                // amount BIGINT (금액이 커질 수 있으므로 Long 권장)
    private Long balanceAfter;          // balance_after BIGINT
    private String transactionType;     // transaction_type VARCHAR(20)
    private String accountNo;           // account_no VARCHAR(30)
    private String description;         // description VARCHAR(100)
    private String category;            // category VARCHAR(30)
}
