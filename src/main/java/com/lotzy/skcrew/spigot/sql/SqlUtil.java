package com.lotzy.skcrew.spigot.sql;

import ch.njol.skript.lang.Variable;
import ch.njol.skript.variables.Variables;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import org.bukkit.event.Event;

public class SqlUtil {
    private static RowSetFactory rowSetFactory;
    static {
        try {
            rowSetFactory = RowSetProvider.newFactory();
        } catch (SQLException ex) {}
    }
    
    public static RowSetFactory getRowSetFactory() {
        return rowSetFactory;
    }    
    
    static public void setVariable(Event e, String name, Object obj, Boolean isLocal) {
        Variables.setVariable(name.toLowerCase(Locale.ENGLISH), obj, e, isLocal);
    }
    
    static public Object executeStatement(DataSource ds, String baseVariable, String query, Boolean isList,  Object[] params) {
        if (ds == null) return "Data source is not set";
        Map<String, Object> variableList = new HashMap<>();
        try (Connection conn = ds.getConnection()) {
            ResultSet resultSet;
            int updateCount;
            if (params==null || params.length == 0) {
                Statement stmt = conn.createStatement();
                resultSet = stmt.execute(query) ? stmt.getResultSet() : null;
                updateCount = stmt.getUpdateCount();
            } else {
                PreparedStatement pstmt = conn.prepareStatement(query);
                for (int i = 0; i < params.length; i++)
                    pstmt.setObject(i+1, params[i]);
                resultSet = pstmt.execute() ? pstmt.getResultSet() : null;
                updateCount = pstmt.getUpdateCount();
            }
            if (baseVariable != null) {
                if (isList) {
                    baseVariable = baseVariable.substring(0, baseVariable.length() - 1);
                }

                if (resultSet!=null) {
                    CachedRowSet crs = getRowSetFactory().createCachedRowSet();
                    crs.populate(resultSet);

                    if (isList) {
                        ResultSetMetaData meta = crs.getMetaData();
                        int columnCount = meta.getColumnCount();

                        for (int i = 1; i <= columnCount; i++) {
                            String label = meta.getColumnLabel(i);
                            variableList.put(baseVariable + label, label);
                        }

                        int rowNumber = 1;
                        try {
                            while (crs.next()) {
                                for (int i = 1; i <= columnCount; i++) {
                                    variableList.put(baseVariable + meta.getColumnLabel(i).toLowerCase(Locale.ENGLISH)
                                            + Variable.SEPARATOR + rowNumber, crs.getObject(i));
                                }
                                rowNumber++;
                            }
                        } catch (SQLException ex) {
                            return ex.getMessage();
                        }
                    } else {
                        crs.last();
                        variableList.put(baseVariable, crs.getRow());
                    }
                } else if (!isList) {
                    variableList.put(baseVariable, updateCount);
                }
            }
        } catch (SQLException ex) {
            return ex.getMessage();
        }
        return variableList;
    }
}
