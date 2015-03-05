/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.util;

import org.opendaylight.yangtools.yang.data.api.codec.Int64Codec;
import org.opendaylight.yangtools.yang.model.api.type.IntegerTypeDefinition;

final class Int64StringCodec extends LeafSimpleValueStringCodec<Long, IntegerTypeDefinition> implements
        Int64Codec<String> {

    protected Int64StringCodec(final IntegerTypeDefinition typeDef) {
        super(typeDef, Long.class);
    }

    @Override
    public Long deserialize(final String stringRepresentation) {
        int base = provideBase(stringRepresentation);
        if (base == 16) {
            return Long.valueOf(normalizeHexadecimal(stringRepresentation), base);
        }
        return Long.valueOf(stringRepresentation, base);
    }

    @Override
    public String serialize(final Long data) {
        return data == null ? "" : data.toString();
    }
}