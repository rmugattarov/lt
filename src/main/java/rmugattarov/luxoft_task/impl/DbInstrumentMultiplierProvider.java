package rmugattarov.luxoft_task.impl;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rmugattarov.luxoft_task.constants.DbConstants;
import rmugattarov.luxoft_task.dto.Multiplier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by rmugattarov on 16.06.2016.
 */
public class DbInstrumentMultiplierProvider {
    private final static String MULTIPLIER_QUERY = "SELECT " + DbConstants.MULTIPLIER_COL + " FROM " + DbConstants.MULTIPLIER_TABLE + " WHERE " + DbConstants.NAME_COL + "=?";
    private static LoadingCache<String, Multiplier> multiplierCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Multiplier>() {
                private PreparedStatement stmt;

                @Override
                public Multiplier load(String instrumentId) throws Exception {
                    Multiplier result = Multiplier.NO_MULTIPLIER;
                    if (!Strings.isNullOrEmpty(instrumentId)) {
                        if (stmt == null) {
                            Connection connection = DriverManager.getConnection(DbConstants.CONN_URL);
                            stmt = connection.prepareStatement(MULTIPLIER_QUERY);
                        }
                        stmt.setString(1, instrumentId);
                        ResultSet resultSet = stmt.executeQuery();
                        if (resultSet.next()) {
                            result = new Multiplier(resultSet.getDouble(DbConstants.MULTIPLIER_COL));
                        }
                    }
                    System.out.printf("%s multiplier : %s\r\n", instrumentId, result);
                    return result;
                }
            });

    public static Multiplier getInstrumentMultiplier(String instrumentId) {
        try {
            return multiplierCache.get(instrumentId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Multiplier.NO_MULTIPLIER;
    }
}
