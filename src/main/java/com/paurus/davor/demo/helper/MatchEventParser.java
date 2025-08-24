package com.paurus.davor.demo.helper;

import com.paurus.davor.demo.model.MatchEvent;

public class MatchEventParser {

    private MatchEventParser() {
    }

    public static MatchEvent parse(String line) {

        String[] tokens = line.split("\\|");

        String matchId = getTokenAt(tokens, 0, String.class);
        Long marketId = getTokenAt(tokens, 1, Long.class);
        String outcomeId = getTokenAt(tokens, 2, String.class);
        String specifiers = getTokenAt(tokens, 3, String.class);

        return new MatchEvent(matchId, marketId, outcomeId, specifiers);
    }

    private static <T> T getTokenAt(String[] tokens, int index, Class<T> type) {
        if (index < 0 || index >= tokens.length) {
            return null;
        }

        String token = tokens[index];
        if (type == String.class) {
            return type.cast(token.replace("'", ""));
        } else if (type == Long.class) {
            return type.cast(Long.parseLong(token));
        }
        throw new IllegalArgumentException("getTokenAt - unsupported type: " + type);
    }
}
