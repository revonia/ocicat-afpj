package com.ocicatteam.afpj.db;

import com.ocicatteam.afpj.algo.FpMap;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by SiriusWangs on 2017/4/20.
 */
public class Db {

    //改这个同时也要修改prepareFind()中的sql
    private static int findLimit = 200;
    private Connection conn = null;

    private HashMap<Integer, Boolean> fp;

    private PreparedStatement stmt = null;

    public Db(String dbPath) throws Exception {

        Class.forName("org.sqlite.JDBC");

        // create a database connection
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        fp = new HashMap<>();
    }

    public String findSongNameById(int id) throws Exception{
        String sql = "SELECT name FROM songs WHERE id = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        stmt = null;

        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public int findIdBySongName(String name) throws Exception{
        String sql = "SELECT id FROM songs WHERE name = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, name);
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        stmt = null;
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    public int insertSong(String name) throws Exception{
        String sql = "insert into songs (name) values(?)";
        stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, name);
        stmt.execute();
        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
        return -1;
    }

    public void prepareInsertFp(int id) throws Exception{
        conn.setAutoCommit(false);
        String sql = "insert into hashmap (hash, time, songid) values(?, ?, " + String.valueOf(id) +")";
        stmt = conn.prepareStatement(sql);
        stmt.setQueryTimeout(1);
    }

    public void insertFp(int hash, int time) throws Exception{
        stmt.setInt(1, hash);
        stmt.setInt(2, time);
        stmt.execute();
    }

    public void saveFp() throws Exception{
        conn.commit();
        stmt = null;
        conn.setAutoCommit(false);
    }

    public FpMap find() throws Exception{
        FpMap map = new FpMap();

        int i = 0,k = 0, count = fp.size() / findLimit;
        int[] rem = new int[findLimit];
        ResultSet rs;

        //如果剩下的个数小于限制的一半，则不查询剩下的
        boolean dontFindRem = false;
        if (count != 0 && rem.length < findLimit / 2) {
            dontFindRem = true;
        }

        //争取一次遍历完毕
        for (int hash : fp.keySet()) {
            if (k == count) {  //到达剩余个数处
                if (dontFindRem) break;
                rem[i] = hash;
            } else {
                stmt.setInt(i + 1, hash);
            }

            i++;
            if (i == findLimit) {
                rs = stmt.executeQuery();
                while (rs.next()) {
                    map.put(rs.getInt(1), rs.getInt(2), rs.getInt(3));
                }
                i = 0;
                k++;
            }
        }

        if (!dontFindRem) {  //寻找剩余
            for (i = 0; i < rem.length; i++) {
                stmt.setInt(i + 1, rem[i]);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt(1), rs.getInt(2), rs.getInt(3));
            }
        }

        stmt = null;
        return map;
    }

    public void prepareFind() throws Exception{
        //200个 25*8
        String sql = "SELECT hash, time, songid FROM hashmap WHERE hash IN (" +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        stmt = conn.prepareStatement(sql);
        stmt.setQueryTimeout(1);
    }

    public void pushFind(int hash) {
        fp.put(hash, true);
    }

}
