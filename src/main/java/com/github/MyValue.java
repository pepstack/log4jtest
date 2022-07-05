package com.github;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MyValue {
    public String name;
    public int age;

    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper(); 

        MyValue value = mapper.readValue("{\"name\":\"Bob\", \"age\":13}", MyValue.class);
    }
}