/*
 * Copyright (c) 2015 Pantheon Technologies s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.model.util.type;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import org.opendaylight.yangtools.concepts.Immutable;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.api.Status;
import org.opendaylight.yangtools.yang.model.api.TypeDefinition;
import org.opendaylight.yangtools.yang.model.api.UnknownSchemaNode;

abstract class AbstractDerivedType<T extends TypeDefinition<T>> implements Immutable, TypeDefinition<T> {
    private final SchemaPath path;
    private final T baseType;
    private final Object defaultValue;
    private final String description;
    private final String reference;
    private final Status status;
    private final String units;
    private final List<UnknownSchemaNode> unknownSchemNodes;

    AbstractDerivedType(final T baseType, final SchemaPath path, final Object defaultValue, final String description,
            final String reference, final Status status, final String units, final Collection<UnknownSchemaNode> unknownSchemNodes) {
        this.baseType = Preconditions.checkNotNull(baseType);
        this.path = Preconditions.checkNotNull(path);
        this.status = Preconditions.checkNotNull(status);
        this.defaultValue = defaultValue;
        this.description = description;
        this.reference = reference;
        this.units = units;
        this.unknownSchemNodes = ImmutableList.copyOf(unknownSchemNodes);
    }

    @Override
    public final T getBaseType() {
        return baseType;
    }

    @Override
    public final Object getDefaultValue() {
        return defaultValue != null ? defaultValue : baseType.getDefaultValue();
    }

    @Override
    public final QName getQName() {
        return path.getLastComponent();
    }

    @Override
    public final SchemaPath getPath() {
        return path;
    }

    @Override
    public final List<UnknownSchemaNode> getUnknownSchemaNodes() {
        return unknownSchemNodes;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public final String getReference() {
        return reference;
    }

    @Override
    public final Status getStatus() {
        return status;
    }

    @Override
    public final String getUnits() {
        return units != null ? units : baseType.getUnits();
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("baseType", baseType).add("default", defaultValue)
                .add("description", description).add("path", path).add("reference", reference).add("status", status)
                .add("units", units).toString();
    }
}