# FortniteXMPP2
A library for interacting with Fortnite's XMPP service, manage parties, collect presences, chat with users, etc!

# Initialize
```java
// build a Fortnite instance.
// DefaultFortnite.Builder#setKillOtherSessions is optional.
var fortnite = DefaultFortnite.Builder.newInstance(username, password).setKillOtherSessions(true).build();

// Next, build a FortniteXmpp instance with the Fortnite instance.
// For parties, AppType.FORTNITE must be used.
var fortniteXmpp = FortniteXmpp.newBuilder(fortnite).setApplication(AppType.FORTNITE).setPlatform(PlatformType.WIN).build();

// Finally connect!
fortniteXmpp.connect();
```

# Work in progress

TODO: Wiki

# Quick Example
https://gist.github.com/Vrekt/6e2aa1ba0dcffb6e3e4cfef65443d706
