package me.vrekt.fortnitexmpp.party.implementation.playlist;

public enum StandardPlaylists {

    SOLO("Playlist_DefaultSolo"), DUO("Playlist_DefaultDuo"), SQUAD("Playlist_DefaultSquad"), CREATIVE("Playlist_PlaygroundV2");

    private final String name;

    StandardPlaylists(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
