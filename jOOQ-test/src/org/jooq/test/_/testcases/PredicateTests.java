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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class PredicateTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public PredicateTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testIsTrue() throws Exception {
        assertEquals(0, create().select().where(val(null).isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("asdf").isTrue()).fetch().size());

        assertEquals(0, create().select().where(val(0).isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("false").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("n").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("no").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("0").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("disabled").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("off").isTrue()).fetch().size());

        assertEquals(1, create().select().where(val(1).isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("true").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("y").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("yes").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("1").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("enabled").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("on").isTrue()).fetch().size());

        assertEquals(0, create().select().where(val("asdf").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val(null).isFalse()).fetch().size());

        assertEquals(1, create().select().where(val(0).isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("false").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("n").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("no").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("0").isFalse()).fetch().size());

        assertEquals(1, create().select().where(val("disabled").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("off").isFalse()).fetch().size());

        assertEquals(0, create().select().where(val(1).isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("true").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("y").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("yes").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("1").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("enabled").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("on").isFalse()).fetch().size());

        // The below code throws an exception on Ingres when run once. When run
        // twice, the DB crashes... This seems to be a driver / database bug
        if (getDialect() != SQLDialect.INGRES) {
            assertEquals(0, create().select().where(val(false).isTrue()).fetch().size());
            assertEquals(1, create().select().where(val(false).isFalse()).fetch().size());
            assertEquals(1, create().select().where(val(true).isTrue()).fetch().size());
            assertEquals(0, create().select().where(val(true).isFalse()).fetch().size());
        }
    }

    @Test
    public void testIsDistinctFrom() throws Exception {
        assertEquals(0, create().select().where(val(null, Integer.class).isDistinctFrom((Integer) null)).fetch().size());
        assertEquals(1, create().select().where(val(1).isDistinctFrom((Integer) null)).fetch().size());
        assertEquals(1, create().select().where(val(null, Integer.class).isDistinctFrom(1)).fetch().size());
        assertEquals(0, create().select().where(val(1).isDistinctFrom(1)).fetch().size());

        assertEquals(1, create().select().where(val(null, Integer.class).isNotDistinctFrom((Integer) null)).fetch().size());
        assertEquals(0, create().select().where(val(1).isNotDistinctFrom((Integer) null)).fetch().size());
        assertEquals(0, create().select().where(val(null, Integer.class).isNotDistinctFrom(1)).fetch().size());
        assertEquals(1, create().select().where(val(1).isNotDistinctFrom(1)).fetch().size());
    }

    @Test
    public void testLike() throws Exception {
        jOOQAbstractTest.reset = false;

        Field<String> notLike = TBook_PUBLISHED_IN().cast(String.class);

        // DB2 doesn't support this syntax
        if (getDialect() == DB2) {
            notLike = val("bbb");
        }

        Result<B> books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like("%a%"))
                .and(TBook_TITLE().notLike(notLike))
                .fetch();

        assertEquals(3, books.size());

        assertEquals(1,
        create().insertInto(TBook())
                .set(TBook_ID(), 5)
                .set(TBook_AUTHOR_ID(), 2)
                .set(TBook_PUBLISHED_IN(), 2012)
                .set(TBook_LANGUAGE_ID(), 1)
                .set(TBook_TITLE(), "About percentages (%) and underscores (_), a critical review! Check exclamation marks, too!")
                .execute());

        // [#1072] Add checks for ESCAPE syntax
        // ------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like("%(!%)%", '!'))
                .and(TBook_TITLE().like("%(#_)%", '#'))
                .and(TBook_TITLE().notLike("%(!%)%", '#'))
                .and(TBook_TITLE().notLike("%(#_)%", '!'))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));

        // DERBY doesn't know any REPLACE function, hence only test those
        // conditions that do not use REPLACE internally
        boolean derby = getDialect() == DERBY;

        // [#1131] DB2 doesn't like concat in LIKE expressions very much
        // -------------------------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like(concat("19", "84")))
                .and(TBook_TITLE().like(upper(concat("198", "4"))))
                .and(TBook_TITLE().like(lower(concat("1", "984"))))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(1, (int) books.get(0).getValue(TBook_ID()));

        // [#1106] Add checks for Factory.escape() function
        // ------------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like(concat("%", escape("(%)", '!'), "%"), '!'))
                .and(derby ? trueCondition() :
                     TBook_TITLE().like(concat(val("%"), escape(val("(_)"), '#'), val("%")), '#'))
                .and(TBook_TITLE().notLike(concat("%", escape("(!%)", '#'), "%"), '#'))
                .and(derby ? trueCondition() :
                     TBook_TITLE().notLike(concat(val("%"), escape(val("(#_)"), '!'), val("%")), '!'))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));

        // [#1089] Add checks for convenience methods
        // ------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().contains("%"))
                .and(TBook_TITLE().contains("review!"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().contains(val("(_")))

                .and(TBook_TITLE().startsWith("About"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().startsWith(val("Abo")))

                .and(TBook_TITLE().endsWith("too!"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().endsWith(val("too!")))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));

        // [#1159] Add checks for matching numbers with LIKE
        // -------------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_PUBLISHED_IN().like("194%"))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, books.size());
        assertEquals(asList(1, 2), books.getValues(TBook_ID()));

        books =
        create().selectFrom(TBook())
                .where(TBook_PUBLISHED_IN().like("%9%"))
                .and(TBook_PUBLISHED_IN().notLike("%8%"))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, books.size());
        assertEquals(asList(2, 4), books.getValues(TBook_ID()));

        // [#1160] Add checks for convenience methods using numbers
        // --------------------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_PUBLISHED_IN().contains(9))
                .and(TBook_PUBLISHED_IN().endsWith(88))
                .and(TBook_PUBLISHED_IN().startsWith(1))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(1, books.size());
        assertEquals(asList(3), books.getValues(TBook_ID()));

        // [#1423] Add checks for the ILIKE operator
        // -----------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().likeIgnoreCase("%IM%"))
                .and(TBook_TITLE().notLikeIgnoreCase("%o%"))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(asList(2), books.getValues(TBook_ID()));
    }

    @Test
    public void testLikeRegex() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case FIREBIRD:
            case INGRES:
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "REGEX tests");
                return;
        }

        Result<B> result =
        create().selectFrom(TBook())
                .where(TBook_CONTENT_TEXT().likeRegex(".*conscious.*"))
                .or(TBook_TITLE().notLikeRegex(".*m.*"))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals(1, (int) result.get(0).getValue(TBook_ID()));
        assertEquals(4, (int) result.get(1).getValue(TBook_ID()));
    }

    @Test
    public void testLargeINCondition() throws Exception {
        Field<Integer> count = count();
        assertEquals(1, (int) create().select(count)
                                      .from(TBook())
                                      .where(TBook_ID().in(Collections.nCopies(999, 1)))
                                      .fetchOne(count));

        switch (getDialect()) {
            case INGRES:
            case SQLITE:
                log.info("SKIPPING", "SQLite/Ingres can't handle more than 999 variables");
                break;

            default:
                // Oracle needs splitting of IN(..) expressions when there are
                // more than 1000 elements
                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1000, 1)))
                    .fetchOne(count));

                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1001, 1)))
                    .fetchOne(count));

                // SQL Server's is at 2100...
                // Sybase ASE's is at 2000...
                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1950, 1)))
                    .fetchOne(count));

                assertEquals(3, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().notIn(Collections.nCopies(1950, 1)))
                    .fetchOne(count));

                // [#1520] Any database should be able to handle lots of inlined
                // variables, including SQL Server and Sybase ASE
                assertEquals(3, (int) create(new Settings().withStatementType(STATIC_STATEMENT))
                    .select(count)
                    .from(TBook())
                    .where(TBook_ID().notIn(Collections.nCopies(3000, 1)))
                    .fetchOne(count));

                // [#1515] Check correct splitting of NOT IN
                List<Integer> list = new ArrayList<Integer>();
                list.addAll(Collections.nCopies(1000, 1));
                list.addAll(Collections.nCopies(1000, 2));

                assertEquals(2, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().notIn(list))
                    .fetchOne(count));

                break;
        }
    }

    @Test
    public void testConditionalSelect() throws Exception {
        Condition c = trueCondition();

        assertEquals(4, create().selectFrom(TBook()).where(c).execute());

        c = c.and(TBook_PUBLISHED_IN().greaterThan(1945));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());

        c = c.not();
        assertEquals(1, create().selectFrom(TBook()).where(c).execute());

        c = c.or(TBook_AUTHOR_ID().equal(
            select(TAuthor_ID()).from(TAuthor()).where(TAuthor_FIRST_NAME().equal("Paulo"))));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());
    }

    @Test
    public void testConditions() throws Exception {
        // The BETWEEN clause
        assertEquals(Arrays.asList(2, 3), create().select()
            .from(TBook())
            .where(TBook_ID().between(2, 3))
            .and(TBook_ID().betweenSymmetric(2, 3))
            .and(TBook_ID().betweenSymmetric(3, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(3, 4), create().select()
            .from(TBook())
            .where(val(3).between(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // [#1074] The NOT BETWEEN clause
        assertEquals(Arrays.asList(1, 4), create().select()
            .from(TBook())
            .where(TBook_ID().notBetween(2, 3))
            .and(TBook_ID().notBetweenSymmetric(2, 3))
            .and(TBook_ID().notBetweenSymmetric(3, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2), create().select()
            .from(TBook())
            .where(val(3).notBetween(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // The IN clause
        // [#502] empty set checks
        assertEquals(Arrays.asList(), create().select()
            .from(TBook())
            .where(TBook_ID().in(new Integer[0]))
            .fetch(TBook_ID()));
        assertEquals(BOOK_IDS, create().select()
            .from(TBook())
            .where(TBook_ID().notIn(new Integer[0]))
            .orderBy(TBook_ID())
            .fetch(TBook_ID()));

        // The IN clause
        // [#1073] NULL checks
        assertEquals(
        asList(1),
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().in(val(1), castNull(Integer.class)))
                .fetch(TBook_ID()));

        // [#1073] Some dialects incorrectly handle NULL in NOT IN predicates
        if (asList(ASE).contains(getDialect())) {
            assertEquals(
            asList(2, 3, 4),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().notIn(val(1), castNull(Integer.class)))
                    .orderBy(TBook_ID())
                    .fetch(TBook_ID()));
        }
        else {
            assertEquals(
            asList(),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().notIn(val(1), castNull(Integer.class)))
                    .fetch(TBook_ID()));
        }

        assertEquals(Arrays.asList(1, 2), create().select()
            .from(TBook())
            .where(TBook_ID().in(1, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(2, 3, 4), create().select()
            .from(TBook())
            .where(val(2).in(TBook_ID(), TBook_AUTHOR_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));
    }

    @Test
    public void testConditionsAsFields() throws Exception {
        Record record = create().select(field(one().eq(zero())), field(one().eq(1))).fetchOne();

        assertEquals(false, record.getValue(0));
        assertEquals(true, record.getValue(1));
    }

    @Test
    public void testQuantifiedPredicates() throws Exception {

        // = { ALL | ANY | SOME }
        switch (getDialect()) {
            case SQLITE:
            case INGRES: // Ingres supports these syntaxes but has internal errors...
                log.info("SKIPPING", "= { ALL | ANY | SOME } tests");
                break;

            default: {

                // Testing = ALL(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(all(selectOne())))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(any(selectOne())))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ALL(array)
                assertEquals(Arrays.asList(1), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(all(1)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(all(1, 2)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(array)
                assertEquals(Arrays.asList(1), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(any(1)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(any(1, 2)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Inducing the above to work the same way as all other operators
                // Check all operators in a single query
                assertEquals(Arrays.asList(3), create()
                    .select()
                    .from(TBook())
                    .where(TBook_ID().equal(select(val(3))))
                    .and(TBook_ID().equal(all(select(val(3)))))
                    .and(TBook_ID().equal(all(3, 3)))
                    .and(TBook_ID().equal(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4)))))
                    .and(TBook_ID().equal(any(3, 4)))
                    .and(TBook_ID().notEqual(select(val(1))))
                    .and(TBook_ID().notEqual(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().notEqual(all(1, 4, 4)))
                    .and(TBook_ID().notEqual(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().notEqual(any(1, 4, 4)))
                    .and(TBook_ID().greaterOrEqual(select(val(1))))
                    .and(TBook_ID().greaterOrEqual(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .and(TBook_ID().greaterOrEqual(all(1, 2)))
                    .and(TBook_ID().greaterOrEqual(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().greaterOrEqual(any(1, 4)))
                    .and(TBook_ID().greaterThan(select(val(1))))
                    .and(TBook_ID().greaterThan(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .and(TBook_ID().greaterThan(all(1, 2)))
                    .and(TBook_ID().greaterThan(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().greaterThan(any(1, 4)))
                    .and(TBook_ID().lessOrEqual(select(val(3))))
                    .and(TBook_ID().lessOrEqual(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4)))))
                    .and(TBook_ID().lessOrEqual(all(3, 4)))
                    .and(TBook_ID().lessOrEqual(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().lessOrEqual(any(1, 4)))
                    .and(TBook_ID().lessThan(select(val(4))))
                    .and(TBook_ID().lessThan(all(select(val(4)))))
                    .and(TBook_ID().lessThan(all(4, 5)))
                    .and(TBook_ID().lessThan(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().lessThan(any(1, 4)))
                    .fetch(TBook_ID()));

                break;
            }
        }
    }

    @Test
    public void testIgnoreCase() {
        A author =
        create().selectFrom(TAuthor())
                .where(TAuthor_FIRST_NAME().equalIgnoreCase(TAuthor_FIRST_NAME()))
                .and(upper(TAuthor_FIRST_NAME()).equalIgnoreCase(lower(TAuthor_FIRST_NAME())))
                .and(TAuthor_FIRST_NAME().equalIgnoreCase("george"))
                .and(TAuthor_FIRST_NAME().equalIgnoreCase("geORge"))
                .and(TAuthor_FIRST_NAME().equalIgnoreCase("GEORGE"))
                .and(TAuthor_FIRST_NAME().notEqualIgnoreCase("paulo"))
                .fetchOne();

        assertEquals("George", author.getValue(TAuthor_FIRST_NAME()));
    }
}
