package com.yash.enotes.service.IUserService;

import com.yash.enotes.entity.User;

public interface UserService {
    User getUser(Integer id);
    User createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Integer id);
}
