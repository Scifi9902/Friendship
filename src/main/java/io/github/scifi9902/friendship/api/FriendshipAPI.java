package io.github.scifi9902.friendship.api;

import io.github.scifi9902.friendship.FriendshipPlugin;
import io.github.scifi9902.friendship.profile.ProfileHandler;
import lombok.Getter;

public class FriendshipAPI {

    @Getter
    private static FriendshipAPI instance;

    private ProfileHandler profileHandler;

    public FriendshipAPI() {
        instance = this;
    }

    public void setProfileHandler(ProfileHandler profileHandler) {
        if (this.profileHandler != null) {
            throw new IllegalStateException("The ProfileHandler is already initialised");
        }

        if (profileHandler == null) {
            throw new NullPointerException("The ProfileHandler instance you are passing is null.");
        }

        this.profileHandler = profileHandler;
    }

    public ProfileHandler getProfileHandler() {
        if (this.profileHandler == null) {
            throw new NullPointerException("The ProfileHandler has not been initialized");
        }
        return this.profileHandler;
    }


}
