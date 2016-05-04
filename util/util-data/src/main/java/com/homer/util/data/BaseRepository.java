package com.homer.util.data;

import com.homer.util.EnumUtil;
import com.homer.util.core.$;
import com.homer.util.core.IBaseObject;
import com.homer.util.core.IDated;
import com.homer.util.core.IIntEnum;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.Matcher;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

/**
 * Created by arigolub on 3/13/16.
 */
public abstract class BaseRepository<T extends IBaseObject> implements IRepository<T> {

    private final Class<T> clazz;
    protected final DateTimeFormatter dateTimeFormatter;

    public BaseRepository(Class<T> clazz) {
        this.clazz = clazz;
        dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
    }

    @Override
    public List<T> getMany(Map<String, ?> filters) {
        List<T> results = null;
        Statement stmt = null;
        Connection con = null;
        try {
            String query = buildQuery(clazz, filters);
            System.out.println(query);
            con = Connector.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            con.commit();
            results = hydrate(clazz, rs);
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
        return results;
    }

    @Override
    public T upsert(T obj) {
        Statement stmt = null;
        Connection con = null;
        try {
            String query = buildUpsert(clazz, $.of(obj).toList());
            System.out.println(query);
            con = Connector.getConnection();
            stmt = con.createStatement();
            int rowCount = stmt.executeUpdate(query);
            if (rowCount > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getLong(1));
                }
            }
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
        return obj;
    }

    @Override
    public boolean delete(long id) {
        String query = buildDelete(clazz, id);

        boolean deleted = false;
        Statement stmt = null;
        Connection con = null;
        try {
            System.out.println(query);
            con = Connector.getConnection();
            stmt = con.createStatement();
            stmt.execute(query);
            con.commit();
            deleted = true;
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
        return deleted;
    }

    //region helpers

    private static String join(List objs, boolean addQuotes)
    {
        StringBuilder sb = new StringBuilder("");
        for(int i = 0; i < objs.size(); i++)
        {
            sb.append(" ");
            if (addQuotes) {
                sb.append("'");
            }
            Object obj = objs.get(i);
            if (obj instanceof String) {
                sb.append(cleanString((String) obj));
            } else {
                sb.append(objs.get(i));
            }
            if (addQuotes) {
                sb.append("'");
            }
            sb.append(" ");
            if (i < objs.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private String buildDelete(Class<T> clazz, long id)
    {
        String tableName = clazz.getAnnotation(Table.class).name();
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(tableName).append(" WHERE id = ").append(id).append(";");
        return query.toString();
    }

    private String buildQuery(Class<T> clazz, Map<String, ?> filters)
    {
        Table annotation = clazz.getAnnotation(Table.class);
        String tableName = annotation.name();
        String schema = annotation.schema();
        StringBuilder query = new StringBuilder("SELECT ");
        List<String> columns = ColumnUtil.getColumns(clazz);
        query.append(join(columns, false));
        query.append("\n FROM ").append(schema).append(".").append(tableName);
        if (filters.size() > 0) {
            query.append("\n WHERE ");
            Iterator<String> iterator = filters.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (List.class.isAssignableFrom(filters.get(key).getClass())) {
                    query.append(key);
                    List objects = (List) filters.get(key);
                    if (objects.size() == 0) {
                        query.append(" = '").append(filters.get(key)).append("'");
                    } else {
                        query.append(" in ").append("(");
                        query.append(join(objects, true));
                        query.append(")");
                    }
                } else if (Matcher.class.isAssignableFrom(filters.get(key).getClass())) {
                    Matcher matcher = (Matcher) filters.get(key);
                    query.append(matcher.getField()).append(" like '%").append(matcher.getMatch()).append("%'");
                } else if (IIntEnum.class.isAssignableFrom(filters.get(key).getClass())) {
                    query.append(key);
                    query.append(" = ").append(EnumUtil.to((IIntEnum) filters.get(key)));
                } else if (String.class.isAssignableFrom(filters.get(key).getClass())) {
                    query.append(key);
                    query.append(" = '").append(cleanString((String) filters.get(key))).append("'");
                } else if (Boolean.class.isAssignableFrom(filters.get(key).getClass())) {
                    query.append(key);
                    query.append(" = ").append(((Boolean)filters.get(key)) ? 1 : 0);
                } else {
                    query.append(key);
                    query.append(" = '").append(filters.get(key)).append("'");
                }
                if (iterator.hasNext()) {
                    query.append("\n AND ");
                }
            }
        }
        query.append(";");
        return query.toString();
    }

    protected <K extends IDated> String buildUpsert(Class<K> clazz, Collection<K> baseObjects) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        StringBuilder query = new StringBuilder();
        Table annotation = clazz.getAnnotation(Table.class);
        String tableName = annotation.name();
        String schema = annotation.schema();
        for(K object : baseObjects) {
            query.append(buildUpsertSingle(clazz, object, schema, tableName));
        }
        return query.toString();
    }

    private <K extends IDated> String buildUpsertSingle(Class<K> clazz, K object, String schema, String tableName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        if (object.getCreatedDateUTC() == null) {
            object.setCreatedDateUTC(DateTime.now(DateTimeZone.UTC));
        }
        object.setUpdatedDateUTC(DateTime.now(DateTimeZone.UTC));
        query.append(schema).append(".").append(tableName).append(" (");
        List<String> columns = ColumnUtil.getColumns(clazz);
        List<Field> fields = ColumnUtil.getFields(clazz);
        query.append(join(columns, false));
        query.append(")").append(" VALUES ").append(" ( ");
        for(int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            Object obj = PropertyUtils.getProperty(object, f.getName());
            if (obj == null) {
                query.append("null");
            } else if(String.class.isAssignableFrom(obj.getClass())) {
                query.append("'").append(cleanString(obj.toString())).append("'");
            } else if (DateTime.class.isAssignableFrom(obj.getClass())) {
                query.append("'").append(dateTimeToString((DateTime)obj)).append("'");
            } else if ("boolean".equals(obj.getClass().getName())) {
                query.append(((boolean) obj) == true ? 1 : 0);
            } else if (obj instanceof IIntEnum) {
                query.append(EnumUtil.to((IIntEnum)obj));
            } else {
                query.append(obj.toString());
            }
            if (i < fields.size() - 1) {
                query.append(" , ");
            }
        }
        query.append(" ) ");
        query.append(" ON DUPLICATE KEY UPDATE ");
        fields = ColumnUtil.getUpdateFields(clazz);
        for(int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            String columnName  = f.getName();
            Object obj = PropertyUtils.getProperty(object, f.getName());
            if (obj == null) {
                query.append(columnName).append(" = ").append("null");
            } else if(String.class.isAssignableFrom(obj.getClass())) {
                query.append(columnName).append(" = ").append("'").append(cleanString(obj.toString())).append("'");
            } else if (DateTime.class.isAssignableFrom(obj.getClass())) {
                query.append(columnName).append(" = ").append("'").append(dateTimeToString((DateTime)obj)).append("'");
            } else if ("boolean".equals(obj.getClass().getName())) {
                query.append(columnName).append(" = ").append(((boolean)obj) == true ? 1 : 0);
            } else if (obj instanceof IIntEnum) {
                query.append(columnName).append(" = ").append(EnumUtil.to((IIntEnum)obj));
            } else {
                query.append(columnName).append(" = ").append(obj.toString());
            }
            if (i < fields.size() - 1) {
                query.append(" , ");
            }
        }
        query.append(";");
        return query.toString();
    }

    private String dateTimeToString(DateTime dateTime) {
        return dateTime.withMillisOfSecond(0).toString(dateTimeFormatter);
    }

    private List<T> hydrate(Class<T> baseObject, ResultSet rs) throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (rs == null) {
            throw new SQLException("ResultSet was null");
        }
        List<T> results = new ArrayList<>();
        List<Field> fields = ColumnUtil.getFields(baseObject);
        while(rs.next()) {
            T obj = baseObject.newInstance();
            for(Field f : fields) {
                String fieldName = f.getName();
                if (f.getType().isPrimitive()) {
                    if("long".equals(f.getType().getName())) {
                        BeanUtils.setProperty(obj, fieldName, rs.getLong(fieldName));
                    } else if ("int".equals(f.getType().getName())) {
                        BeanUtils.setProperty(obj, fieldName, rs.getInt(fieldName));
                    } else if ("boolean".equals(f.getType().getName())) {
                        BeanUtils.setProperty(obj, fieldName, rs.getBoolean(fieldName));
                    }
                } else if (Integer.class.equals(f.getType())) {
                    Integer o = rs.getInt(fieldName);
                    if (rs.wasNull()) {
                        o = null;
                    }
                    PropertyUtils.setProperty(obj, fieldName, o);
                } else if (Long.class.equals(f.getType())) {
                    Long o = rs.getLong(fieldName);
                    if(rs.wasNull()) {
                        o = null;
                    }
                    PropertyUtils.setProperty(obj, fieldName, o);
                } else if (String.class.equals(f.getType())) {
                    BeanUtils.setProperty(obj, fieldName, rs.getString(fieldName));
                } else if (DateTime.class.equals(f.getType())) {
                    Timestamp timestamp = rs.getTimestamp(fieldName);
                    BeanUtils.setProperty(obj, fieldName, new DateTime(timestamp).withMillisOfSecond(0));
                } else if (IIntEnum.class.isAssignableFrom(f.getType())) {
                    Class<IIntEnum> clazz = (Class<IIntEnum>) f.getType();
                    BeanUtils.setProperty(obj, fieldName, EnumUtil.fromNotParameterized(clazz, rs.getInt(fieldName)));
                }
            }
            results.add(obj);
        }
        return results;
    }

    private static String cleanString(String input) {
        return input.replace("'", "\\'");
    }

    //endregion
}
