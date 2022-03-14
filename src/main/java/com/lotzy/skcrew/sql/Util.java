package com.lotzy.skcrew.sql;

import ch.njol.skript.lang.Variable;
import ch.njol.skript.variables.Variables;
import java.sql.Connection;
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

public class Util {
    private static RowSetFactory rowSetFactory;
    static {
        try {
            rowSetFactory = RowSetProvider.newFactory();
        } catch (SQLException ex) {

        }
    }
    public static RowSetFactory getRowSetFactory() {
        return rowSetFactory;
    }    
    
    static public void setVariable(Event e, String name, Object obj, Boolean isLocal) {
        Variables.setVariable(name.toLowerCase(Locale.ENGLISH), obj, e, isLocal);
    }
    
    static public Object executeStatement(DataSource ds, String baseVariable, String query, Boolean isList) {
        if (ds == null) {
            return "Data source is not set";
        }
        Map<String, Object> variableList = new HashMap<>();
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            boolean hasResultSet = stmt.execute(query);

            if (baseVariable != null) {
                if (isList) {
                    baseVariable = baseVariable.substring(0, baseVariable.length() - 1);
                }

                if (hasResultSet) {
                    CachedRowSet crs = getRowSetFactory().createCachedRowSet();
                    crs.populate(stmt.getResultSet());

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
                    //if no results are returned and the specified variable isn't a list variable, put the affected rows count in the variable
                    variableList.put(baseVariable, stmt.getUpdateCount());
                }
            }
        } catch (SQLException ex) {
            return ex.getMessage();
        }
        return variableList;
    }
}
