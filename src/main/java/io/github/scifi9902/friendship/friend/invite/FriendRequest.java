package io.github.scifi9902.friendship.friend.invite;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FriendRequest {

    private final UUID requester;

    private final long requestedAt;

    /**
     * Constructs a new {@link FriendRequest} instance;
     *
     * @param requester person that requested to be friends
     * @param requestedAt the millis at which it was made
     */
    public FriendRequest(UUID requester, long requestedAt) {
        this.requester = requester;
        this.requestedAt = requestedAt;
    }
}
