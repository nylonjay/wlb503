package com.bankscene.bes.welllinkbank.db1;

import android.content.Context;

import org.greenrobot.greendao.AbstractDao;

public class DBHelper {
    private static final String DB_NAME = "data.db";//数据库名称
    private static DBHelper instance;
    private static DBManager<Data, Long> author;
    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DBHelper() {

    }

    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public void init(Context context, String dbName) {
        mHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DBManager<Data, Long> author() {
        if (author == null) {
            author = new DBManager<Data, Long>() {
                @Override
                public AbstractDao<Data, Long> getAbstractDao() {
                    return mDaoSession.getDataDao();
                }
            };
        }
        return author;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void clear() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void close() {
        clear();
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    public static String getDataByKey(long key) {
        Object data = DBHelper.getInstance().author().load(key);
        if (data != null) {
            return ((Data) data).getData();
        }
        return "";
    }

    public static boolean insert(Data data) {
        return DBHelper.getInstance().author().insertOrReplace(data);
    }
}
