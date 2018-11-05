package com.example.annotation.annoAndreflect;

public class AnnoDemo {

    private String str;

    public String getStr() {
        return str;
    }

    @Autowired
    public void setStr(String str) {
        this.str = str;
    }
}
