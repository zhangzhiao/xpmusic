package com.xp.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Enzo Cotter on 2020-11-10.
 */
public class LoveMusicManager  {
    private  DaoMaster.DevOpenHelper helper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;
    /**
     * DaoSession
     */
    private DaoSession mDaoSession;
    /**
     * 上下文
     */
    private Context context;
    /**
     * dao
     */
    LoveMusicDao dao;
    public static LoveMusicManager loveMusicManager;

    public LoveMusicManager(Context context) {
        this.context = context;
        helper = new DaoMaster.DevOpenHelper(context,"person.db", null);
        mDaoMaster =new DaoMaster(getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        dao = mDaoSession.getLoveMusicDao();
    }

    /**
     * 获取单例
     */
    public static LoveMusicManager getInstance(Context context){
        if(loveMusicManager == null){
            synchronized (LoveMusicManager.class){
                if(loveMusicManager == null){
                    loveMusicManager = new LoveMusicManager(context);
                }
            }
        }
        return loveMusicManager;
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase(){
        if(helper == null){
            helper = new DaoMaster.DevOpenHelper(context,"person.db",null);
        }
        return helper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     * @return
     */
    private SQLiteDatabase getWritableDatabase(){
        if(helper == null){
            helper =new DaoMaster.DevOpenHelper(context,"person.db",null);
        }
        return helper.getWritableDatabase();
    }
    public List<LoveMusic> selectByName(String name){
        return  dao.queryBuilder().where(LoveMusicDao.Properties.Name.eq(name)).list();
    }
    public void insertOrReplace(LoveMusic loveMusic){
        dao.insertOrReplace(loveMusic);
    }
    public long insert(LoveMusic loveMusic){
      return   dao.insert(loveMusic);
    }
    public List<LoveMusic> searchAll(){
        return dao.queryBuilder().list();
    }
    public void delete(String name){
        dao.queryBuilder().where(LoveMusicDao.Properties.Name.eq(name)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

}
