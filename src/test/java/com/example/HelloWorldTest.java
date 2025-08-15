package com.example;

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
