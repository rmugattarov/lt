package rmugattarov.luxoft_task.impl;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import rmugattarov.luxoft_task.constants.DbConstants;

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
    private static LoadingCache<String, Double> multiplierCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Double>() {
                @Override
                public Double load(String instrumentId) throws Exception {
                    Double result = null;
                    if (!Strings.isNullOrEmpty(instrumentId)) {
                        Connection connection = DriverManager.getConnection(DbConstants.CONN_URL);
                        PreparedStatement stmt = connection.prepareStatement(MULTIPLIER_QUERY);
                        stmt.setString(1, instrumentId);
                        ResultSet resultSet = stmt.executeQuery();
                        if (resultSet.next()) {
                            result = resultSet.getDouble(DbConstants.MULTIPLIER_COL);
                        }
                    }
                    return result == null ? 1.0 : result;
                }
            });

    public static double getInstrumentMultiplier(String instrumentId) {
        try {
            return multiplierCache.get(instrumentId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 1.0;
    }
}
