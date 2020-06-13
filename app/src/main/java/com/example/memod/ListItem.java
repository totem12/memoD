package com.example.memod;


class ListItem {
    private long id = 0;
    private String title = null;
    private String body = null;
    private  String uuid = null;

    long getId() {return id;}
    String getTitle() {return title;}
    String getBody() {return  body;}
    String getUuid() {return uuid;}

    void setId(long id) {this.id = id; }
    void setTitle(String title) {this.title = title;}
    void setBody(String body) {this.body = body;}
    void setUuid(String uuid) {this.uuid = uuid;}
}
