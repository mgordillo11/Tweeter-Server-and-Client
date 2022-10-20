package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public abstract class Presenter {
    protected UserService userService;
    protected FollowService followService;
    protected StatusService statusService;

    public Presenter() {
        this.userService = new UserService();
        this.followService = new FollowService();
        this.statusService = new StatusService();
    }
}
