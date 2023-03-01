package com.example.map_test1.custom;

import java.util.UUID;

public class CrimeItem {
    String name; // crime name

    public CrimeItem(String crimeName) {
        this.name = crimeName;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
