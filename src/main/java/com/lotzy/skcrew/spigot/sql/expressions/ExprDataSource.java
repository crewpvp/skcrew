package com.lotzy.skcrew.spigot.sql.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.event.Event;
import java.util.HashMap;
import java.util.Map;

@Name("SQL - Database")
@Description("Return database for execute statements")
@Examples({"on load:",
        "\tset {database} to database \"mysql://IP:3306/DATABASE?user=USERNAME&password=PASSWORD&useSSL=false\""})
@Since("1.0")
public class ExprDataSource extends SimpleExpression<HikariDataSource> {
    private static final Map<String, HikariDataSource> connectionCache = new HashMap<>();

    static {
        Skript.registerExpression(ExprDataSource.class, HikariDataSource.class,
                ExpressionType.COMBINED, "[the] data(base|[ ]source) [(of|at)] %string% " +
                        "[with [a] [max[imum]] [connection] life[ ]time of %-timespan%] [(with|and) driver [class] [name] %-string%]");
    }

    private Expression<String> url;
    private Expression<Timespan> maxLifetime;
    private Expression<String> driverClassName;

    @Override
    protected HikariDataSource[] get(Event e) {
        String jdbcUrl = url.getSingle(e);
        if (jdbcUrl == null) {
            return null;
        }
        String rawUrl = jdbcUrl.toLowerCase();
        if (!jdbcUrl.toLowerCase().startsWith("jdbc:")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        } else {
            rawUrl = rawUrl.substring(5);
        }

        if (connectionCache.containsKey(jdbcUrl)) {
            return new HikariDataSource[]{connectionCache.get(jdbcUrl)};
        }

        HikariDataSource ds = new HikariDataSource();
        
        
        //allow specifying of own sql driver class name
        if (driverClassName != null)
            ds.setDriverClassName(driverClassName.getSingle(e));
        else {
            //auto proceed jdbc driver
            if (rawUrl.startsWith("postgresql")) {
                ds.setDriverClassName("org.postgresql.Driver");
            } else if (rawUrl.startsWith("mariadb")) {
                ds.setDriverClassName("org.mariadb.jdbc.Driver");
            } else if (rawUrl.startsWith("mysql")) {
                ds.setDriverClassName("com.mysql.cj.jdbc");
            } else if (rawUrl.startsWith("sqlite")) {
                ds.setDriverClassName("org.sqlite.JDBC");
            } else if (rawUrl.startsWith("sqlite")) {
                ds.setDriverClassName("org.sqlite.JDBC");
            } else if (rawUrl.startsWith("sqlserver")) {
                ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } else if (rawUrl.startsWith("oracle")) {
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
                } catch (ClassNotFoundException ex) {
                    ds.setDriverClassName("oracle.jdbc.OracleDriver");
                }
            }
        }
        ds.setJdbcUrl(jdbcUrl);

        if (maxLifetime != null) {
            Timespan l = maxLifetime.getSingle(e);

            if (l != null) {
                ds.setMaxLifetime(l.getMilliSeconds());
            }
        }

        connectionCache.put(jdbcUrl, ds);

        return new HikariDataSource[]{ds};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends HikariDataSource> getReturnType() {
        return HikariDataSource.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Datasource: " + url.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        url = (Expression<String>) exprs[0];
        maxLifetime = (Expression<Timespan>) exprs[1];
        return true;
    }
}
