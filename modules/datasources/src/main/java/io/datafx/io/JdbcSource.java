/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.io;

import io.datafx.io.converter.JdbcConverter;
import io.datafx.io.converter.JdbcDataSourceUtil;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create a connection to a relational DB and returns objects
 * @param <T> the type of the returned objects.
 */
public class JdbcSource<T> extends AbstractDataReader<T> implements WritableDataReader<T> {

    private final String sqlStatement;
    private final JdbcConverter<T> converter;
    private boolean connectionCreated;
    private ResultSet resultSet;
    private boolean updateQuery = false;
    private static final Logger LOGGER = Logger.getLogger(JdbcSource.class.getName());

    private JdbcConnectionFactory connectionFactory;

    public JdbcSource(String jdbcUrl, JdbcConverter<T> converter, String table, String... cols) {
        this(jdbcUrl, JdbcDataSourceUtil.createSelectStatement(table, cols), converter);
    }

    public JdbcSource(final String jdbcUrl, String selectStatement,
                      JdbcConverter<T> converter) {
        this(new JdbcConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(jdbcUrl);
            }
        }, selectStatement, converter);
    }

    public JdbcSource(Connection connection, JdbcConverter<T> converter, String table, String... cols) {
        this(connection, JdbcDataSourceUtil.createSelectStatement(table, cols), converter);
    }

    public JdbcSource(final Connection connection, String selectStatement,
                      JdbcConverter<T> converter) {
        this(new JdbcConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return connection;
            }
        }, selectStatement, converter);
    }

    private JdbcSource(JdbcConnectionFactory connectionFactory, String selectStatement,
                      JdbcConverter<T> converter) {
        this.connectionFactory = connectionFactory;
        this.sqlStatement = selectStatement;
        this.converter = converter;
    }

    public void setUpdateQuery(boolean b) {
        this.updateQuery = b;
    }

    private synchronized void createConnection() {
        if (connectionCreated) {
            return;
        }
        Connection connection = null;
        try {
            connection = connectionFactory.getConnection();
            Statement query;
            if (updateQuery) {
                LOGGER.info("an updatequery will be created");
                query = connection.createStatement();
            } else {
                LOGGER.info("a regularquery will be created");
                query = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
            }
            if (updateQuery) {
                query.executeUpdate(sqlStatement);
            } else {
                resultSet = query.executeQuery(sqlStatement);
                converter.initialize(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionCreated = true;
    }

    private interface JdbcConnectionFactory {
        Connection getConnection() throws SQLException;
    }

    @Override
    public void writeBack() {
        try {
            Connection connection = connectionFactory.getConnection();
            Statement query = connection.createStatement();
            query.executeUpdate(sqlStatement);
        } catch (SQLException ex) {
            Logger.getLogger(JdbcSource.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public T get() {
        // TODO: refactor this. We call this method on updates as well, while we don't need to parse a result on updates.
        if (!connectionCreated) {
            createConnection();
        }
        if (converter != null) {
            return converter.get();
        } else {
            return null;
        }
    }

    @Override
    public boolean next() {
        if (!connectionCreated) {
            createConnection();
        }
        boolean answer = converter.next();
        return answer;
    }
}
