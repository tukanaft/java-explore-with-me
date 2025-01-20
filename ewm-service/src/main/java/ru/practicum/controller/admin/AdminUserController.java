package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.users.NewUserRequest;
import ru.practicum.dto.users.UserDto;
import ru.practicum.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody NewUserRequest userRequest) {
        log.info("UserController выполнение запроса на добавление пользователя");
        return userService.addUser(userRequest);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Integer> ids, @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.info("UserController выполнение запроса на отправление информации о пользователях пользователей");
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("UserController выполнение запроса на удаление пользователя: {}", userId);
        userService.deleteUser(userId);
    }
}
