/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.stmt.rfc6020;

import javax.annotation.Nonnull;
import org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping;
import org.opendaylight.yangtools.yang.model.api.meta.EffectiveStatement;
import org.opendaylight.yangtools.yang.model.api.stmt.ReferenceStatement;
import org.opendaylight.yangtools.yang.parser.spi.SubstatementValidator;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractDeclaredStatement;
import org.opendaylight.yangtools.yang.parser.spi.meta.AbstractStatementSupport;
import org.opendaylight.yangtools.yang.parser.spi.meta.StmtContext;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.effective.ReferenceEffectiveStatementImpl;

public class ReferenceStatementImpl extends AbstractDeclaredStatement<String> implements ReferenceStatement {
    private static final SubstatementValidator SUBSTATEMENT_VALIDATOR = SubstatementValidator.builder(Rfc6020Mapping
            .REFERENCE)
            .build();

    protected ReferenceStatementImpl(final StmtContext<String, ReferenceStatement, ?> context) {
        super(context);
    }

    public static class Definition extends AbstractStatementSupport<String,ReferenceStatement,EffectiveStatement<String,ReferenceStatement>> {

        public Definition() {
            super(Rfc6020Mapping.REFERENCE);
        }

        @Override
        public String parseArgumentValue(final StmtContext<?, ?, ?> ctx, final String value) {
            return value;
        }

        @Override
        public ReferenceStatement createDeclared(final StmtContext<String, ReferenceStatement, ?> ctx) {
            return new ReferenceStatementImpl(ctx);
        }

        @Override
        public EffectiveStatement<String, ReferenceStatement> createEffective(
                final StmtContext<String, ReferenceStatement, EffectiveStatement<String, ReferenceStatement>> ctx) {
            return new ReferenceEffectiveStatementImpl(ctx);
        }

        @Override
        public void onFullDefinitionDeclared(StmtContext.Mutable<String, ReferenceStatement,
                EffectiveStatement<String, ReferenceStatement>> stmt) {
            super.onFullDefinitionDeclared(stmt);
            SUBSTATEMENT_VALIDATOR.validate(stmt);
        }
    }

    @Nonnull @Override
    public String getText() {
        return rawArgument();
    }
}
