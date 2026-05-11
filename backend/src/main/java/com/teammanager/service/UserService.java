package com.teammanager.service;

import com.teammanager.dto.UserResponse;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}
