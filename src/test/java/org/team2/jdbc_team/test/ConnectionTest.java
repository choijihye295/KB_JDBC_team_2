package org.team2.jdbc_team.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionTest {
    @Test
    @DisplayName("jdbc_team 데이터베이스 접속")
    public void testConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:8080/jdbc_team";
        String id = "team2";
        String password = "1234";
        Connection conn = DriverManager.getConnection(url, id, password);
        System.out.println("testConnection() DB 연결 성공");
        conn.close();
    }

    @Test
    @DisplayName("jdbc_team에 접속한다.(자동닫기)")
    public void testConnection2() throws SQLException {
        try(Connection conn = org.team2.jdbc_team.common.JDBCUtil.getConnection()){
            System.out.println("DB 연결 성공");
        }
    }
}
