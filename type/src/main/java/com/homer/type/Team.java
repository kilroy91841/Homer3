package com.homer.type;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/14/16.
 */
@Table(name = "teams", schema = "homer")
public class Team extends BaseObject {

    @Column
    private String name;
    @Column
    private String abbreviation;
    @Column
    private String owner1;
    @Nullable
    @Column
    private String owner2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Team team = (Team) o;

        if (name != null ? !name.equals(team.name) : team.name != null) return false;
        if (abbreviation != null ? !abbreviation.equals(team.abbreviation) : team.abbreviation != null) return false;
        if (owner1 != null ? !owner1.equals(team.owner1) : team.owner1 != null) return false;
        return owner2 != null ? owner2.equals(team.owner2) : team.owner2 == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (owner1 != null ? owner1.hashCode() : 0);
        result = 31 * result + (owner2 != null ? owner2.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getOwner1() {
        return owner1;
    }

    public void setOwner1(String owner1) {
        this.owner1 = owner1;
    }

    @Nullable
    public String getOwner2() {
        return owner2;
    }

    public void setOwner2(@Nullable String owner2) {
        this.owner2 = owner2;
    }
}
