/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.yang.stmt.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opendaylight.yangtools.yang.model.api.Module;
import org.opendaylight.yangtools.yang.parser.spi.meta.MissingSubstatementException;
import org.opendaylight.yangtools.yang.parser.spi.meta.ReactorException;
import org.opendaylight.yangtools.yang.stmt.retest.TestUtils;

public class SubstatementValidatorTest {

    private final PrintStream stdout = System.out;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private String testLog;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws UnsupportedEncodingException {
        System.setOut(new PrintStream(output, true, "UTF-8"));
    }

    @After
    public void cleanUp() {
        System.setOut(stdout);
    }

    @Test
    public void noException() throws URISyntaxException, ReactorException {
        Set<Module> modules = TestUtils.loadModules(getClass().getResource("/augment-test/augment-in-augment").toURI());
        assertNotNull(modules);
    }

    @Test
    public void undesirableElementException() throws URISyntaxException, ReactorException {
        Set<Module> modules = TestUtils.loadModules(getClass().getResource
                ("/substatement-validator/undesirable-element").toURI());
        testLog = output.toString();
        assertTrue(testLog.contains("TYPE is not valid for REVISION"));
    }

    @Test
    public void maximalElementCountException() throws URISyntaxException, ReactorException {
        Set<Module> modules = TestUtils.loadModules(getClass().getResource
                ("/substatement-validator/maximal-element").toURI());
        testLog = output.toString();
        assertTrue(testLog.contains("Maximal count of DESCRIPTION for AUGMENT is 1"));
    }

    @Test
    public void missingElementException() throws URISyntaxException, ReactorException {
        expectedEx.expect(MissingSubstatementException.class);

        Set<Module> modules = TestUtils.loadModules(getClass().getResource
                ("/substatement-validator/missing-element").toURI());
    }

    @Test
    public void emptyElementException() throws URISyntaxException, ReactorException {
        expectedEx.expect(NoSuchElementException.class);

        Set<Module> modules = TestUtils.loadModules(getClass().getResource
                ("/substatement-validator/empty-element").toURI());
    }

    @Test
    public void bug4310test() throws URISyntaxException, ReactorException {
        expectedEx.expect(IllegalArgumentException.class);

        Set<Module> modules = TestUtils.loadModules(getClass().getResource
                ("/substatement-validator/bug-4310").toURI());
    }
}