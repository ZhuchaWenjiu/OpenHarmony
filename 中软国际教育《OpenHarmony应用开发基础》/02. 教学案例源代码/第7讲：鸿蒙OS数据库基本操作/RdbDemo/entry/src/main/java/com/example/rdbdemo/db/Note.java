package com.example.rdbdemo.db;

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Column;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据库表--便签记录表
 */
@Entity(tableName = "note")
public class Note extends OrmObject implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name ="date")
    private Date data;
    @Column(name = "content")
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getData() {
        return data;
    }
    public String getDateStr () {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(data);
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Note() {
    }

    public Note(int id) {
        this.id = id;
    }

    public Note(String title, String content, int id) {
        this.data = new Date();
        this.title = title;
        this.content = content;
        this.id = id;
    }
    public Note(String title, String content) {
        this.data = new Date();
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", data=" + data +
                ", content='" + content + '\'' +
                ", id=" + id +
                '}';
    }
}
