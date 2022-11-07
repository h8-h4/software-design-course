package ru.akirakozov.sd.refactoring.dao;

import lombok.experimental.UtilityClass;
import ru.akirakozov.sd.refactoring.util.ThrowingConsumer;
import ru.akirakozov.sd.refactoring.util.ThrowingFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class DatabaseUtils {
    public static int executeUpdate(String sql, ThrowingConsumer<PreparedStatement, SQLException> executeQuery) {
        try (
                Connection connection = DatabaseConnector.newConnection();
                PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            executeQuery.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T executeConstantQuery(
            String sql,
            ThrowingFunction<ResultSet, T, SQLException> resultSetMapper
    ) {
        try (
                Connection connection = DatabaseConnector.newConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
        ) {
            return resultSetMapper.apply(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
