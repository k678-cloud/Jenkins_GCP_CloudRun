package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
    # MongoDB
MONGODB_URI=mongodb+srv://user:password@cluster0.mongodb.net/test?retryWrites=true&w=majority

# MySQL
MYSQL_PASSWORD=supersecret123!

# Generic High Entropy
SECRET_KEY=9f8e7d6c5b4a3f2e1d0c9b8a7f6e5d4c
    @Test
    public void testGetMessage() {
        assertEquals("Hello, welcome to iQuant YouTube Channel!", app.getMessage());
    }
    
    @Test
    public void testHelloEndpoint() throws Exception {
        this.mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Hello, welcome to iQuant YouTube Channel!")));
    }
}
