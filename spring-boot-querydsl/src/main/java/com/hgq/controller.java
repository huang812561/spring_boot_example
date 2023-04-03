package com.hgq;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLQuery;

/**
 * todo
 *
 * @Author hgq
 * @Date: 2022-07-29 17:46
 * @since 1.0
 **/
public class controller {

    public static void test() {

        /*Map<String, SQLTemplates> sqlTemplatesMap = new LinkedHashMap<>();
        sqlTemplatesMap.put("mysql", MySQLTemplates.DEFAULT);
        sqlTemplatesMap.put("oracle", OracleTemplates.DEFAULT);
        sqlTemplatesMap.put("sqlserver", SQLServer2008Templates.DEFAULT);
        sqlTemplatesMap.put("postgresql", PostgreSQLTemplates.DEFAULT);
        sqlTemplatesMap.put("h2", H2Templates.DEFAULT);
        sqlTemplatesMap.put("db2", DB2Templates.DEFAULT);
        for(Map.Entry<String,SQLTemplates> entry:sqlTemplatesMap.entrySet()){
            final SQLQuery<Object> sqlQuery = new SQLQuery<>(entry.getValue());
            PathBuilder pathBuilder = new PathBuilder(Object.class, "person");
            sqlQuery.select(pathBuilder.get("name"))
                    .from(pathBuilder.getRoot())
                    .where(pathBuilder.get("idnumber")
                            .in("a","b","c")).offset(5).limit(10);
            System.out.println("name:" + entry.getKey());
            final SQLBindings bindings = sqlQuery.getSQL();
            System.out.println(bindings.getSQL());
            System.out.println(bindings.getNullFriendlyBindings());
            System.out.println("====================\r\n");
        }*/
        SQLQuery sqlQuery = new SQLQuery(MySQLTemplates.DEFAULT);
        PathBuilder pathBuilder = new PathBuilder(Object.class, "person");
        sqlQuery.select(pathBuilder.get("name"))
                .from(pathBuilder.getRoot())
                .where(pathBuilder.get("idnumber").in("a", "b", "c")).offset(5).limit(10);
        final SQLBindings bindings = sqlQuery.getSQL();
        System.out.println(bindings.getSQL());
        System.out.println(bindings.getNullFriendlyBindings());
        System.out.println("====================\r\n");
    }

    public static void main(String[] args) {
        test();
    }
}
