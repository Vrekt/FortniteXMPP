package me.vrekt.fortnitexmpp.implementation.service.party;

import me.vrekt.fortnitexmpp.FortniteXmpp;
import me.vrekt.fortnitexmpp.implementation.PlatformType;
import me.vrekt.fortnitexmpp.implementation.party.Party;
import me.vrekt.fortnitexmpp.implementation.party.PartyListener;
import me.vrekt.fortnitexmpp.implementation.party.event.join.PartyJoinRequestApprovedEvent;
import me.vrekt.fortnitexmpp.implementation.party.member.data.PartyMemberData;
import me.vrekt.fortnitexmpp.implementation.party.packet.authentication.client.PacketPartyJoinAcknowledged;
import me.vrekt.fortnitexmpp.implementation.party.packet.authentication.client.PacketPartyJoinRequest;
import me.vrekt.fortnitexmpp.implementation.party.packet.member.PacketPartyMemberData;
import org.jxmpp.jid.Jid;

/**
 * Handles data used for join a party
 */
public final class PartyJoinData {

    private final Party party;
    private final PlatformType platformType;
    private final PartyMemberData data;

    public PartyJoinData(Party party, PlatformType platformType, PartyMemberData data) {
        this.party = party;
        this.platformType = platformType;
        this.data = data;
    }

    /**
     * Accept the party invite and join the party
     *
     * @param service      the service
     * @param fortniteXmpp the xmpp instance
     */
    void accept(DefaultPartyService service, FortniteXmpp fortniteXmpp) {
        service.addPartyListener(new PartyListener() {
            @Override
            public void onJoinRequestApproved(PartyJoinRequestApprovedEvent event) {
                service.sendPacket(new PacketPartyJoinAcknowledged(party));
            }

            @Override
            public void onJoinAcknowledgedResponse(Party party, Jid from) {
                service.sendPacket(new PacketPartyMemberData(party, data));
                service.removePartyListener(this);
            }
        });
        service.sendPacket(new PacketPartyJoinRequest(party, fortniteXmpp.getDisplayName(), platformType));
    }

}
