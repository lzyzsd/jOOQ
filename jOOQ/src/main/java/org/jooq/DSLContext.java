/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.impl.DSL;

/**
 * A contextual DSL providing "attached" implementations to the
 * <code>org.jooq</code> interfaces.
 * <p>
 * Apart from the {@link DSL}, this contextual DSL is the main entry point
 * for client code, to access jOOQ classes and functionality that are related to
 * {@link Query} execution. Unlike objects created through the
 * <code>Factory</code>, objects created from a <code>DSLContext</code> will be
 * "attached" to the <code>DSLContext</code>'s {@link #configuration()}, such
 * that they can be executed immediately in a fluent style. An example is given
 * here:
 * <p>
 * <code><pre>
 * DSLContext create = Factory.using(connection, dialect);
 *
 * // Immediately fetch results after constructing a query
 * create.selectFrom(MY_TABLE).where(MY_TABLE.ID.eq(1)).fetch();
 *
 * // The above is equivalent to this "non-fluent" style
 * create.fetch(Factory.selectFrom(MY_TABLE).where(MY_TABLE.ID.eq(1)));
 * </pre></code>
 * <p>
 * The <code>Factory</code> provides convenient constructors to create a
 * {@link Configuration}, which will be shared among all <code>Query</code>
 * objects thus created. Optionally, you can pass a reusable
 * <code>Configuration</code> to the {@link DSL#using(Configuration)}
 * constructor. Please consider thread-safety concerns documented in
 * {@link Configuration}, should you want to reuse the same
 * <code>Configuration</code> instance in various threads and / or transactions.
 *
 * @see DSL
 * @see Configuration
 * @author Lukas Eder
 */
public interface DSLContext {

    // -------------------------------------------------------------------------
    // XXX Configuration API
    // -------------------------------------------------------------------------

    /**
     * The <code>Configuration</code> referenced from this
     * <code>DSLContext</code>.
     */
    Configuration configuration();

    /**
     * Map a schema to another one.
     * <p>
     * This will map a schema onto another one, depending on configured schema
     * mapping in this <code>Executor</code>. If no applicable schema mapping
     * can be found, the schema itself is returned.
     *
     * @param schema A schema
     * @return The mapped schema
     */
    Schema map(Schema schema);

    /**
     * Map a table to another one.
     * <p>
     * This will map a table onto another one, depending on configured table
     * mapping in this <code>Executor</code>. If no applicable table mapping can
     * be found, the table itself is returned.
     *
     * @param table A table
     * @return The mapped table
     */
    <R extends Record> Table<R> map(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX Convenience methods accessing the underlying Connection
    // -------------------------------------------------------------------------

    /**
     * Access the database meta data.
     * <p>
     * This method returns a wrapper type that gives access to your JDBC
     * connection's database meta data.
     */
    Meta meta();

    // -------------------------------------------------------------------------
    // XXX RenderContext and BindContext accessors
    // -------------------------------------------------------------------------

    /**
     * Get a new {@link RenderContext} for the context of this executor.
     * <p>
     * This will return an initialised render context as such:
     * <ul>
     * <li>
     * <code>{@link RenderContext#castMode()} == {@link org.jooq.RenderContext.CastMode#DEFAULT DEFAULT}</code>
     * </li>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * <li> <code>{@link RenderContext#format()} == false</code></li>
     * <li> <code>{@link RenderContext#inline()} == false</code></li>
     * <li> <code>{@link RenderContext#namedParams()} == false</code></li>
     * <li> <code>{@link RenderContext#qualify()} == true</code></li>
     * <li> <code>{@link RenderContext#subquery()} == false</code></li>
     * </ul>
     */
    RenderContext renderContext();

    /**
     * Render a QueryPart in the context of this executor.
     * <p>
     * This is the same as calling <code>renderContext().render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    String render(QueryPart part);

    /**
     * Render a QueryPart in the context of this executor, rendering bind
     * variables as named parameters.
     * <p>
     * This is the same as calling
     * <code>renderContext().namedParams(true).render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    String renderNamedParams(QueryPart part);

    /**
     * Render a QueryPart in the context of this executor, inlining all bind
     * variables.
     * <p>
     * This is the same as calling
     * <code>renderContext().inline(true).render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    String renderInlined(QueryPart part);

    /**
     * Retrieve the bind values that will be bound by a given
     * <code>QueryPart</code>.
     * <p>
     * The returned <code>List</code> is immutable. To modify bind values, use
     * {@link #extractParams(QueryPart)} instead.
     */
    List<Object> extractBindValues(QueryPart part);

    /**
     * Get a <code>Map</code> of named parameters.
     * <p>
     * The <code>Map</code> itself is immutable, but the {@link Param} elements
     * allow for modifying bind values on an existing {@link Query} (or any
     * other {@link QueryPart}).
     * <p>
     * Bind values created with {@link DSL#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see DSL#param(String, Object)
     */
    Map<String, Param<?>> extractParams(QueryPart part);

    /**
     * Get a named parameter from a {@link QueryPart}, provided its name.
     * <p>
     * Bind values created with {@link DSL#val(Object)} will have their bind
     * index as name.
     *
     * @see Param
     * @see DSL#param(String, Object)
     */
    Param<?> extractParam(QueryPart part, String name);

    /**
     * Get a new {@link BindContext} for the context of this executor.
     * <p>
     * This will return an initialised bind context as such:
     * <ul>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * </ul>
     * <p>
     * RenderContext for JOOQ INTERNAL USE only. Avoid referencing it directly
     */
    BindContext bindContext(PreparedStatement stmt);

    /**
     * Get a new {@link BindContext} for the context of this executor.
     * <p>
     * This will return an initialised bind context as such:
     * <ul>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * </ul>
     * <p>
     * RenderContext for JOOQ INTERNAL USE only. Avoid referencing it directly
     */
    int bind(QueryPart part, PreparedStatement stmt);

    // -------------------------------------------------------------------------
    // XXX Attachable and Serializable API
    // -------------------------------------------------------------------------

    /**
     * Attach this <code>Executor</code> to some attachables.
     */
    void attach(Attachable... attachables);

    /**
     * Attach this <code>Executor</code> to some attachables.
     */
    void attach(Collection<? extends Attachable> attachables);

    // -------------------------------------------------------------------------
    // XXX Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * Create a new <code>Loader</code> object to load data from a CSV or XML
     * source.
     */
    @Support
    <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table);

    /**
     * Create a new query holding plain SQL. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "SET SCHEMA 'abc'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query wrapping the plain SQL
     */
    @Support
    Query query(String sql);

    /**
     * Create a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "SET SCHEMA 'abc'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A query wrapping the plain SQL
     */
    @Support
    Query query(String sql, Object... bindings);

    /**
     * Create a new query holding plain SQL.
     * <p>
     * Unlike {@link #query(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * query("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will render this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query wrapping the plain SQL
     */
    @Support
    Query query(String sql, QueryPart... parts);

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(String sql) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(String sql, Object... bindings);

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Unlike {@link #fetch(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetch("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(String sql, QueryPart... parts);

    /**
     * Execute a new query holding plain SQL and "lazily" return the generated
     * result.
     * <p>
     * The returned {@link Cursor} holds a reference to the executed
     * {@link PreparedStatement} and the associated {@link ResultSet}. Data can
     * be fetched (or iterated over) lazily, fetching records from the
     * {@link ResultSet} one by one.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(String sql) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL and "lazily" return the generated
     * result.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * The returned {@link Cursor} holds a reference to the executed
     * {@link PreparedStatement} and the associated {@link ResultSet}. Data can
     * be fetched (or iterated over) lazily, fetching records from the
     * {@link ResultSet} one by one.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(String sql, Object... bindings) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL and "lazily" return the generated
     * result.
     * <p>
     * The returned {@link Cursor} holds a reference to the executed
     * {@link PreparedStatement} and the associated {@link ResultSet}. Data can
     * be fetched (or iterated over) lazily, fetching records from the
     * {@link ResultSet} one by one.
     * <p>
     * Unlike {@link #fetchLazy(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetchLazy("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(String sql, QueryPart... parts) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets.
     * <p>
     * Example (Sybase ASE):
     * <p>
     * <code><pre>
     * String sql = "sp_help 'my_table'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    List<Result<Record>> fetchMany(String sql) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * Example (Sybase ASE):
     * <p>
     * <code><pre>
     * String sql = "sp_help 'my_table'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    List<Result<Record>> fetchMany(String sql, Object... bindings) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets.
     * <p>
     * Unlike {@link #fetchMany(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetchMany("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    List<Result<Record>> fetchMany(String sql, QueryPart... parts) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(String sql) throws DataAccessException, InvalidResultException;

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This may be
     *         <code>null</code> if the database returned no records
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(String sql, Object... bindings) throws DataAccessException, InvalidResultException;

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Unlike {@link #fetchOne(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetchOne("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query. This may be
     *         <code>null</code> if the database returned no records
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(String sql, QueryPart... parts) throws DataAccessException, InvalidResultException;

    /**
     * Execute a query holding plain SQL.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    int execute(String sql) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    int execute(String sql, Object... bindings) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Unlike {@link #execute(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * execute("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    int execute(String sql, QueryPart... parts) throws DataAccessException;

    /**
     * Create a new query holding plain SQL.
     * <p>
     * There must not be any binding variables contained in the SQL
     * <p>
     * Use this method, when you want to take advantage of the many ways to
     * fetch results in jOOQ, using {@link ResultQuery}. Some examples:
     * <p>
     * <table border="1">
     * <tr>
     * <td> {@link ResultQuery#fetchLazy()}</td>
     * <td>Open a cursor and fetch records one by one</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(Class)}</td>
     * <td>Fetch records into a custom POJO (optionally annotated with JPA
     * annotations)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(RecordHandler)}</td>
     * <td>Fetch records into a custom callback (similar to Spring's RowMapper)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchLater()}</td>
     * <td>Fetch records of a long-running query asynchronously</td>
     * </tr>
     * </table>
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return An executable query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    ResultQuery<Record> resultQuery(String sql);

    /**
     * Create a new query holding plain SQL.
     * <p>
     * There must be as many bind variables contained in the SQL, as passed in
     * the bindings parameter
     * <p>
     * Use this method, when you want to take advantage of the many ways to
     * fetch results in jOOQ, using {@link ResultQuery}. Some examples:
     * <p>
     * <table border="1">
     * <tr>
     * <td> {@link ResultQuery#fetchLazy()}</td>
     * <td>Open a cursor and fetch records one by one</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(Class)}</td>
     * <td>Fetch records into a custom POJO (optionally annotated with JPA
     * annotations)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(RecordHandler)}</td>
     * <td>Fetch records into a custom callback (similar to Spring's RowMapper)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchLater()}</td>
     * <td>Fetch records of a long-running query asynchronously</td>
     * </tr>
     * </table>
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A query wrapping the plain SQL
     */
    @Support
    ResultQuery<Record> resultQuery(String sql, Object... bindings);

    /**
     * Create a new query holding plain SQL.
     * <p>
     * Unlike {@link #resultQuery(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * resultQuery("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will render this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link DSL#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query wrapping the plain SQL
     */
    @Support
    ResultQuery<Record> resultQuery(String sql, QueryPart... parts);

    // -------------------------------------------------------------------------
    // XXX JDBC convenience methods
    // -------------------------------------------------------------------------

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}.
     * <p>
     * After fetching all data, the JDBC ResultSet will be closed.
     * <p>
     * Use {@link #fetchLazy(ResultSet)}, to fetch one <code>Record</code> at a
     * time, instead of load the entire <code>ResultSet</code> into a jOOQ
     * <code>Result</code> at once.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(ResultSet rs) throws DataAccessException;

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}.
     * <p>
     * After fetching all data, the JDBC ResultSet will be closed.
     * <p>
     * Use {@link #fetchLazy(ResultSet)}, to fetch one <code>Record</code> at a
     * time, instead of load the entire <code>ResultSet</code> into a jOOQ
     * <code>Result</code> at once.
     * <p>
     * The additional <code>fields</code> argument is used by jOOQ to coerce
     * field names and data types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param fields The fields to use in the desired output
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(ResultSet rs, Field<?>... fields) throws DataAccessException;

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}.
     * <p>
     * After fetching all data, the JDBC ResultSet will be closed.
     * <p>
     * Use {@link #fetchLazy(ResultSet)}, to fetch one <code>Record</code> at a
     * time, instead of load the entire <code>ResultSet</code> into a jOOQ
     * <code>Result</code> at once.
     * <p>
     * The additional <code>types</code> argument is used by jOOQ to coerce data
     * types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param types The data types to use in the desired output
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(ResultSet rs, DataType<?>... types) throws DataAccessException;

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}.
     * <p>
     * After fetching all data, the JDBC ResultSet will be closed.
     * <p>
     * Use {@link #fetchLazy(ResultSet)}, to fetch one <code>Record</code> at a
     * time, instead of load the entire <code>ResultSet</code> into a jOOQ
     * <code>Result</code> at once.
     * <p>
     * The additional <code>types</code> argument is used by jOOQ to coerce data
     * types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param types The data types to use in the desired output
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(ResultSet rs, Class<?>... types) throws DataAccessException;

    /**
     * Fetch a record from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Record}.
     * <p>
     * This will internally fetch all records and throw an exception if there
     * was more than one resulting record.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(ResultSet rs) throws DataAccessException, InvalidResultException;

    /**
     * Fetch a record from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Record}.
     * <p>
     * This will internally fetch all records and throw an exception if there
     * was more than one resulting record.
     * <p>
     * The additional <code>fields</code> argument is used by jOOQ to coerce
     * field names and data types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param fields The fields to use in the desired output
     * @return The resulting jOOQ record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(ResultSet rs, Field<?>... fields) throws DataAccessException, InvalidResultException;

    /**
     * Fetch a record from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Record}.
     * <p>
     * This will internally fetch all records and throw an exception if there
     * was more than one resulting record.
     * <p>
     * The additional <code>types</code> argument is used by jOOQ to coerce data
     * types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param types The data types to use in the desired output
     * @return The resulting jOOQ record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(ResultSet rs, DataType<?>... types) throws DataAccessException, InvalidResultException;

    /**
     * Fetch a record from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Record}.
     * <p>
     * This will internally fetch all records and throw an exception if there
     * was more than one resulting record.
     * <p>
     * The additional <code>types</code> argument is used by jOOQ to coerce data
     * types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param types The data types to use in the desired output
     * @return The resulting jOOQ record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    Record fetchOne(ResultSet rs, Class<?>... types) throws DataAccessException, InvalidResultException;

    /**
     * Wrap a JDBC {@link ResultSet} into a jOOQ {@link Cursor}.
     * <p>
     * Use {@link #fetch(ResultSet)}, to load the entire <code>ResultSet</code>
     * into a jOOQ <code>Result</code> at once.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(ResultSet rs) throws DataAccessException;

    /**
     * Wrap a JDBC {@link ResultSet} into a jOOQ {@link Cursor}.
     * <p>
     * Use {@link #fetch(ResultSet)}, to load the entire <code>ResultSet</code>
     * into a jOOQ <code>Result</code> at once.
     * <p>
     * The additional <code>fields</code> argument is used by jOOQ to coerce
     * field names and data types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param fields The fields to use in the desired output
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(ResultSet rs, Field<?>... fields) throws DataAccessException;

    /**
     * Wrap a JDBC {@link ResultSet} into a jOOQ {@link Cursor}.
     * <p>
     * Use {@link #fetch(ResultSet)}, to load the entire <code>ResultSet</code>
     * into a jOOQ <code>Result</code> at once.
     * <p>
     * The additional <code>types</code> argument is used by jOOQ to coerce data
     * types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param types The data types to use in the desired output
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(ResultSet rs, DataType<?>... types) throws DataAccessException;

    /**
     * Wrap a JDBC {@link ResultSet} into a jOOQ {@link Cursor}.
     * <p>
     * Use {@link #fetch(ResultSet)}, to load the entire <code>ResultSet</code>
     * into a jOOQ <code>Result</code> at once.
     * <p>
     * The additional <code>types</code> argument is used by jOOQ to coerce data
     * types to the desired output
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @param types The data types to use in the desired output
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Cursor<Record> fetchLazy(ResultSet rs, Class<?>... types) throws DataAccessException;

    /**
     * Fetch all data from a formatted string.
     * <p>
     * The supplied string is supposed to be formatted in the following,
     * human-readable way: <code><pre>
     * COL1  COL2   COL3 containing whitespace
     * ----- ----   --------------------------
     * val1  1      some text
     * val2   2      more text
     * </pre></code> This method will decode the above formatted string
     * according to the following rules:
     * <ul>
     * <li>The number of columns is defined by the number of dash groups in the
     * second line</li>
     * <li>The column types are <code>VARCHAR(N)</code> where
     * <code>N = number of dashes per dash group</code></li>
     * <li>The column names are defined by the trimmed text contained in the
     * first row</li>
     * <li>The data is defined by the trimmed text contained in the subsequent
     * rows</li>
     * </ul>
     * <p>
     * This is the same as calling {@link #fetchFromTXT(String, String)} with
     * <code>"{null}"</code> as <code>nullLiteral</code>
     * <p>
     * A future version of jOOQ will also support the inverse operation of
     * {@link Result#format()} through this method
     *
     * @param string The formatted string
     * @return The transformed result
     * @see #fetchFromTXT(String, String)
     * @throws DataAccessException If the supplied string does not adhere to the
     *             above format rules.
     */
    @Support
    Result<Record> fetchFromTXT(String string) throws DataAccessException;

    /**
     * Fetch all data from a formatted string.
     * <p>
     * The supplied string is supposed to be formatted in the following,
     * human-readable way: <code><pre>
     * COL1  COL2   COL3 containing whitespace
     * ----- ----   --------------------------
     * val1  1      some text
     * val2   2      more text
     * </pre></code> This method will decode the above formatted string
     * according to the following rules:
     * <ul>
     * <li>The number of columns is defined by the number of dash groups in the
     * second line</li>
     * <li>The column types are <code>VARCHAR(N)</code> where
     * <code>N = number of dashes per dash group</code></li>
     * <li>The column names are defined by the trimmed text contained in the
     * first row</li>
     * <li>The data is defined by the trimmed text contained in the subsequent
     * rows</li>
     * </ul>
     * <p>
     * A future version of jOOQ will also support the inverse operation of
     * {@link Result#format()} through this method
     *
     * @param string The formatted string
     * @param nullLiteral The string literal to be used as <code>null</code>
     *            value.
     * @return The transformed result
     * @throws DataAccessException If the supplied string does not adhere to the
     *             above format rules.
     */
    @Support
    Result<Record> fetchFromTXT(String string, String nullLiteral) throws DataAccessException;

    /**
     * Fetch all data from a CSV string.
     * <p>
     * This is the same as calling <code>fetchFromCSV(string, ',')</code> and
     * the inverse of calling {@link Result#formatCSV()}. The first row of the
     * CSV data is required to hold field name information. Subsequent rows may
     * contain data, which is interpreted as {@link String}. Use the various
     * conversion methods to retrieve other data types from the
     * <code>Result</code>:
     * <ul>
     * <li> {@link Result#getValues(Field, Class)}</li>
     * <li> {@link Result#getValues(int, Class)}</li>
     * <li> {@link Result#getValues(String, Class)}</li>
     * <li> {@link Result#getValues(Field, Converter)}</li>
     * <li> {@link Result#getValues(int, Converter)}</li>
     * <li> {@link Result#getValues(String, Converter)}</li>
     * </ul>
     * <p>
     * Missing values result in <code>null</code>. Empty values result in empty
     * <code>Strings</code>
     *
     * @param string The CSV string
     * @return The transformed result
     * @throws DataAccessException If anything went wrong parsing the CSV file
     * @see #fetchFromCSV(String, char)
     */
    @Support
    Result<Record> fetchFromCSV(String string) throws DataAccessException;

    /**
     * Fetch all data from a CSV string.
     * <p>
     * This is inverse of calling {@link Result#formatCSV(char)}. The first row
     * of the CSV data is required to hold field name information. Subsequent
     * rows may contain data, which is interpreted as {@link String}. Use the
     * various conversion methods to retrieve other data types from the
     * <code>Result</code>:
     * <ul>
     * <li> {@link Result#getValues(Field, Class)}</li>
     * <li> {@link Result#getValues(int, Class)}</li>
     * <li> {@link Result#getValues(String, Class)}</li>
     * <li> {@link Result#getValues(Field, Converter)}</li>
     * <li> {@link Result#getValues(int, Converter)}</li>
     * <li> {@link Result#getValues(String, Converter)}</li>
     * </ul>
     * <p>
     * Missing values result in <code>null</code>. Empty values result in empty
     * <code>Strings</code>
     *
     * @param string The CSV string
     * @param delimiter The delimiter to expect between records
     * @return The transformed result
     * @throws DataAccessException If anything went wrong parsing the CSV file
     * @see #fetchFromCSV(String)
     * @see #fetchFromStringData(List)
     */
    @Support
    Result<Record> fetchFromCSV(String string, char delimiter) throws DataAccessException;

    /**
     * Fetch all data from a list of strings.
     * <p>
     * This is used by methods such as
     * <ul>
     * <li> {@link #fetchFromCSV(String)}</li>
     * <li> {@link #fetchFromTXT(String)}</li>
     * </ul>
     * The first element of the argument list should contain column names.
     * Subsequent elements contain actual data. The degree of all arrays
     * contained in the argument should be the same, although this is not a
     * requirement. jOOQ will ignore excess data, and fill missing data with
     * <code>null</code>.
     *
     * @param data The data to be transformed into a <code>Result</code>
     * @return The transformed result
     * @see #fetchFromStringData(List)
     */
    Result<Record> fetchFromStringData(String[]... data);

    /**
     * Fetch all data from a list of strings.
     * <p>
     * This is used by methods such as
     * <ul>
     * <li> {@link #fetchFromCSV(String)}</li>
     * <li> {@link #fetchFromTXT(String)}</li>
     * </ul>
     * The first element of the argument list should contain column names.
     * Subsequent elements contain actual data. The degree of all arrays
     * contained in the argument should be the same, although this is not a
     * requirement. jOOQ will ignore excess data, and fill missing data with
     * <code>null</code>.
     *
     * @param data The data to be transformed into a <code>Result</code>
     * @return The transformed result
     */
    Result<Record> fetchFromStringData(List<String[]> data);

    // -------------------------------------------------------------------------
    // XXX Global Query factory
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL select statement.
     * <p>
     * Example: <code><pre>
     * SELECT * FROM [table] WHERE [conditions] ORDER BY [ordering] LIMIT [limit clause]
     * </pre></code>
     */
    @Support
    <R extends Record> SelectWhereStep<R> selectFrom(Table<R> table);

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Collection)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.select(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#select(Collection)
     */
    @Support
    SelectSelectStep<Record> select(Collection<? extends Field<?>> fields);

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field...)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.select(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see DSL#select(Field...)
     */
    @Support
    SelectSelectStep<Record> select(Field<?>... fields);

    // [jooq-tools] START [select]
    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Field#in(Select)}, {@link Field#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1> SelectSelectStep<Record1<T1>> select(Field<T1> field1);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row2#in(Select)}, {@link Row2#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2> SelectSelectStep<Record2<T1, T2>> select(Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row3#in(Select)}, {@link Row3#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row4#in(Select)}, {@link Row4#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, field4)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row5#in(Select)}, {@link Row5#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, field4, field5)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row6#in(Select)}, {@link Row6#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field5, field6)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row7#in(Select)}, {@link Row7#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field6, field7)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row8#in(Select)}, {@link Row8#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field7, field8)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row9#in(Select)}, {@link Row9#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field8, field9)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row10#in(Select)}, {@link Row10#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field9, field10)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row11#in(Select)}, {@link Row11#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field10, field11)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row12#in(Select)}, {@link Row12#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field11, field12)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row13#in(Select)}, {@link Row13#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field12, field13)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row14#in(Select)}, {@link Row14#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field13, field14)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row15#in(Select)}, {@link Row15#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field14, field15)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row16#in(Select)}, {@link Row16#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field15, field16)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row17#in(Select)}, {@link Row17#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field16, field17)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row18#in(Select)}, {@link Row18#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field17, field18)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row19#in(Select)}, {@link Row19#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field18, field19)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row20#in(Select)}, {@link Row20#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field19, field20)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row21#in(Select)}, {@link Row21#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field20, field21)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #select(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row22#in(Select)}, {@link Row22#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#select(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .select(field1, field2, field3, .., field21, field22)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> select(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [select]

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Collection)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.selectDistinct(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Collection)
     */
    @Support
    SelectSelectStep<Record> selectDistinct(Collection<? extends Field<?>> fields);

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field...)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.selectDistinct(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     */
    @Support
    SelectSelectStep<Record> selectDistinct(Field<?>... fields);

    // [jooq-tools] START [selectDistinct]
    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Field#in(Select)}, {@link Field#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1> SelectSelectStep<Record1<T1>> selectDistinct(Field<T1> field1);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row2#in(Select)}, {@link Row2#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2> SelectSelectStep<Record2<T1, T2>> selectDistinct(Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row3#in(Select)}, {@link Row3#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3> SelectSelectStep<Record3<T1, T2, T3>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row4#in(Select)}, {@link Row4#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, field4)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4> SelectSelectStep<Record4<T1, T2, T3, T4>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row5#in(Select)}, {@link Row5#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, field4, field5)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5> SelectSelectStep<Record5<T1, T2, T3, T4, T5>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row6#in(Select)}, {@link Row6#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field5, field6)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6> SelectSelectStep<Record6<T1, T2, T3, T4, T5, T6>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row7#in(Select)}, {@link Row7#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field6, field7)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7> SelectSelectStep<Record7<T1, T2, T3, T4, T5, T6, T7>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row8#in(Select)}, {@link Row8#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field7, field8)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> SelectSelectStep<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row9#in(Select)}, {@link Row9#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field8, field9)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> SelectSelectStep<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row10#in(Select)}, {@link Row10#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field9, field10)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> SelectSelectStep<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row11#in(Select)}, {@link Row11#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field10, field11)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> SelectSelectStep<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row12#in(Select)}, {@link Row12#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field11, field12)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> SelectSelectStep<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row13#in(Select)}, {@link Row13#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field12, field13)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> SelectSelectStep<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row14#in(Select)}, {@link Row14#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field13, field14)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> SelectSelectStep<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row15#in(Select)}, {@link Row15#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field14, field15)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> SelectSelectStep<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row16#in(Select)}, {@link Row16#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field15, field16)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> SelectSelectStep<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row17#in(Select)}, {@link Row17#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field16, field17)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> SelectSelectStep<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row18#in(Select)}, {@link Row18#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field17, field18)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> SelectSelectStep<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row19#in(Select)}, {@link Row19#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field18, field19)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> SelectSelectStep<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row20#in(Select)}, {@link Row20#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field19, field20)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> SelectSelectStep<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row21#in(Select)}, {@link Row21#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field20, field21)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> SelectSelectStep<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL select statement.
     * <p>
     * This is the same as {@link #selectDistinct(Field...)}, except that it
     * declares additional record-level typesafety, which is needed by
     * {@link Row22#in(Select)}, {@link Row22#equal(Select)} and other predicate
     * building methods taking subselect arguments.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectDistinct(Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field, Field)} instead.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .selectDistinct(field1, field2, field3, .., field21, field22)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectDistinct(Field...)
     * @see #selectDistinct(Field...)
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> SelectSelectStep<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> selectDistinct(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [selectDistinct]

    /**
     * Create a new DSL select statement for constant <code>0</code> literal
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectZero()} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.selectZero()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#zero()
     * @see DSL#selectZero()
     */
    @Support
    SelectSelectStep<Record1<Integer>> selectZero();

    /**
     * Create a new DSL select statement for constant <code>1</code> literal
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectOne()} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.selectOne()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#one()
     * @see DSL#selectOne()
     */
    @Support
    SelectSelectStep<Record1<Integer>> selectOne();

    /**
     * Create a new DSL select statement for <code>COUNT(*)</code>
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link DSLContext}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link DSL#selectCount()} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.selectCount()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see DSL#selectCount()
     */
    @Support
    SelectSelectStep<Record1<Integer>> selectCount();

    /**
     * Create a new {@link SelectQuery}
     */
    @Support
    SelectQuery<Record> selectQuery();

    /**
     * Create a new {@link SelectQuery}
     *
     * @param table The table to select data from
     * @return The new {@link SelectQuery}
     */
    @Support
    <R extends Record> SelectQuery<R> selectQuery(TableLike<R> table);

    /**
     * Create a new {@link InsertQuery}
     *
     * @param into The table to insert data into
     * @return The new {@link InsertQuery}
     */
    @Support
    <R extends Record> InsertQuery<R> insertQuery(Table<R> into);

    /**
     * Create a new DSL insert statement. This type of insert may feel more
     * convenient to some users, as it uses the <code>UPDATE</code> statement's
     * <code>SET a = b</code> syntax.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.insertInto(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .newRecord()
     *       .set(field1, value3)
     *       .set(field2, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Support
    <R extends Record> InsertSetStep<R> insertInto(Table<R> into);

    // [jooq-tools] START [insert]
    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1)
     *       .values(field1)
     *       .values(field1)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1> InsertValuesStep1<R, T1> insertInto(Table<R> into, Field<T1> field1);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2)
     *       .values(field1, field2)
     *       .values(field1, field2)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2> InsertValuesStep2<R, T1, T2> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3)
     *       .values(field1, field2, field3)
     *       .values(field1, field2, field3)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3> InsertValuesStep3<R, T1, T2, T3> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, field4)
     *       .values(field1, field2, field3, field4)
     *       .values(field1, field2, field3, field4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4> InsertValuesStep4<R, T1, T2, T3, T4> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, field4, field5)
     *       .values(field1, field2, field3, field4, field5)
     *       .values(field1, field2, field3, field4, field5)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5> InsertValuesStep5<R, T1, T2, T3, T4, T5> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field5, field6)
     *       .values(valueA1, valueA2, valueA3, .., valueA5, valueA6)
     *       .values(valueB1, valueB2, valueB3, .., valueB5, valueB6)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6> InsertValuesStep6<R, T1, T2, T3, T4, T5, T6> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field6, field7)
     *       .values(valueA1, valueA2, valueA3, .., valueA6, valueA7)
     *       .values(valueB1, valueB2, valueB3, .., valueB6, valueB7)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7> InsertValuesStep7<R, T1, T2, T3, T4, T5, T6, T7> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field7, field8)
     *       .values(valueA1, valueA2, valueA3, .., valueA7, valueA8)
     *       .values(valueB1, valueB2, valueB3, .., valueB7, valueB8)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> InsertValuesStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field8, field9)
     *       .values(valueA1, valueA2, valueA3, .., valueA8, valueA9)
     *       .values(valueB1, valueB2, valueB3, .., valueB8, valueB9)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> InsertValuesStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field9, field10)
     *       .values(valueA1, valueA2, valueA3, .., valueA9, valueA10)
     *       .values(valueB1, valueB2, valueB3, .., valueB9, valueB10)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> InsertValuesStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field10, field11)
     *       .values(valueA1, valueA2, valueA3, .., valueA10, valueA11)
     *       .values(valueB1, valueB2, valueB3, .., valueB10, valueB11)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> InsertValuesStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field11, field12)
     *       .values(valueA1, valueA2, valueA3, .., valueA11, valueA12)
     *       .values(valueB1, valueB2, valueB3, .., valueB11, valueB12)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> InsertValuesStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field12, field13)
     *       .values(valueA1, valueA2, valueA3, .., valueA12, valueA13)
     *       .values(valueB1, valueB2, valueB3, .., valueB12, valueB13)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> InsertValuesStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field13, field14)
     *       .values(valueA1, valueA2, valueA3, .., valueA13, valueA14)
     *       .values(valueB1, valueB2, valueB3, .., valueB13, valueB14)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> InsertValuesStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field14, field15)
     *       .values(valueA1, valueA2, valueA3, .., valueA14, valueA15)
     *       .values(valueB1, valueB2, valueB3, .., valueB14, valueB15)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> InsertValuesStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field15, field16)
     *       .values(valueA1, valueA2, valueA3, .., valueA15, valueA16)
     *       .values(valueB1, valueB2, valueB3, .., valueB15, valueB16)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> InsertValuesStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field16, field17)
     *       .values(valueA1, valueA2, valueA3, .., valueA16, valueA17)
     *       .values(valueB1, valueB2, valueB3, .., valueB16, valueB17)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> InsertValuesStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field17, field18)
     *       .values(valueA1, valueA2, valueA3, .., valueA17, valueA18)
     *       .values(valueB1, valueB2, valueB3, .., valueB17, valueB18)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> InsertValuesStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field18, field19)
     *       .values(valueA1, valueA2, valueA3, .., valueA18, valueA19)
     *       .values(valueB1, valueB2, valueB3, .., valueB18, valueB19)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> InsertValuesStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field19, field20)
     *       .values(valueA1, valueA2, valueA3, .., valueA19, valueA20)
     *       .values(valueB1, valueB2, valueB3, .., valueB19, valueB20)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> InsertValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field20, field21)
     *       .values(valueA1, valueA2, valueA3, .., valueA20, valueA21)
     *       .values(valueB1, valueB2, valueB3, .., valueB20, valueB21)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> InsertValuesStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * using(configuration)
     *       .insertInto(table, field1, field2, field3, .., field21, field22)
     *       .values(valueA1, valueA2, valueA3, .., valueA21, valueA22)
     *       .values(valueB1, valueB2, valueB3, .., valueB21, valueB22)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> InsertValuesStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> insertInto(Table<R> into, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [insert]

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.insertInto(table, field1, field2)
     *       .values(value1, value2)
     *       .values(value3, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Support
    <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Field<?>... fields);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.insertInto(table, field1, field2)
     *       .values(value1, value2)
     *       .values(value3, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Support
    <R extends Record> InsertValuesStepN<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields);

    /**
     * Create a new {@link UpdateQuery}
     *
     * @param table The table to update data into
     * @return The new {@link UpdateQuery}
     */
    @Support
    <R extends Record> UpdateQuery<R> updateQuery(Table<R> table);

    /**
     * Create a new DSL update statement.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.update(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </pre></code>
     * <p>
     * Note that some databases support table expressions more complex than
     * simple table references. In CUBRID and MySQL, for instance, you can write
     * <code><pre>
     * create.update(t1.join(t2).on(t1.id.eq(t2.id)))
     *       .set(t1.value, value1)
     *       .set(t2.value, value2)
     *       .where(t1.id.eq(10))
     *       .execute();
     * </pre></code>
     */
    @Support
    <R extends Record> UpdateSetFirstStep<R> update(Table<R> table);

    /**
     * Create a new DSL SQL standard MERGE statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <th>dialect</th>
     * <th>support type</th>
     * <th>documentation</th>
     * </tr>
     * <tr>
     * <td>CUBRID</td>
     * <td>SQL:2008 standard and some enhancements</td>
     * <td><a href="http://www.cubrid.org/manual/90/en/MERGE"
     * >http://www.cubrid.org/manual/90/en/MERGE</a></td>
     * </tr>
     * <tr>
     * <tr>
     * <td>DB2</td>
     * <td>SQL:2008 standard and major enhancements</td>
     * <td><a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.admin.doc/doc/r0010873.htm"
     * >http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.
     * ibm.db2.udb.admin.doc/doc/r0010873.htm</a></td>
     * </tr>
     * <tr>
     * <td>HSQLDB</td>
     * <td>SQL:2008 standard</td>
     * <td><a
     * href="http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA"
     * >http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA</a></td>
     * </tr>
     * <tr>
     * <td>Oracle</td>
     * <td>SQL:2008 standard and minor enhancements</td>
     * <td><a href=
     * "http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/statements_9016.htm"
     * >http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/
     * statements_9016.htm</a></td>
     * </tr>
     * <tr>
     * <td>SQL Server</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href= "http://msdn.microsoft.com/de-de/library/bb510625.aspx"
     * >http://msdn.microsoft.com/de-de/library/bb510625.aspx</a></td>
     * </tr>
     * <tr>
     * <td>Sybase</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href=
     * "http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html"
     * >http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html</a></td>
     * </tr>
     * </table>
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.mergeInto(table)
     *       .using(select)
     *       .on(condition)
     *       .whenMatchedThenUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .whenNotMatchedThenInsert(field1, field2)
     *       .values(value1, value2)
     *       .execute();
     * </pre></code>
     * <p>
     * Note: Using this method, you can also create an H2-specific MERGE
     * statement without field specification. See also
     * {@link #mergeInto(Table, Field...)}
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table);

    // [jooq-tools] START [merge]
    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1> MergeKeyStep1<R, T1> mergeInto(Table<R> table, Field<T1> field1);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2> MergeKeyStep2<R, T1, T2> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3> MergeKeyStep3<R, T1, T2, T3> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4> MergeKeyStep4<R, T1, T2, T3, T4> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5> MergeKeyStep5<R, T1, T2, T3, T4, T5> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6> MergeKeyStep6<R, T1, T2, T3, T4, T5, T6> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7> MergeKeyStep7<R, T1, T2, T3, T4, T5, T6, T7> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8> MergeKeyStep8<R, T1, T2, T3, T4, T5, T6, T7, T8> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9> MergeKeyStep9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> MergeKeyStep10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> MergeKeyStep11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> MergeKeyStep12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> MergeKeyStep13<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> MergeKeyStep14<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> MergeKeyStep15<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> MergeKeyStep16<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> MergeKeyStep17<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> MergeKeyStep18<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> MergeKeyStep19<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> MergeKeyStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> MergeKeyStep21<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> MergeKeyStep22<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> mergeInto(Table<R> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

// [jooq-tools] END [merge]

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Field<?>... fields);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     *
     * @see #mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record> MergeKeyStepN<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields);

    /**
     * Create a new {@link DeleteQuery}
     *
     * @param table The table to delete data from
     * @return The new {@link DeleteQuery}
     */
    @Support
    <R extends Record> DeleteQuery<R> deleteQuery(Table<R> table);

    /**
     * Create a new DSL delete statement.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.delete(table)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </pre></code>
     */
    @Support
    <R extends Record> DeleteWhereStep<R> delete(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX Batch query execution
    // -------------------------------------------------------------------------

    /**
     * Execute a set of queries in batch mode (without bind values).
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.createStatement();
     *
     * for (Query query : queries) {
     *     s.addBatch(query.getSQL(true));
     * }
     *
     * s.execute();
     * </pre></code>
     *
     * @see Statement#executeBatch()
     */
    @Support
    Batch batch(Query... queries);

    /**
     * Execute a set of queries in batch mode (without bind values).
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.createStatement();
     *
     * for (Query query : queries) {
     *     s.addBatch(query.getSQL(true));
     * }
     *
     * s.execute();
     * </pre></code>
     *
     * @see Statement#executeBatch()
     */
    @Support
    Batch batch(Collection<? extends Query> queries);

    /**
     * Execute a set of queries in batch mode (with bind values).
     * <p>
     * When running <code><pre>
     * create.batch(query)
     *       .bind(valueA1, valueA2)
     *       .bind(valueB1, valueB2)
     *       .execute();
     * </pre></code>
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.prepareStatement(query.getSQL(false));
     *
     * for (Object[] bindValues : allBindValues) {
     *     for (Object bindValue : bindValues) {
     *         s.setXXX(bindValue);
     *     }
     *
     *     s.addBatch();
     * }
     *
     * s.execute();
     * </pre></code>
     * <p>
     * Note: bind values will be inlined to a static batch query as in
     * {@link #batch(Query...)}, if you choose to execute queries with
     * <code>{@link Settings#getStatementType()} == {@link StatementType#STATIC_STATEMENT}</code>
     *
     * @see Statement#executeBatch()
     */
    @Support
    BatchBindStep batch(Query query);

    /**
     * Execute a set of <code>INSERT</code> and <code>UPDATE</code> queries in
     * batch mode (with bind values).
     * <p>
     * This batch operation can be executed in two modes:
     * <p>
     * <h5>With
     * <code>{@link Settings#getStatementType()} == {@link StatementType#PREPARED_STATEMENT}</code>
     * (the default)</h5>
     * <p>
     * In this mode, record order is preserved as much as possible, as long as
     * two subsequent records generate the same SQL (with bind variables). The
     * number of executed batch operations corresponds to
     * <code>[number of distinct rendered SQL statements]</code>. In the worst
     * case, this corresponds to the number of total records.
     * <p>
     * The record type order is preserved in the way they are passed to this
     * method. This is an example of how statements will be ordered: <code><pre>
     * // Let's assume, odd numbers result in INSERTs and even numbers in UPDATES
     * // Let's also assume a[n] are all of the same type, just as b[n], c[n]...
     * int[] result = create.batchStore(a1, a2, a3, b1, a4, c1, b3, a5)
     *                      .execute();
     * </pre></code> The above results in <code>result.length == 8</code> and
     * the following 4 separate batch statements:
     * <ol>
     * <li>INSERT a1, a3, a5</li>
     * <li>UPDATE a2, a4</li>
     * <li>INSERT b1, b3</li>
     * <li>INSERT c1</li>
     * </ol>
     * <p>
     * <h5>With
     * <code>{@link Settings#getStatementType()} == {@link StatementType#STATIC_STATEMENT}</code>
     * </h5>
     * <p>
     * This mode may be better for large and complex batch store operations, as
     * the order of records is preserved entirely, and jOOQ can guarantee that
     * only a single batch statement is serialised to the database.
     *
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchStore(UpdatableRecord<?>... records);

    /**
     * Execute a set of <code>INSERT</code> and <code>UPDATE</code> queries in
     * batch mode (with bind values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchStore(Collection<? extends UpdatableRecord<?>> records);

    /**
     * Execute a set of <code>INSERT</code> queries in batch mode (with bind
     * values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchInsert(UpdatableRecord<?>... records);

    /**
     * Execute a set of <code>INSERT</code> queries in batch mode (with bind
     * values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchInsert(Collection<? extends UpdatableRecord<?>> records);

    /**
     * Execute a set of <code>UPDATE</code> queries in batch mode (with bind
     * values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchUpdate(UpdatableRecord<?>... records);

    /**
     * Execute a set of <code>UPDATE</code> queries in batch mode (with bind
     * values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchUpdate(Collection<? extends UpdatableRecord<?>> records);

    /**
     * Execute a set of <code>DELETE</code> queries in batch mode (with bind
     * values).
     * <p>
     * This batch operation can be executed in two modes:
     * <p>
     * <h5>With
     * <code>{@link Settings#getStatementType()} == {@link StatementType#PREPARED_STATEMENT}</code>
     * (the default)</h5>
     * <p>
     * In this mode, record order is preserved as much as possible, as long as
     * two subsequent records generate the same SQL (with bind variables). The
     * number of executed batch operations corresponds to
     * <code>[number of distinct rendered SQL statements]</code>. In the worst
     * case, this corresponds to the number of total records.
     * <p>
     * The record type order is preserved in the way they are passed to this
     * method. This is an example of how statements will be ordered: <code><pre>
     * // Let's assume a[n] are all of the same type, just as b[n], c[n]...
     * int[] result = create.batchStore(a1, a2, a3, b1, a4, c1, c2, a5)
     *                      .execute();
     * </pre></code> The above results in <code>result.length == 8</code> and
     * the following 5 separate batch statements:
     * <ol>
     * <li>DELETE a1, a2, a3</li>
     * <li>DELETE b1</li>
     * <li>DELETE a4</li>
     * <li>DELETE c1, c2</li>
     * <li>DELETE a5</li>
     * </ol>
     * <p>
     * <h5>With
     * <code>{@link Settings#getStatementType()} == {@link StatementType#STATIC_STATEMENT}</code>
     * </h5>
     * <p>
     * This mode may be better for large and complex batch delete operations, as
     * the order of records is preserved entirely, and jOOQ can guarantee that
     * only a single batch statement is serialised to the database.
     *
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchDelete(UpdatableRecord<?>... records);

    /**
     * Execute a set of <code>DELETE</code> in batch mode (with bind values).
     *
     * @see #batchDelete(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    Batch batchDelete(Collection<? extends UpdatableRecord<?>> records);

    // -------------------------------------------------------------------------
    // XXX DDL Statements
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Example: <code><pre>
     * Executor create = Factory.using();
     *
     * create.truncate(table)
     *       .execute();
     * </pre></code>
     * <p>
     * Most dialects implement the <code>TRUNCATE</code> statement. If it is not
     * supported, it is simulated using an equivalent <code>DELETE</code>
     * statement. This is particularly true for these dialects:
     * <ul>
     * <li> {@link SQLDialect#FIREBIRD}</li>
     * <li> {@link SQLDialect#INGRES}</li>
     * <li> {@link SQLDialect#SQLITE}</li>
     * </ul>
     * <p>
     * Note, this statement is only supported in DSL mode. Immediate execution
     * is omitted for future extensibility of this command.
     */
    @Support
    <R extends Record> Truncate<R> truncate(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX Other queries for identites and sequences
    // -------------------------------------------------------------------------

    /**
     * Retrieve the last inserted ID.
     * <p>
     * Note, there are some restrictions to the following dialects:
     * <ul>
     * <li> {@link SQLDialect#DB2} doesn't support this</li>
     * <li> {@link SQLDialect#ORACLE} doesn't support this</li>
     * <li> {@link SQLDialect#POSTGRES} doesn't support this</li>
     * <li> {@link SQLDialect#SQLITE} supports this, but its support is poorly
     * documented.</li>
     * </ul>
     *
     * @return The last inserted ID. This may be <code>null</code> in some
     *         dialects, if no such number is available.
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ ASE, CUBRID, DERBY, H2, HSQLDB, INGRES, MYSQL, SQLITE, SQLSERVER, SYBASE })
    BigInteger lastID() throws DataAccessException;

    /**
     * Convenience method to fetch the NEXTVAL for a sequence directly from this
     * {@link DSLContext}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, ORACLE, POSTGRES, SYBASE })
    <T extends Number> T nextval(Sequence<T> sequence) throws DataAccessException;

    /**
     * Convenience method to fetch the CURRVAL for a sequence directly from this
     * {@link DSLContext}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ CUBRID, DB2, FIREBIRD, H2, INGRES, ORACLE, POSTGRES, SYBASE })
    <T extends Number> T currval(Sequence<T> sequence) throws DataAccessException;

    /**
     * Use a schema as the default schema of the underlying connection.
     * <p>
     * This has two effects.
     * <ol>
     * <li>The <code>USE [schema]</code> statement is executed on those RDBMS
     * that support this</li>
     * <li>The supplied {@link Schema} is used as the default schema resulting
     * in omitting that schema in rendered SQL.</li>
     * </ol>
     * <p>
     * The <code>USE [schema]</code> statement translates to the various
     * dialects as follows:
     * <table>
     * <tr>
     * <th>Dialect</th>
     * <th>Command</th>
     * </tr>
     * <tr>
     * <td>DB2</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Derby:</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>H2:</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>HSQLDB:</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>MySQL:</td>
     * <td><code>USE [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Oracle:</td>
     * <td><code>ALTER SESSION SET CURRENT_SCHEMA = [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Postgres:</td>
     * <td><code>SET SEARCH_PATH = [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Sybase:</td>
     * <td><code>USE [schema]</code></td>
     * </tr>
     * </table>
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ DB2, DERBY, H2, HSQLDB, MYSQL, SYBASE, ORACLE, POSTGRES, SYBASE })
    int use(Schema schema) throws DataAccessException;

    /**
     * Use a schema as the default schema of the underlying connection.
     *
     * @see #use(Schema)
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ DB2, DERBY, H2, HSQLDB, MYSQL, SYBASE, ORACLE, POSTGRES, SYBASE })
    int use(String schema) throws DataAccessException;

    // -------------------------------------------------------------------------
    // XXX Global Record factory
    // -------------------------------------------------------------------------

    /**
     * Create a new {@link UDTRecord}.
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <R> The generic record type
     * @param type The UDT describing records of type &lt;R&gt;
     * @return The new record
     */
    <R extends UDTRecord<R>> R newRecord(UDT<R> type);

    /**
     * Create a new {@link Record} that can be inserted into the corresponding
     * table.
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type &lt;R&gt;
     * @return The new record
     */
    <R extends Record> R newRecord(Table<R> table);

    /**
     * Create a new pre-filled {@link Record} that can be inserted into the
     * corresponding table.
     * <p>
     * This performs roughly the inverse operation of {@link Record#into(Class)}
     * <p>
     * The resulting record will have its internal "changed" flags set to true
     * for all values. This means that {@link UpdatableRecord#store()} will
     * perform an <code>INSERT</code> statement. If you wish to store the record
     * using an <code>UPDATE</code> statement, use
     * {@link #executeUpdate(UpdatableRecord)} instead.
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type &lt;R&gt;
     * @param source The source to be used to fill the new record
     * @return The new record
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see Record#from(Object)
     * @see Record#into(Class)
     */
    <R extends Record> R newRecord(Table<R> table, Object source);

    /**
     * Create a new empty {@link Result}.
     * <p>
     * The result is attached to this {@link Configuration} by default. This
     * result can be used as a container for records.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type &lt;R&gt;
     * @return The new result
     */
    <R extends Record> Result<R> newResult(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX Executing queries
    // -------------------------------------------------------------------------

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * results.
     *
     * @param query The query to execute
     * @return The result
     * @throws DataAccessException if something went wrong executing the query
     * @see ResultQuery#fetch()
     */
    <R extends Record> Result<R> fetch(ResultQuery<R> query) throws DataAccessException;

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * a cursor.
     *
     * @param query The query to execute
     * @return The cursor
     * @throws DataAccessException if something went wrong executing the query
     * @see ResultQuery#fetchLazy()
     */
    <R extends Record> Cursor<R> fetchLazy(ResultQuery<R> query) throws DataAccessException;

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * a cursor.
     *
     * @param query The query to execute
     * @return The results
     * @throws DataAccessException if something went wrong executing the query
     * @see ResultQuery#fetchMany()
     */
    <R extends Record> List<Result<Record>> fetchMany(ResultQuery<R> query) throws DataAccessException;

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * a record.
     *
     * @param query The query to execute
     * @return The record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     * @see ResultQuery#fetchOne()
     */
    <R extends Record> R fetchOne(ResultQuery<R> query) throws DataAccessException, InvalidResultException;

    /**
     * Execute a {@link Select} query in the context of this executor and return
     * a <code>COUNT(*)</code> value.
     * <p>
     * This wraps a pre-existing <code>SELECT</code> query in another one to
     * calculate the <code>COUNT(*)</code> value, without modifying the original
     * <code>SELECT</code>. An example: <code><pre>
     * -- Original query:
     * SELECT id, title FROM book WHERE title LIKE '%a%'
     *
     * -- Wrapped query:
     * SELECT count(*) FROM (
     *   SELECT id, title FROM book WHERE title LIKE '%a%'
     * )
     * </pre></code> This is particularly useful for those databases that do not
     * support the <code>COUNT(*) OVER()</code> window function to calculate
     * total results in paged queries.
     *
     * @param query The wrapped query
     * @return The <code>COUNT(*)</code> result
     * @throws DataAccessException if something went wrong executing the query
     */
    int fetchCount(Select<?> query) throws DataAccessException;

    /**
     * Execute a {@link Query} in the context of this executor.
     *
     * @param query The query to execute
     * @return The number of affected rows
     * @throws DataAccessException if something went wrong executing the query
     * @see Query#execute()
     */
    int execute(Query query) throws DataAccessException;

    // -------------------------------------------------------------------------
    // XXX Fast querying
    // -------------------------------------------------------------------------

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table]</pre></code>
     * <p>
     * The result and its contained records are attached to this
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> Result<R> fetch(Table<R> table) throws DataAccessException;

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     * <p>
     * The result and its contained records are attached to this
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> Result<R> fetch(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table]</pre></code>
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    <R extends Record> R fetchOne(Table<R> table) throws DataAccessException, InvalidResultException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    <R extends Record> R fetchOne(Table<R> table, Condition condition) throws DataAccessException,
        InvalidResultException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] LIMIT 1</pre></code>
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> R fetchAny(Table<R> table) throws DataAccessException;

    /**
     * Execute and return all records lazily for
     * <code><pre>SELECT * FROM [table]</pre></code>
     * <p>
     * The result and its contained records are attached to this
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> Cursor<R> fetchLazy(Table<R> table) throws DataAccessException;

    /**
     * Execute and return all records lazily for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     * <p>
     * The result and its contained records are attached to this
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> Cursor<R> fetchLazy(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Insert one record
     * <p>
     * This executes something like the following statement:
     * <code><pre>INSERT INTO [table] ... VALUES [record] </pre></code>
     * <p>
     * Unlike {@link UpdatableRecord#store()}, this does not change any of the
     * argument <code>record</code>'s internal "changed" flags, such that a
     * subsequent call to {@link UpdatableRecord#store()} might lead to another
     * <code>INSERT</code> statement being executed.
     *
     * @return The number of inserted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>> int executeInsert(R record) throws DataAccessException;

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [record is supplied record] </pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends UpdatableRecord<R>> int executeUpdate(R record) throws DataAccessException;

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>, T> int executeUpdate(R record, Condition condition) throws DataAccessException;

    /**
     * Delete a record from a table
     * <code><pre>DELETE FROM [table] WHERE [record is supplied record]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends UpdatableRecord<R>> int executeDelete(R record) throws DataAccessException;

    /**
     * Delete a record from a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>, T> int executeDelete(R record, Condition condition) throws DataAccessException;
}
