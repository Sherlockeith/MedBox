package com.example.jwtapplication.dao;

import com.example.jwtapplication.util.MyApp;
import com.example.jwtapplication.db.TokenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Token {
    @Id
    Long id;
    @Unique
    String username;
    String key;
    String selectStr;
    @Generated(hash = 1415621379)
    public Token(Long id, String username, String key, String selectStr) {
        this.id = id;
        this.username = username;
        this.key = key;
        this.selectStr = selectStr;
    }
    @Generated(hash = 79808889)
    public Token() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getSelectStr() {
        return this.selectStr;
    }
    public void setSelectStr(String selectStr) {
        this.selectStr = selectStr;
    }
    public static Token getToken(){
        TokenDao dao = MyApp.getInstance().getDaoSession().getTokenDao();
        return dao.queryBuilder()
                .where(TokenDao.Properties.SelectStr.eq("TEST"))
                .build()
                .list().get(0);
    }
    public static void savetOKEN(Token token) {
        TokenDao dao = MyApp.getInstance().getDaoSession().getTokenDao();
        dao.deleteAll();
        dao.insertInTx(token);
    }
}
