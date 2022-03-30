package com.lotzy.skcrew.url;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

@Name("URL - Response")
@Description("Return html text from address")
@Examples({"on load:",
        "\tbroadcast \"%response from url \"https://google.com\"%\""})
@Since("1.0")
public class ExprResponseFromUrl extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprResponseFromUrl.class, String.class,
                ExpressionType.COMBINED, "[the] response (from|of) (url|[web]site|link) %string%");
    }
    private Expression<String> expr;
    
    @Nullable 
    @Override
    protected String[] get(Event e) {
        try {
            return new String[] {GETRequest(expr.getSingle(e))};
        } catch (IOException ex) {
            Logger.getLogger(ExprResponseFromUrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Nullable
    private static String GETRequest(String url) throws IOException {
        
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                     BufferedReader in = new BufferedReader(new InputStreamReader(
                                     con.getInputStream()));
                     String inputLine;
                     StringBuffer response = new StringBuffer();

                     while ((inputLine = in.readLine()) != null) {
                             response.append(inputLine);
                     }
                     in.close();
                     return response.toString();

            } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
            }
            
        } catch (UnknownHostException e) {
            con.disconnect();
            Skript.warning("Host '"+url+"' doesn't responding");
        }
        return null;
        
    }
    
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(final @Nullable Event e, boolean debug) {
        return "Responce from url";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        expr = (Expression<String>) exprs[0];
        return true;
    }
}
