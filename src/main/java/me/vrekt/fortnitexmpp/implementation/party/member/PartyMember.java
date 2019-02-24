package me.vrekt.fortnitexmpp.implementation.party.member;

import me.vrekt.fortnitexmpp.implementation.PlatformType;
import org.jxmpp.jid.Jid;

import javax.json.JsonObject;

public interface PartyMember {

    String getAccountId();

    String getDisplayName();

    PlatformType getPlatform();

    String getResource();

    Jid getJid();

    static Builder newBuilder(Type type) {
        return new Builder(type);
    }

    final class Builder {
        private final Type buildType;
        private String accountId, resource, displayName;
        private PlatformType platform;

        private JsonObject memberData;

        private Builder(Type type) {
            this.buildType = type;
        }

        public Builder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder setResource(String resource) {
            this.resource = resource;
            return this;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setPlatform(PlatformType platform) {
            this.platform = platform;
            return this;
        }

        public Builder setMemberData(JsonObject memberData) {
            this.memberData = memberData;
            return this;
        }

        public PartyMember build() {
            return buildType == Type.WITH_PAYLOAD ? new DefaultPartyMember(memberData, PlatformType.WIN) : new DefaultPartyMember(accountId, resource, displayName, platform);
        }

    }

    enum Type {
        /**
         * WITH_PAYLOAD = another member has sent their data, and its in a JsonObject.
         * NORMAL = build yourself, you set all properties.
         */
        WITH_PAYLOAD, NORMAL
    }

}