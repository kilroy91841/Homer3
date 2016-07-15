package com.homer.exception;

/**
 * Created by arigolub on 5/21/16.
 */
public abstract class FreeAgentAuctionBidException extends Exception {

    public FreeAgentAuctionBidException(String message) {
        super(message);
    }

    public static class AuctionNotInProgress extends FreeAgentAuctionBidException {
        public AuctionNotInProgress(String message) {
            super(message);
        }
    }

    public static class InsufficientFunds extends FreeAgentAuctionBidException {
        public InsufficientFunds(String message) {
            super(message);
        }
    }

    public static class BadAuctionRequest extends FreeAgentAuctionBidException {
        public BadAuctionRequest(String message) {
            super(message);
        }
    }
}
