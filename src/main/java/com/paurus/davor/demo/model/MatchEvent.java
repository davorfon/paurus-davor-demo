package com.paurus.davor.demo.model;

import com.google.common.base.Joiner;
import com.paurus.davor.demo.helper.Tools;

import java.util.Arrays;

public class MatchEvent implements Comparable<MatchEvent> {

    private String matchId;
    private Long marketId;
    private String outcomeId;
    private String specifiers;

    public MatchEvent(String matchId, Long marketId, String outcomeId, String specifiers) {
        this.matchId = matchId;
        this.marketId = marketId;
        this.outcomeId = outcomeId;
        this.specifiers = specifiers;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(String outcomeId) {
        this.outcomeId = outcomeId;
    }

    public String getSpecifiers() {
        return specifiers;
    }

    public void setSpecifiers(String specifiers) {
        this.specifiers = specifiers;
    }

    public String print() {
        return "'" + getMatchId() + "'"
                + "|" + getMarketId()
                + "|'" + getOutcomeId() + "'"
                + "|" + (getSpecifiers() == null ? "" : "'" + getSpecifiers() + "'");
    }

    @Override
    public int compareTo(MatchEvent other) {

        int compare = getMatchIdent(this).compareTo(getMatchIdent(other));
        if (compare != 0) return compare;

        compare = getMatchOrderNumber(this).compareTo(getMatchOrderNumber(other));
        if (compare != 0) return compare;

        compare = this.getMarketId().compareTo(other.getMarketId());
        if (compare != 0) return compare;

        compare = compareOutcomeId(this, other);
        if (compare != 0) return compare;

        return compareSpecifiers(this, other);
    }

    private String getMatchIdent(MatchEvent matchEvent) {
        String[] tokens = matchEvent.getMatchId().split(":");
        String[] identTokens = Arrays.copyOf(tokens, tokens.length - 1);
        return Joiner.on(":").join(identTokens);
    }

    private Integer getMatchOrderNumber(MatchEvent matchEvent) {
        String[] tokens = matchEvent.getMatchId().split(":");
        return Integer.parseInt(tokens[tokens.length - 1]);
    }

    private int compareOutcomeId(MatchEvent c1, MatchEvent c2) {

        boolean isNumber1 = Tools.isNumeric(c1.getOutcomeId());
        boolean isNumber2 = Tools.isNumeric(c2.getOutcomeId());

        if (isNumber1 && isNumber2) {
            Integer outcomeId1 = Integer.parseInt(c1.getOutcomeId());
            Integer outcomeId2 = Integer.parseInt(c2.getOutcomeId());
            return outcomeId1.compareTo(outcomeId2);
        }

        if (!isNumber1 && !isNumber2) {
            return compareString(c1.getOutcomeId(), c2.getOutcomeId());
        }

        if (!isNumber1) {
            return 1;
        }
        return -1;
    }

    private int compareSpecifiers(MatchEvent c1, MatchEvent c2) {
        if (c1.getSpecifiers() == null && c2.getSpecifiers() == null) return 0;
        if (c1.getSpecifiers() == null) return -1;
        if (c2.getSpecifiers() == null) return 1;

        return compareString(c1.getSpecifiers(), c2.getSpecifiers());
    }

    private int compareString(String c1, String c2) {
        String[] tokens1 = c1.split("[:=]");
        String[] tokens2 = c2.split("[:=]");

        int len = Math.min(tokens1.length, tokens2.length);
        for (int i = 0; i < len; i++) {
            String t1 = tokens1[i];
            String t2 = tokens2[i];

            try {
                double i1 = Double.parseDouble(t1);
                double i2 = Double.parseDouble(t2);
                int cmp = Double.compare(i1, i2);
                if (cmp != 0) return cmp;
            } catch (NumberFormatException e) {
                int cmp = t1.compareTo(t2);
                if (cmp != 0) return cmp;
            }
        }

        return Integer.compare(tokens1.length, tokens2.length);
    }



}

