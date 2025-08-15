package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Base64;

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
        // 🔐 1. Hardcoded Secret
        String apiKey = "12345-SECRET-KEY";
        System.out.println("Using API Key: " + apiKey); // 🔓 2. Sensitive Info in Logs
        assertEquals("Hello, welcome to iQuant YouTube Channel!", app.getMessage());
    }

    @Test
    public void testHelloEndpoint() throws Exception {
        // 🧨 3. XSS Injection Simulation
        this.mockMvc.perform(get("/?name=<script>alert('XSS')</script>"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, welcome to iQuant YouTube Channel!")));
    }

    @Test
    public void testWriteToFile() {
        try {
            // 📂 4. Insecure File Write (Path Traversal Risk)
            File file = new File("/tmp/test.txt");
            FileWriter writer = new FileWriter(file);
            writer.write("This is a test.");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); // 🪓 5. Poor Exception Handling
        }
    }

    @Test
    public void testSQLInjection() {
        try {
            // 🧬 6. SQL Injection Risk
            String userInput = "'; DROP TABLE users; --";
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
            Statement stmt = conn.createStatement();
            stmt.execute("SELECT * FROM users WHERE name = '" + userInput + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsecureDeserialization() {
        try {
            // 🧟 7. Insecure Deserialization
            byte[] data = Base64.getDecoder().decode("rO0ABXNyABFqYXZhLnV0aWwuQXJyYXlMaXN0x...");
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUnvalidatedRedirect() throws Exception {
        // 🚪 8. Open Redirect
        String redirectUrl = "http://malicious.com";
        mockMvc.perform(get("/redirect?url=" + redirectUrl))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testCommandInjection() {
        try {
            // 🧨 9. Command Injection
            String userInput = "ls"; // could be "rm -rf /"
            Runtime.getRuntime().exec("sh -c " + userInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void testUnrestrictedURLAccess() {
    //     try {
    //         // 🌐 10. SSRF (Server-Side Request Forgery)
    //         URL url = new URL("http://localhost:8080/internal-api");
    //         BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             System.out.println(line);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}


// package com.example;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.hamcrest.Matchers.containsString;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class HelloWorldTest {

//     @Autowired
//     private MockMvc mockMvc;
    
//     @Autowired
//     private HelloWorld app;
    
//     @Test
//     public void testGetMessage() {
//         assertEquals("Hello, welcome to iQuant YouTube Channel!", app.getMessage());
//     }
    
//     @Test
//     public void testHelloEndpoint() throws Exception {
//         this.mockMvc.perform(get("/"))
//             .andDo(print())
//             .andExpect(status().isOk())
//             .andExpect(content().string(containsString("Hello, welcome to iQuant YouTube Channel!")));
//     }
// }
