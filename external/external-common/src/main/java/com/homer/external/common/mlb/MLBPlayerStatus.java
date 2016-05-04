package com.homer.external.common.mlb;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 4/20/16.
 */
public enum MLBPlayerStatus {
    ACTIVE("Active", "A", null),
    MINORS("Minors", "MIN", null),
    DISABLEDLIST("Disabled List", "DL", null),
    FREEAGENT("Free Agent", "FA", null),
    UNKNOWN("Unknown", "UNK", null),
    INACTIVE("Inactive", "I", null),
    DL15("15-Day Disabled List", "D15", DISABLEDLIST),
    DL50("60-Day Disabled List", "D60", DISABLEDLIST),
    REASSIGNED("Re-assigned", "RM", MINORS),
    RESTRICTED("Restricted", "RST", DISABLEDLIST),
    SUSPENDED("Suspended", "SUSP", DISABLEDLIST),
    BEREAVEMENT("Bereavement", "BRV", ACTIVE),
    NONROSTERINVITEE("Non-Roster Invitee", "NRI", MINORS),
    ;

    private final String fullName;
    private final String abbreviation;
    @Nullable
    private final MLBPlayerStatus parentStatus;

    private MLBPlayerStatus(String fullName, String abbreviation, @Nullable MLBPlayerStatus parentStatus) {
        this.fullName = fullName;
        this.abbreviation = abbreviation;
        this.parentStatus = parentStatus;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    @Nullable
    public MLBPlayerStatus getParentStatus() {
        return parentStatus;
    }

    @Nullable
    public static MLBPlayerStatus fromStatusCode(String statusCode) {
        for (MLBPlayerStatus status : MLBPlayerStatus.class.getEnumConstants())
        {
            if (statusCode.equals(status.getAbbreviation())) {
                return status.getParentStatus() != null? status.getParentStatus()
                        : status;
            }
        }
        return null;
    }
}
