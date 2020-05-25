package com.example.searchengine;

public class ResultData {

    public String _id;
    public String URL;
    public String Name;
    public float Relavents;

    public ResultData(String _id, String URL, String name, float relavents) {
        this._id = _id;
        this.URL = URL;
        Name = name;
        Relavents = relavents;
    }
}
