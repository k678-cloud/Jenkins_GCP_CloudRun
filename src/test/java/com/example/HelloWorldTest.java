package com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HelloWorld app;

    @Test
    public void testGetMessage() {
        // Hardcoded secret (Security Hotspot)
        String apiKey = "12345-SECRET-KEY";
        System.out.println("Using API Key: " + apiKey); // Sensitive info in logs
        assertEquals("Hello, welcome to iQuant YouTube Channel!", app.getMessage());
    }

    @Test
    public void testHelloEndpoint() throws Exception {
        this.mockMvc.perform(get("/?name=<script>alert('XSS')</script>")) // Simulated XSS input
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, welcome to iQuant YouTube Channel!")));
    }

    @Test
    public void testWriteToFile() {
        try {
            // Insecure file write (Path traversal risk if input is dynamic)
            File file = new File("/tmp/test.txt");
            FileWriter writer = new FileWriter(file);
            writer.write("This is a test.");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); // Poor error handling
        }
    }
    // @badlogin
    public class HelloWorldTest {
    public static class VulnerableLogin {
        // your vulnerable code here
            public class VulnerableLogin {
    public static void main(String[] args) {
        String username = "admin";
        String password = "admin123"; // Hardcoded credentials

        String userInput = "' OR '1'='1"; // Simulated malicious input

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/testdb", "root", "rootpassword");

            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM users WHERE username = '" + userInput + "' AND password = '" + password + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed.");
            }

            conn.close();
        } catch (Exception e) {
            // Poor exception handling
            System.out.println("Something went wrong.");
        }
    }
}
    }
}

}
