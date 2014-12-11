/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.codec.gson;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.opendaylight.yangtools.yang.data.api.codec.LeafrefCodec;

final class JSONLeafrefCodec implements JSONCodec<Object>, LeafrefCodec<String> {
    @Override
    public Object deserialize(final String input) {
        return input;
    }

    @Override
    public String serialize(final Object input) {
        return String.valueOf(input);
    }

    @Override
    public boolean needQuotes() {
        return true;
    }

    /**
     * Serialize specified value with specified JsonWriter.
     *
     * @param writer JsonWriter
     * @param value
     */
    @Override
    public void serializeToWriter(JsonWriter writer, Object value) throws IOException {
        writer.value(serialize(value));
    }
}