package com.homer.util.data;

import com.homer.util.HomerBeanUtil;
import com.homer.util.core.*;
import com.homer.util.core.data.IRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by arigolub on 3/19/16.
 */
public abstract class BaseVersionedRepository<T extends IBaseObject, H extends IHistoryObject>
        extends BaseRepository<T> implements IRepository<T> {

    private Class<H> historyClass;

    public BaseVersionedRepository(Class<T> clazz, Class<H> historyClazz) {
        super(clazz);
        this.historyClass = historyClazz;
    }

    @Override
    public T upsert(T obj) {
        obj = super.upsert(obj);
        if(obj.getId() > 0) {
            Statement stmt = null;
            Connection con = null;
            H history = null;
            try {
                history = historyClass.newInstance();
                HomerBeanUtil.copyProperties(history, obj);
                history.setIsDeleted(false);
                history.setHistoryCreatedDateUTC(DateTime.now(DateTimeZone.UTC));
                String query = buildUpsert(historyClass, $.of(history).toList());
                logger.debug(query);
                con = Connector.getConnection();
                stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.commit();
            } catch (Exception e ) {
                e.printStackTrace();
                if (con != null) {
                    try {
                        con.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        throw new RuntimeException(e1);
                    }
                }
                throw new RuntimeException(e);
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }
}
