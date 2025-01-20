package ru.practicum.service.user;

import ru.practicum.dto.users.NewUserRequest;
import ru.practicum.dto.users.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(NewUserRequest userRequest);

    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);

    void deleteUser(Integer userId);
}
