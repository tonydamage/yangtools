/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.common;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Objects;
import org.opendaylight.yangtools.concepts.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QNameModule implements Immutable, Serializable {
    private static final Interner<QNameModule> INTERNER = Interners.newWeakInterner();
    private static final Logger LOG = LoggerFactory.getLogger(QNameModule.class);
    private static final QNameModule NULL_INSTANCE = new QNameModule(null, null);
    private static final long serialVersionUID = 1L;

    //Nullable
    private final URI namespace;

    //Nullable
    private final Date revision;

    //Nullable
    private volatile String formattedRevision;

    private QNameModule(final URI namespace, final Date revision) {
        this.namespace = namespace;
        this.revision = revision;
    }

    /**
     * Look up specified module in the global cache and return a shared reference.
     *
     * @param module Module instance
     * @return Cached instance, according to {@link org.opendaylight.yangtools.objcache.ObjectCache} policy.
     *
     * @deprecated Use {@link #intern()} instead.
     */
    @Deprecated
    public static QNameModule cachedReference(final QNameModule module) {
        return module.intern();
    }

    /**
     * Return an interned reference to a equivalent QNameModule.
     *
     * @return Interned reference, or this object if it was interned.
     */
    public QNameModule intern() {
        return INTERNER.intern(this);
    }

    /**
     * Create a new QName module instance with specified namespace/revision.
     *
     * @param namespace Module namespace
     * @param revision Module revision
     * @return A new, potentially shared, QNameModule instance
     */
    public static QNameModule create(final URI namespace, final Date revision) {
        if (namespace == null && revision == null) {
            return NULL_INSTANCE;
        }

        return new QNameModule(namespace, revision);
    }

    public String getFormattedRevision() {
        if (revision == null) {
            return null;
        }

        String ret = formattedRevision;
        if (ret == null) {
            ret = SimpleDateFormatUtil.getRevisionFormat().format(revision);
            formattedRevision = ret;
        }

        return ret;
    }

    /**
     * Returns the namespace of the module which is specified as argument of
     * YANG Module <b><font color="#00FF00">namespace</font></b> keyword.
     *
     * @return URI format of the namespace of the module
     */
    public URI getNamespace() {
        return namespace;
    }

    /**
     * Returns the revision date for the module.
     *
     * @return date of the module revision which is specified as argument of
     *         YANG Module <b><font color="#339900">revison</font></b> keyword
     */
    public Date getRevision() {
        return revision;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = Objects.hashCode(namespace);
        result = prime * result + Objects.hashCode(revision);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof QNameModule)) {
            return false;
        }
        final QNameModule other = (QNameModule) obj;
        if (!Objects.equals(revision, other.revision)) {
            return false;
        }
        if (!Objects.equals(namespace, other.namespace)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a namespace in form defined by section 5.6.4. of {@link https
     * ://tools.ietf.org/html/rfc6020}, if namespace is not correctly defined,
     * the method will return <code>null</code> <br>
     * example "http://example.acme.com/system?revision=2008-04-01"
     *
     * @return namespace in form defined by section 5.6.4. of {@link https
     *         ://tools.ietf.org/html/rfc6020}, if namespace is not correctly
     *         defined, the method will return <code>null</code>
     *
     */
    URI getRevisionNamespace() {

        if (namespace == null) {
            return null;
        }

        String query = "";
        if (revision != null) {
            query = "revision=" + getFormattedRevision();
        }

        URI compositeURI = null;
        try {
            compositeURI = new URI(namespace.getScheme(), namespace.getUserInfo(), namespace.getHost(),
                    namespace.getPort(), namespace.getPath(), query, namespace.getFragment());
        } catch (final URISyntaxException e) {
            LOG.error("", e);
        }
        return compositeURI;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("ns", getNamespace()).add("rev", getFormattedRevision()).toString();
    }
}
