package com.beiming.uhf_test.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.beiming.uhf_test.greendao.gen.DaoMaster;
import com.beiming.uhf_test.greendao.gen.DaoSession;
import com.github.yuweiguocn.library.greendao.MigrationHelper;


/**
 * Created by htj on 2017/9/18.
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static SQLiteDatabase db;
    private static Context mContext;
    private static volatile GreenDaoManager mInstance = null;
    private GreenDaoManager(){
        if (mInstance == null) {
            //重写MySQLiteOpenHelper数据库升级，数据不丢失
            //MyApplication.getContext()上下文表示了数据库存储路径为手机内存
            //DEBUG表示是否打印debug
            MigrationHelper.DEBUG = true;
            MySQLiteOpenHelper helper = new
                    MySQLiteOpenHelper(mContext,"uhf_test.db",null);
            db = helper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static GreenDaoManager getInstance() {

        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                mInstance = new GreenDaoManager();
            }
            }
        }
        return mInstance;
    }
    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    public static void setContext(Context context) {
        mContext = context;
    }
    public  static SQLiteDatabase getDb() {
        return db;
    }
}
