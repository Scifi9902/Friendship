package io.github.scifi9902.friendship.friend;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Friend {

    private UUID friendUUID;

    private long friendsSince;

    /**
     * Constructs a new {@link Friend} instance
     *
     * @param friendUUID UUID of the person they are friends with
     * @param friendsSince the millis of when they became friends
     */
    public Friend(UUID friendUUID, long friendsSince) {
        this.friendUUID = friendUUID;
        this.friendsSince = friendsSince;
    }
}
