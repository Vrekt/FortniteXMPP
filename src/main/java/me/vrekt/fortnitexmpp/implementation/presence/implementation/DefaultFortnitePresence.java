package me.vrekt.fortnitexmpp.implementation.presence.implementation;

import me.vrekt.fortnitexmpp.implementation.presence.FortnitePresence;
import me.vrekt.fortnitexmpp.utility.JsonUtility;
import org.jxmpp.jid.Jid;

import javax.json.Json;
import java.io.StringReader;
import java.util.Optional;

public final class DefaultFortnitePresence implements FortnitePresence {

    private String accountId, sessionId, status, partyId, partyKey;
    private int playersRemaining, partySize;
    private boolean isPlaying;
    private Jid from;

    public DefaultFortnitePresence(String accountId, String status, Jid from) {
        this.accountId = accountId;
        this.from = from;
        parse(status);
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public Optional<String> getSessionId() {
        return Optional.ofNullable(sessionId);
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getPlayersRemaining() {
        return playersRemaining;
    }

    @Override
    public int getPartySize() {
        return partySize;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public Optional<String> getPartyId() {
        return Optional.ofNullable(partyId);
    }

    @Override
    public Optional<String> getAccessKey() {
        return Optional.ofNullable(partyKey);
    }

    @Override
    public Jid getFrom() {
        return from;
    }

    private void parse(String status) {
        this.status = status;

        try {
            var reader = Json.createReader(new StringReader(status));
            var object = reader.readObject();
            reader.close();

            final var partyJoinData = "party.joininfodata.286331153_j";
            JsonUtility.getBoolean("bIsPlaying", object).ifPresent(isPlaying -> this.isPlaying = isPlaying);
            JsonUtility.getString("SessionId", object).ifPresent(sessionId -> {
                if (!sessionId.isEmpty()) this.sessionId = sessionId;
            });
            JsonUtility.getObject("Properties", object).ifPresent(properties -> {
                JsonUtility.getInt("ServerPlayerCount_i", properties).ifPresent(playersRemaining -> this.playersRemaining = playersRemaining);
                JsonUtility.getInt("FortPartySize_i", properties).ifPresent(partySize -> this.partySize = partySize);
                JsonUtility.getObject(partyJoinData, properties).ifPresent(party -> {
                    JsonUtility.getString("partyId", party).ifPresent(partyId -> this.partyId = partyId);
                    JsonUtility.getString("key", party).ifPresent(partyKey -> this.partyKey = partyKey);
                });
            });
        } catch (Exception exception) {
            //
        }
    }
}
