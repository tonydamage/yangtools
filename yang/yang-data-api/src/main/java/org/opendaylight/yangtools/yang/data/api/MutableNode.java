/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.api;

import org.opendaylight.yangtools.concepts.Mutable;


/**
 * Base representation of node in the data tree, defines basic parameters of
 * node such as a QName.
 *
 *
 * @param <T>
 */
public interface MutableNode<T> extends Node<T>,Mutable {

    /**
     * @param parent value to set
     */
    void setParent(CompositeNode parent);

    /**
     * @param value value to set (children list or leaf value)
     */
    @Override
    T setValue(T value);

    /**
     * @param action value to set
     */
    void setModifyAction(ModifyAction action);
}
