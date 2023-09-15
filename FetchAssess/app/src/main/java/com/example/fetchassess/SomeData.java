package com.example.fetchassess;

import java.io.Serializable;
import com.squareup.moshi.Json;
public class SomeData implements Serializable{
    @Json(name = "id")
    String someId;
    @Json(name = "listId")
    String someListId;
    @Json(name = "name")
    String someName;

    public SomeData(String someId, String someListId, String someName) {
        this.someId = someId;
        this.someListId = someListId;
        this.someName = someName;
    }

    public String getSomeId() {
        return someId;
    }

    public void setSomeId(String someId) {
        this.someId = someId;
    }

    public String getSomeListId() {
        return someListId;
    }

    public void setSomeListId(String someListId) {
        this.someListId = someListId;
    }

    public String getSomeName() {
        return someName;
    }

    public void setSomeName(String someName) {
        this.someName = someName;
    }
}
