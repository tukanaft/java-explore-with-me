package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.dto.users.NewUserRequest;
import ru.practicum.dto.users.UserDto;
import ru.practicum.dto.users.UserMapper;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final UserMapper userMapper;

    @Override
    public UserDto addUser(NewUserRequest userRequest) {
        log.info("userService: добавление пользователя");
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        log.info("userService: отправление информации о пользователях");
        return userMapper.toUserDtoList(userRepository.findUsers(ids, from, size));
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("userService: удвление пользователя");
        userRepository.deleteById(userId);
    }
}
