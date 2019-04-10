package me.vrekt.fortnitexmpp.party.implementation.member;

import me.vrekt.fortnitexmpp.FortniteXMPP;
import me.vrekt.fortnitexmpp.party.implementation.member.connection.ConnectionType;
import me.vrekt.fortnitexmpp.party.implementation.member.data.ImmutablePartyMemberData;
import me.vrekt.fortnitexmpp.utility.FindPlatformUtility;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import javax.json.JsonObject;

public final class DefaultPartyMember implements PartyMember {

    private String accountId, resource, displayName, connectionType;
    private FortniteXMPP.PlatformType platform;
    private Jid jid;

    private ImmutablePartyMemberData data;

    /**
     * Initializes this member
     *
     * @param object the payload sent from the other member
     */
    DefaultPartyMember(final JsonObject object) {
        final var payload = JsonUtility.getObject("member", object).orElse(object);
        JsonUtility.getString("userId", payload).ifPresent(userId -> accountId = userId);
        JsonUtility.getString("xmppResource", payload).ifPresent(xmppResource -> resource = xmppResource);
        JsonUtility.getString("displayName", payload).ifPresent(displayName -> this.displayName = displayName);
        JsonUtility.getString("connectionType", payload).ifPresent(connectionType -> this.connectionType = connectionType);

        // attempt to get platform.
        platform = FindPlatformUtility.getPlatformForResource(resource);
        this.jid = JidCreate.fromOrThrowUnchecked(accountId + "@" + FortniteXMPP.SERVICE_DOMAIN + "/" + resource);
    }

    /**
     * Initialize this member
     *
     * @param accountId      the account ID
     * @param resource       the XMPP resource. (eg. V2:Fortnite:WIN)
     * @param displayName    the display name
     * @param platformType   the platform
     * @param connectionType the connecion type to use
     */
    DefaultPartyMember(final String accountId, final String resource, final String displayName,
                       final FortniteXMPP.PlatformType platformType, final ConnectionType connectionType) {
        this.accountId = accountId;
        this.resource = resource;
        this.displayName = displayName;
        this.connectionType = connectionType.getName();

        this.platform = platformType;
        this.jid = JidCreate.fromOrThrowUnchecked(accountId + "@" + FortniteXMPP.SERVICE_DOMAIN + "/" + resource);
    }

    @Override
    public String accountId() {
        return accountId;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    /**
     * This method has the possibly to return {@code null}
     *
     * @return the platform or {@code null} if no platform could be found.
     */
    @Override
    public FortniteXMPP.PlatformType platform() {
        return platform;
    }

    @Override
    public String resource() {
        return resource;
    }

    @Override
    public String connectionType() {
        return connectionType;
    }

    @Override
    public Jid user() {
        return jid;
    }

    @Override
    public ImmutablePartyMemberData data() {
        return data;
    }

    @Override
    public void updateData(ImmutablePartyMemberData data) {
        this.data = data;
    }
}
