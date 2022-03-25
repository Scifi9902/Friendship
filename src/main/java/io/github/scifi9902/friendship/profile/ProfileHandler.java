package io.github.scifi9902.friendship.profile;

import io.github.scifi9902.friendship.FriendshipPlugin;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileHandler {

    private final Map<UUID, Profile> profileMap = new ConcurrentHashMap<>();

    private final FriendshipPlugin instance;

    /**
     * Constructs a new {@link ProfileHandler} instance
     *
     * @param instance instance of the plugin
     */
    public ProfileHandler(FriendshipPlugin instance) {
        this.instance = instance;
    }

    public Optional<Profile> getProfileFromCache(UUID uniqueId) {
        if (profileMap.containsKey(uniqueId)) {
            return Optional.of(profileMap.get(uniqueId));
        }
        return Optional.empty();
    }


}
