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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.io.converter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.datafx.io.Converter;

/**
 * A Converter implementation that translates a {@link java.sql.ResultSet} into
 * objects of type T. The {@link #convertOneRow(java.sql.ResultSet)} method
 * should be implemented by consumers of this class.
 *
 * @param <T> The type of the converted objects.
 */
public abstract class JdbcConverter<T> implements Converter<ResultSet, T> {

    protected ResultSet resultSet;
    private boolean hasNext = true;

    @Override
    public void initialize(ResultSet input) throws IOException {
        this.resultSet = input;
        try {
            hasNext = resultSet.next();
        } catch (SQLException ex) {
            throw new IOException("Can't initialize Jdbc resultset", ex);
        }
    }

    @Override
    public T get() {
        T entry = convertOneRow(resultSet);
        try {
            hasNext = resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // TODO we rather should throw an exception than returning null here
        }
        return entry;
    }

    /**
     * Implementation-specific conversion between a resultSet and a java object
     * of type T.
     *
     * @param resultSet the resultset, obtained by the JDBC call.
     * @return the converted object of type T
     */
    public abstract T convertOneRow(ResultSet resultSet);

    @Override
    public boolean next() {
        try {
            int type = resultSet.getType();
            if (type == ResultSet.TYPE_FORWARD_ONLY) {
                return hasNext;
            } else {
                return !resultSet.isAfterLast();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
