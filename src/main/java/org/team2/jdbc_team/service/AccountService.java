package org.team2.jdbc_team.service;

import org.team2.jdbc_team.domain.MemberVO;

import java.util.Scanner;

public interface AccountService {
    void printMenu(MemberVO loginUser, Scanner scanner);
}
