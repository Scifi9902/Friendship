package io.github.scifi9902.friendship.friend;

import io.github.scifi9902.friendship.FriendshipPlugin;
import io.github.scifi9902.friendship.friend.invite.FriendRequest;
import io.github.scifi9902.friendship.profile.Profile;
import io.github.scifi9902.friendship.utilities.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class FriendHandler {

    private final FriendshipPlugin instance;

    public FriendHandler(FriendshipPlugin instance) {
        this.instance = instance;
    }

    public void addFriendRequest(UUID toAdd, FriendRequest friendRequest) {
        Optional<Profile> optional = this.instance.getFriendshipAPI().getProfileHandler().getProfileFromCache(toAdd);

        if (optional.isPresent()) {
            Profile profile = optional.get();

            profile.getFriendRequests().add(friendRequest);

            Player adding = this.instance.getServer().getPlayer(friendRequest.getRequester());
            OfflinePlayer addTo = this.instance.getServer().getOfflinePlayer(toAdd);

            if (addTo == null) {
                return;
            }

            if (adding != null) {
                adding.sendMessage(CC.chat(this.instance.getMessages().getString("friend_request_sent")
                        .replace("%player%", addTo.getName())));

                if (addTo.isOnline()) {
                    addTo.getPlayer().sendMessage(CC.chat(this.instance.getMessages().getString("friend_request_received")
                            .replace("%player%", adding.getName())));
                }

            }
        }

    }

}
