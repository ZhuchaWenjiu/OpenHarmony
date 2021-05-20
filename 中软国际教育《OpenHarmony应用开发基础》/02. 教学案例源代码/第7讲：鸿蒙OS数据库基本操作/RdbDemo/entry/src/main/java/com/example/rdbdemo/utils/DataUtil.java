package com.example.rdbdemo.utils;

import com.example.rdbdemo.db.Note;
import com.example.rdbdemo.db.NoteStore;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    private static OrmContext context;

    public static void onInitialize(Context ct) {
        //数据库初始化
        DatabaseHelper helper = new DatabaseHelper(ct);
        context = helper.getOrmContext("NoteStore", "NoteStore.db", NoteStore.class);
    }

    public static boolean addNote(Note note) {
        boolean flag = context.insert(note);
        if (flag) {
            flag = context.flush();
            LogUtil.inof("add:" + note + ",flag:" + flag);
        }

        return flag;
    }

    public static boolean deleteNote(Integer id) {
        OrmPredicates predicates = context.where(Note.class);
        predicates.equalTo("id", id);
        List<Note> notes = context.query(predicates);
        return deleteNote(notes.get(0));
    }

    public static boolean deleteNote(Note note) {
        boolean flag = context.delete(note);
        LogUtil.inof("delete :" + note + ",flag:" + flag);
        if (flag) {
            flag = context.flush();
        }
        LogUtil.inof("delete:" + flag);
        return flag;
    }

    public static List<Note> getAll() {
        OrmPredicates query = context.where(Note.class).orderByDesc("id");
        List<Note> notes = context.query(query);
        if (notes == null) {
            return new ArrayList<>();
        }
        LogUtil.inof("getAll:" + notes);
        return notes;
    }

    public static boolean updateNote(Note note) {
        OrmPredicates predicates = context.where(Note.class);
        predicates.equalTo("id", note.getId());
        List<Note> notes = context.query(predicates);
        Note note2 = notes.get(0);
        note2.setTitle(note.getTitle());
        note2.setContent(note.getContent());
        boolean flag = context.update(note2);
        LogUtil.inof("delete :" + note + ",flag:" + flag);
        if (flag) {
            flag = context.flush();
        }
        LogUtil.inof("delete:" + flag);
        return flag;

    }
}
