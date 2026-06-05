package org.team2.jdbc_team.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    private Integer memberNo;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthDate;
    private LocalDateTime createAt;
}
