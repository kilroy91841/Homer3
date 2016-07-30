package com.homer.service.utility;

import com.homer.external.common.espn.ESPNTransaction;
import com.homer.type.Position;
import com.homer.type.TransactionType;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 7/29/16.
 */
public final class ESPNUtility {

    @Nullable
    public static Position translateESPNPosition(String positionText) {
        if (positionText == null) {
            return null;
        }
        switch (positionText) {
            case "C":
                return Position.CATCHER;
            case "1B":
                return Position.FIRSTBASE;
            case "2B":
                return Position.SECONDBASE;
            case "3B":
                return Position.THIRDBASE;
            case "SS":
                return Position.SHORTSTOP;
            case "2B/SS":
                return Position.MIDDLEINFIELD;
            case "1B/3B":
                return Position.CORNERINFIELD;
            case "OF":
                return Position.OUTFIELD;
            case "UTIL":
                return Position.UTILITY;
            case "P":
                return Position.PITCHER;
            case "DL":
                return Position.DISABLEDLIST;
            default:
                return null;
        }
    }

    @Nullable
    public static TransactionType translateESPNTransactionType(ESPNTransaction.Type type) {
        switch (type) {
            case ADD:
                return TransactionType.ADD;
            case DROP:
                return TransactionType.DROP;
            case MOVE:
                return TransactionType.MOVE;
            case TRADE:
                return TransactionType.TRADE;
            default:
                return TransactionType.UNKNOWN;
        }

    }
}
