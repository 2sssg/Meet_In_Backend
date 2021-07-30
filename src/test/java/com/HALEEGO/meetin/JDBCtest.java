package com.HALEEGO.meetin;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

public class JDBCtest {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnection() {

        try(Connection con =
                    DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/Meet_In?serverTimezone=Asia/Seoul",
                            "dmswl",
                            "dmswl0818")){
            System.out.println(con+"dddd");
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
