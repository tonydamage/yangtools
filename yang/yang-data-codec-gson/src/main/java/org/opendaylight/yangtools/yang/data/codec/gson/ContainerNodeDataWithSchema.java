/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.codec.gson;

import java.io.IOException;
import org.opendaylight.yangtools.yang.data.api.schema.stream.SchemaAwareNormalizedNodeStreamWriter;
import org.opendaylight.yangtools.yang.model.api.DataSchemaNode;

class ContainerNodeDataWithSchema extends CompositeNodeDataWithSchema {

    public ContainerNodeDataWithSchema(final DataSchemaNode schema) {
        super(schema);
    }

    @Override
    public void write(final SchemaAwareNormalizedNodeStreamWriter writer) throws IOException {
        writer.nextDataSchemaNode(getSchema());
        writer.startContainerNode(provideNodeIdentifier(), childSizeHint());
        super.write(writer);
        writer.endNode();
    }

}
