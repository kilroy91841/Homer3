package com.homer.type;

import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 7/25/16.
 */
@Table(name = "transactions", schema = "homer")
public class Transaction extends BaseObject implements Comparable<Transaction> {

    @Column(updatable = false)
    private long playerId;

    @Column(updatable = false)
    private long teamId;

    @Column(updatable = false)
    private TransactionType transactionType;

    @Column(updatable = false)
    private DateTime transactionDate;

    @Column(updatable = false)
    private String text;

    @Nullable
    @Column(updatable = false)
    private Position oldPosition;

    @Nullable
    @Column(updatable =  false)
    private Position newPosition;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Transaction that = (Transaction) o;
        return playerId == that.playerId &&
                teamId == that.teamId &&
                transactionType == that.transactionType &&
                Objects.equal(transactionDate, that.transactionDate) &&
                Objects.equal(text, that.text) &&
                oldPosition == that.oldPosition &&
                newPosition == that.newPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), playerId, teamId, transactionType, transactionDate, text, oldPosition, newPosition);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "playerId=" + playerId +
                ", teamId=" + teamId +
                ", transactionType=" + transactionType +
                ", transactionDate=" + transactionDate +
                ", text='" + text + '\'' +
                ", oldPosition=" + oldPosition +
                ", newPosition=" + newPosition +
                "} " + super.toString();
    }

    // endregion

    // region getters + setters

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public DateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(DateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Nullable
    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(@Nullable Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    @Nullable
    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(@Nullable Position newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public int compareTo(Transaction o) {
        if (this.getTransactionDate().isBefore(o.getTransactionDate())) {
            return -1;
        }
        if (this.getTransactionDate().isAfter(o.getTransactionDate())) {
            return 1;
        }
        if (this.getTransactionType().getId() < o.getTransactionType().getId()) {
            return -1;
        }
        if (this.getTransactionType().getId() > o.getTransactionType().getId()) {
            return 1;
        }
        return 0;
    }

    // endregion
}
