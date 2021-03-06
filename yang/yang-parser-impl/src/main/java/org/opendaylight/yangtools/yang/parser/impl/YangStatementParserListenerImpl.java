/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import org.opendaylight.yangtools.antlrv4.code.gen.YangStatementParser.ArgumentContext;
import org.opendaylight.yangtools.antlrv4.code.gen.YangStatementParser.KeywordContext;
import org.opendaylight.yangtools.antlrv4.code.gen.YangStatementParser.StatementContext;
import org.opendaylight.yangtools.antlrv4.code.gen.YangStatementParserBaseListener;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.YangConstants;
import org.opendaylight.yangtools.yang.model.api.Rfc6020Mapping;
import org.opendaylight.yangtools.yang.parser.spi.meta.ModelProcessingPhase;
import org.opendaylight.yangtools.yang.parser.spi.source.DeclarationInTextSource;
import org.opendaylight.yangtools.yang.parser.spi.source.PrefixToModule;
import org.opendaylight.yangtools.yang.parser.spi.source.QNameToStatementDefinition;
import org.opendaylight.yangtools.yang.parser.spi.source.SourceException;
import org.opendaylight.yangtools.yang.parser.spi.source.StatementSourceReference;
import org.opendaylight.yangtools.yang.parser.spi.source.StatementWriter;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.TypeUtils;
import org.opendaylight.yangtools.yang.parser.stmt.rfc6020.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Immutable
public class YangStatementParserListenerImpl extends YangStatementParserBaseListener {
    private static final Logger LOG = LoggerFactory.getLogger(YangStatementParserListenerImpl.class);

    private final List<String> toBeSkipped = new ArrayList<>();
    private final String sourceName;
    private QNameToStatementDefinition stmtDef;
    private PrefixToModule prefixes;
    private StatementWriter writer;

    public YangStatementParserListenerImpl(final String sourceName) {
        this.sourceName = sourceName;
    }

    public void setAttributes(final StatementWriter writer, final QNameToStatementDefinition stmtDef) {
        this.writer = writer;
        this.stmtDef = stmtDef;
    }

    public void setAttributes(final StatementWriter writer, final QNameToStatementDefinition stmtDef, final PrefixToModule prefixes) {
        this.writer = writer;
        this.stmtDef = stmtDef;
        this.prefixes = prefixes;
    }

    @Override
    public void enterStatement(final StatementContext ctx) {
        final StatementSourceReference ref = DeclarationInTextSource.atPosition(sourceName, ctx
                .getStart().getLine(), ctx.getStart().getCharPositionInLine());
        final KeywordContext keywordCtx = Verify.verifyNotNull(ctx.getChild(KeywordContext.class, 0));
        final ArgumentContext argumentCtx = ctx.getChild(ArgumentContext.class, 0);
        final String keywordTxt = keywordCtx.getText();

        final QName identifier = QName.create(YangConstants.RFC6020_YIN_MODULE, keywordTxt);
        final QName validStatementDefinition = Utils.getValidStatementDefinition(prefixes, stmtDef, identifier);
        if (stmtDef != null && validStatementDefinition != null && toBeSkipped.isEmpty()) {
            final String argument = argumentCtx != null ? Utils.stringFromStringContext(argumentCtx) : null;
            // FIXME: Refactor/clean up this special case
            if (identifier.equals(Rfc6020Mapping.TYPE.getStatementName())) {
                Preconditions.checkArgument(argument != null);
                if (TypeUtils.isYangTypeBodyStmtString(argument)) {
                    writer.startStatement(QName.create(YangConstants.RFC6020_YIN_MODULE, argument), ref);
                } else {
                    writer.startStatement(QName.create(YangConstants.RFC6020_YIN_MODULE, Rfc6020Mapping
                        .TYPE.getStatementName().getLocalName()), ref);
                }
                writer.argumentValue(argument, ref);
            } else {
                writer.startStatement(validStatementDefinition, ref);
                if (argument != null) {
                    writer.argumentValue(argument, ref);
                }
            }
        } else {
            Preconditions.checkArgument(writer.getPhase() != ModelProcessingPhase.FULL_DECLARATION,
                    "%s is not a YANG statement or use of extension. Source: %s", identifier.getLocalName(), ref);
            toBeSkipped.add(keywordTxt);
        }
    }

    @Override
    public void exitStatement(final StatementContext ctx) {
        final StatementSourceReference ref = DeclarationInTextSource.atPosition(
            sourceName, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

        try {
            KeywordContext keyword = ctx.getChild(KeywordContext.class, 0);
            String statementName = keyword.getText();
            QName identifier = QName.create(YangConstants.RFC6020_YIN_MODULE, statementName);
            if (stmtDef != null && Utils.getValidStatementDefinition(prefixes, stmtDef, identifier) != null
                    && toBeSkipped.isEmpty()) {
                writer.endStatement(ref);
            }

            // No-op if the statement is not on the list
            toBeSkipped.remove(statementName);
        } catch (SourceException e) {
            LOG.warn(e.getMessage(), e);
        }
    }
}
