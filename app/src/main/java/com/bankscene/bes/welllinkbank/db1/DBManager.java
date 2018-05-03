package com.bankscene.bes.welllinkbank.db1;

import android.database.sqlite.SQLiteException;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collection;
import java.util.List;

public abstract class DBManager<M, K> {


    public boolean insert(@NotNull M m) {
        try {
            getAbstractDao().insert(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean insertOrReplace(@NotNull M m) {
        try {
            getAbstractDao().insertOrReplace(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean insertInTx(@NotNull List<M> list) {
        try {
            getAbstractDao().insertInTx(list);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean insertOrReplaceInTx(@NotNull List<M> list) {
        try {
            getAbstractDao().insertOrReplaceInTx(list);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean delete(@NotNull M m) {
        try {
            getAbstractDao().delete(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean deleteByKey(@NotNull K key) {
        try {
            getAbstractDao().deleteByKey(key);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean deleteInTx(@NotNull List<M> list) {
        try {
            getAbstractDao().deleteInTx(list);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SafeVarargs
    public final boolean deleteByKeyInTx(@NotNull K... key) {
        try {
            getAbstractDao().deleteByKeyInTx(key);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean deleteAll() {
        try {
            getAbstractDao().deleteAll();
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean update(@NotNull M m) {
        try {
            getAbstractDao().update(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @SafeVarargs
    public final boolean updateInTx(@NotNull M... m) {
        try {
            getAbstractDao().updateInTx(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean updateInTx(@NotNull List<M> list) {
        try {
            getAbstractDao().updateInTx(list);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public M load(@NotNull K key) {
        try {
            return getAbstractDao().load(key);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<M> loadAll() {
        return getAbstractDao().loadAll();
    }


    public boolean refresh(@NotNull M m) {
        try {
            getAbstractDao().refresh(m);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void runInTx(@NotNull Runnable runnable) {
        try {
            getAbstractDao().getSession().runInTx(runnable);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }


    public QueryBuilder<M> queryBuilder() {
        return getAbstractDao().queryBuilder();
    }


    public List<M> queryRaw(@NotNull String where, @NotNull String... selectionArg) {
        return getAbstractDao().queryRaw(where, selectionArg);
    }


    public Query<M> queryRawCreate(@NotNull String where, @NotNull Object... selectionArg) {
        return getAbstractDao().queryRawCreate(where, selectionArg);
    }


    public Query<M> queryRawCreateListArgs(@NotNull String where, @NotNull Collection<Object> selectionArg) {
        return getAbstractDao().queryRawCreateListArgs(where, selectionArg);
    }


    public abstract AbstractDao<M, K> getAbstractDao();

}
