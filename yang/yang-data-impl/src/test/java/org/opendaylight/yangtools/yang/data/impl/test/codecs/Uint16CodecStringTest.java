/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.yang.data.impl.test.codecs;

import static org.junit.Assert.*;
import static org.opendaylight.yangtools.yang.data.impl.test.codecs.TypeDefinitionAwareCodecTestHelper.deserializeWithExpectedIllegalArgEx;
import static org.opendaylight.yangtools.yang.data.impl.test.codecs.TypeDefinitionAwareCodecTestHelper.getCodec;

import org.junit.Test;
import org.opendaylight.yangtools.yang.data.api.codec.Uint16Codec;
import org.opendaylight.yangtools.yang.model.util.Uint16;

/**
 * Unit tests for Uint16CodecString.
 *
 * @author Thomas Pantelis
 */
public class Uint16CodecStringTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialize() {
        Uint16Codec<String> codec = getCodec(Uint16.getInstance(), Uint16Codec.class);

        assertEquals("serialize", "10", codec.serialize(Integer.valueOf( 10 )));
        assertEquals("serialize", "", codec.serialize(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserialize() {
        final String hexa = "0X45c";
        final String octal = "02134";
        final String integer = "1116";

        Uint16Codec<String> codec = getCodec(Uint16.getInstance(), Uint16Codec.class);

        assertEquals("deserialize", codec.deserialize(hexa), Integer.valueOf("045c", 16));
        assertEquals("deserialize", codec.deserialize(octal), Integer.valueOf(octal, 8));
        assertEquals("deserialize", codec.deserialize(integer), Integer.valueOf(integer, 10));

        deserializeWithExpectedIllegalArgEx(codec, "1o");
        deserializeWithExpectedIllegalArgEx(codec, "");
        deserializeWithExpectedIllegalArgEx(codec, null);
    }
}
