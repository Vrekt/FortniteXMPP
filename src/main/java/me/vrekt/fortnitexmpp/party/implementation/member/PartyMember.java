package me.vrekt.fortnitexmpp.party.implementation.member;

import me.vrekt.fortnitexmpp.party.implementation.member.connection.ConnectionType;
import me.vrekt.fortnitexmpp.party.implementation.member.data.ImmutablePartyMemberData;
import me.vrekt.fortnitexmpp.type.PlatformType;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public interface PartyMember {

    /**
     * Create a new instance of {@link PartyMember}
     *
     * @param payload the payload
     * @return a new {@link PartyMember}
     */
    static PartyMember newMember(final JsonObject payload) {
        return new DefaultPartyMember(payload);
    }

    /**
     * Create a new instance of {@link PartyMember}
     *
     * @param accountId      the account ID
     * @param resource       the XMPP resource. (eg. V2:Fortnite:WIN)
     * @param displayName    the display name
     * @param platformType   the platform
     * @param connectionType the connection type to use
     * @return a new {@link PartyMember}
     */
    static PartyMember newMember(final String accountId, final String resource, final String displayName,
                                 final PlatformType platformType, final ConnectionType connectionType) {
        return new DefaultPartyMember(accountId, resource, displayName, platformType, connectionType);
    }

    /**
     * @return the account ID of this {@link PartyMember}
     */
    String accountId();

    /**
     * @return the display name of this {@link PartyMember}
     */
    String displayName();

    /**
     * @return the current platform this {@link PartyMember} is on.
     */
    PlatformType platform();

    /**
     * @return the XMPP resource of this {@link PartyMember}
     */
    String resource();

    /**
     * @return the connection type of this member
     */
    String connectionType();

    /**
     * @return the JID of this {@link PartyMember}
     */
    Jid user();

    /**
     * @return the data of this member, their cosmetic loadout, ready status, etc.
     */
    ImmutablePartyMemberData data();

    /**
     * Set the data of this member
     *
     * @param data the data
     */
    void updateData(ImmutablePartyMemberData data);

}
