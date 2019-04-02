package me.vrekt.fortnitexmpp.presence.implementation;

import me.vrekt.fortnitexmpp.party.implementation.Party;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.Json;
import java.io.StringReader;
import java.util.Optional;

public final class DefaultFortnitePresence implements FortnitePresence {

    private String accountId, sessionId, status, partyId, partyKey;
    private int playersRemaining, partySize;
    private boolean isPlaying, isValid;
    private final Jid from;

    /**
     * Initialize this presence
     *
     * @param accountId the ID of the account who sent it
     * @param status    the status
     * @param from      who it was sent from
     */
    DefaultFortnitePresence(final String accountId, final String status, final Jid from) {
        this.accountId = accountId;
        this.status = status;
        this.from = from;

        try {
            final var reader = Json.createReader(new StringReader(status));
            final var object = reader.readObject();
            reader.close();

            JsonUtility.getBoolean("bIsPlaying", object).ifPresent(isPlaying -> this.isPlaying = isPlaying);
            JsonUtility.getString("SessionId", object).ifPresent(sessionId -> this.sessionId = sessionId.isEmpty() ? null : sessionId);
            JsonUtility.getObject("Properties", object).ifPresent(properties -> {
                JsonUtility.getInt("ServerPlayerCount_i", properties).ifPresent(playersRemaining -> this.playersRemaining = playersRemaining);
                JsonUtility.getInt("FortPartySize_i", properties).ifPresent(partySize -> this.partySize = partySize);
                JsonUtility.getObject(Party.PARTY_DATA_INFO, properties).ifPresent(party -> {
                    JsonUtility.getString("partyId", party).ifPresent(partyId -> this.partyId = partyId);
                    JsonUtility.getString("key", party).ifPresent(partyKey -> this.partyKey = partyKey);
                });
            });

        } catch (final Exception exception) {
            isValid = false;
        }

    }

    @Override
    public String accountId() {
        return accountId;
    }

    @Override
    public Optional<String> sessionId() {
        return Optional.ofNullable(sessionId);
    }

    @Override
    public Optional<String> partyId() {
        return Optional.ofNullable(partyId);
    }

    @Override
    public Optional<String> accessKey() {
        return Optional.ofNullable(partyKey);
    }

    @Override
    public String status() {
        return status;
    }

    @Override
    public int playersRemaining() {
        return playersRemaining;
    }

    @Override
    public int partySize() {
        return partySize;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public Jid getFrom() {
        return from;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }
}
