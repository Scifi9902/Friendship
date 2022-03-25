package io.github.scifi9902.friendship.profile.listener;

import io.github.scifi9902.friendship.FriendshipPlugin;
import io.github.scifi9902.friendship.profile.Profile;
import io.github.scifi9902.friendship.redis.action.ActionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Optional;
import java.util.UUID;

public class ProfileListener implements Listener {

    private final FriendshipPlugin instance;

    public ProfileListener(FriendshipPlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        Optional<Profile> optional = this.instance.getFriendshipAPI().getProfileHandler().getProfileFromCache(uuid);

        if (optional.isPresent()) {
            return;
        }

        this.instance.getFriendshipAPI().getProfileHandler().getProfileFromDatabase(uuid)
                .whenCompleteAsync((profile, throwable) -> {
                   if (profile == null) {
                       Profile generatedProfile = new Profile(uuid);
                       this.instance.getRedisHandler().publish(ActionType.PROFILE_ADD, generatedProfile);
                       this.instance.getFriendshipAPI().getProfileHandler().saveToDatabase(generatedProfile);
                   } else {
                       this.instance.getRedisHandler().publish(ActionType.PROFILE_ADD, profile);
                   }
                });
    }
}
