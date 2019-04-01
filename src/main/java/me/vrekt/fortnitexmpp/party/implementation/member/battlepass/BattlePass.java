package me.vrekt.fortnitexmpp.party.implementation.member.battlepass;

public final class BattlePass {

    private boolean hasPurchased;
    private int passLevel, selfBoostXp, friendBoostXp;

    public boolean hasPurchased() {
        return hasPurchased;
    }

    public BattlePass setHasPurchased(boolean hasPurchased) {
        this.hasPurchased = hasPurchased;
        return this;
    }

    public int getPassLevel() {
        return passLevel;
    }

    public BattlePass setPassLevel(int passLevel) {
        this.passLevel = passLevel;
        return this;
    }

    public int getSelfBoostXp() {
        return selfBoostXp;
    }

    public BattlePass setSelfBoostXp(int selfBoostXp) {
        this.selfBoostXp = selfBoostXp;
        return this;
    }

    public int getFriendBoostXp() {
        return friendBoostXp;
    }

    public BattlePass setFriendBoostXp(int friendBoostXp) {
        this.friendBoostXp = friendBoostXp;
        return this;
    }
}
