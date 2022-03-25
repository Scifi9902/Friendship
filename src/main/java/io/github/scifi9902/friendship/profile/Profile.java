package io.github.scifi9902.friendship.profile;

import com.google.gson.annotations.SerializedName;
import io.github.scifi9902.friendship.friend.Friend;
import io.github.scifi9902.friendship.friend.invite.FriendRequest;
import lombok.Getter;

import java.util.*;

@Getter
public class Profile {

    @SerializedName("_id")
    private final UUID uniqueId;

    private final List<Friend> friends;

    private final List<FriendRequest> friendRequests;

    /**
     * Constructs a new {@link Profile} instance
     *
     * @param uniqueId uniqueId of the player
     */
    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
    }

    /**
     * Adds an {@link  FriendRequest} to the collection
     *
     * @param request the {@link FriendRequest} to add
     */
    public void addFriendRequest(FriendRequest request) {
        if (request == null) {
            return;
        }

        this.friendRequests.add(request);
        this.friendRequests.sort(Comparator.comparingLong(FriendRequest::getRequestedAt));
    }

    /**
     * Removes an {@link FriendRequest} from the collection
     *
     * @param request the {@link FriendRequest} to remove
     */
    public void removeFriendRequest(FriendRequest request) {
        if (request == null) {
            return;
        }

        this.friendRequests.remove(request);
        this.friendRequests.sort(Comparator.comparingLong(FriendRequest::getRequestedAt));
    }


    /**
     * @param requester person that requested to be friends
     * @return an Optional of type FriendRequest
     */
    public Optional<FriendRequest> getFriendRequest(UUID requester) {
        for (FriendRequest friendRequest : this.friendRequests) {
            if (friendRequest.getRequester().equals(requester)) {
                return Optional.of(friendRequest);
            }
        }
        return Optional.empty();
    }

    /**
     * Add an {@link Friend} to the collection
     *
     * @param friend the friend to add
     */
    public void addFriend(Friend friend) {
        if (friend == null) {
            return;
        }

        this.friends.add(friend);
        this.friends.sort(Comparator.comparingLong(Friend::getFriendsSince).reversed());
    }

    public void removeFriend(Friend friend) {
        if (friend == null) {
            return;
        }

        this.friends.remove(friend);
        this.friends.sort(Comparator.comparingLong(Friend::getFriendsSince).reversed());
    }
}
