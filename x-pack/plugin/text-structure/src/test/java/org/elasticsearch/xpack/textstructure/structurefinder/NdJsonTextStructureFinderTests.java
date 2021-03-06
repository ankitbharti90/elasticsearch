/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.textstructure.structurefinder;

import org.elasticsearch.xpack.core.textstructure.structurefinder.TextStructure;

import java.util.Collections;

public class NdJsonTextStructureFinderTests extends TextStructureTestCase {

    private final FileStructureFinderFactory factory = new NdJsonFileStructureFinderFactory();

    public void testCreateConfigsGivenGoodJson() throws Exception {
        assertTrue(factory.canCreateFromSample(explanation, NDJSON_SAMPLE, 0.0));

        String charset = randomFrom(POSSIBLE_CHARSETS);
        Boolean hasByteOrderMarker = randomHasByteOrderMarker(charset);
        FileStructureFinder structureFinder = factory.createFromSample(
            explanation,
            NDJSON_SAMPLE,
            charset,
            hasByteOrderMarker,
            FileStructureFinderManager.DEFAULT_LINE_MERGE_SIZE_LIMIT,
            FileStructureOverrides.EMPTY_OVERRIDES,
            NOOP_TIMEOUT_CHECKER
        );

        TextStructure structure = structureFinder.getStructure();

        assertEquals(TextStructure.Format.NDJSON, structure.getFormat());
        assertEquals(charset, structure.getCharset());
        if (hasByteOrderMarker == null) {
            assertNull(structure.getHasByteOrderMarker());
        } else {
            assertEquals(hasByteOrderMarker, structure.getHasByteOrderMarker());
        }
        assertNull(structure.getExcludeLinesPattern());
        assertNull(structure.getMultilineStartPattern());
        assertNull(structure.getDelimiter());
        assertNull(structure.getQuote());
        assertNull(structure.getHasHeaderRow());
        assertNull(structure.getShouldTrimFields());
        assertNull(structure.getGrokPattern());
        assertEquals("timestamp", structure.getTimestampField());
        assertEquals(Collections.singletonList("UNIX_MS"), structure.getJodaTimestampFormats());
        assertEquals(Collections.singleton("properties"), structure.getMappings().keySet());
    }
}
