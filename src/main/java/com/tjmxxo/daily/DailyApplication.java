package com.tjmxxo.daily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class DailyApplication {
    public static ArrayList<String> ARGLIST = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("asdasd".split(",").length);
        if (args.length < 1) {
            ARGLIST.add("savefile");
        }
        ARGLIST.addAll(Arrays.asList(args));
        SpringApplication.run(DailyApplication.class, args);
    }
}
