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
import org.opendaylight.yangtools.yang.model.api.LeafListSchemaNode;

class LeafListNodeDataWithSchema extends CompositeNodeDataWithSchema {
    public LeafListNodeDataWithSchema(final DataSchemaNode schema) {
        super(schema);
    }

    @Override
    public void write(final SchemaAwareNormalizedNodeStreamWriter writer) throws IOException {
        final LeafListSchemaNode schema = (LeafListSchemaNode) getSchema();
        writer.nextDataSchemaNode(schema);
        if (schema.isUserOrdered()) {
            writer.startOrderedLeafSet(provideNodeIdentifier(), childSizeHint());
        } else {
            writer.startLeafSet(provideNodeIdentifier(), childSizeHint());
        }
        super.write(writer);
        writer.endNode();
    }
}
