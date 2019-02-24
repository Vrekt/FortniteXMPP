package me.vrekt.fortnitexmpp.implementation.party.member;

import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import javax.json.JsonObject;

public final class DefaultPartyMember implements PartyMember {

    private String accountId, resource, displayName;
    private PlatformType platform;
    private Jid jid;

    DefaultPartyMember(JsonObject memberData, PlatformType platform) {
        JsonUtility.getString("userId", memberData).ifPresent(userId -> accountId = userId);
        JsonUtility.getString("xmppResource", memberData).ifPresent(xmppResource -> resource = xmppResource);
        JsonUtility.getString("displayName", memberData).ifPresent(displayName -> this.displayName = displayName);

        this.platform = platform;
        this.jid = JidCreate.fromOrThrowUnchecked(accountId + "@prod.ol.epicgames.com/" + resource);
    }

    DefaultPartyMember(String accountId, String resource, String displayName, PlatformType platform) {
        this.accountId = accountId;
        this.resource = resource;
        this.displayName = displayName;

        this.platform = platform;
        this.jid = JidCreate.fromOrThrowUnchecked(accountId + "@prod.ol.epicgames.com/" + resource);
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public PlatformType getPlatform() {
        return platform;
    }

    @Override
    public Jid getJid() {
        return jid;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PartyMember)) return false;
        if (obj == this) return true;
        return ((PartyMember) obj).getAccountId().equals(accountId);
    }
}
