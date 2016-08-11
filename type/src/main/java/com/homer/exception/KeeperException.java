package com.homer.exception;

/**
 * Created by arigolub on 8/15/16.
 */
public abstract class KeeperException extends RuntimeException {

    public KeeperException(String message) {
        super(message);
    }

    public static class MajorLeagueCountExceeded extends KeeperException {
        public MajorLeagueCountExceeded(String message) {
            super(message);
        }
    }

    public static class MinorLeagueCountExceeded extends KeeperException {
        public MinorLeagueCountExceeded(String message) {
            super(message);
        }
    }

    public static class InsufficientFunds extends KeeperException {
        public InsufficientFunds(String message) {
            super(message);
        }
    }

    public static class NoKeeperYearsRemaining extends KeeperException {
        public NoKeeperYearsRemaining(String message) { super(message); }
    }

    public static class IncorrectTeam extends KeeperException {
        public IncorrectTeam(String message) { super(message); }
    }

    public static class IneligibleMinorLeaguer extends KeeperException {
        public IneligibleMinorLeaguer(String message) { super(message); }
    }
}