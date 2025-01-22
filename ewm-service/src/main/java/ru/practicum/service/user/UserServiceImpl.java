package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.dto.users.NewUserRequest;
import ru.practicum.dto.users.UserDto;
import ru.practicum.dto.users.UserMapper;
import ru.practicum.exception.ConflictExeption;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.users.User;
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
        userValidation(userRequest);
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        log.info("userService: отправление информации о пользователях");
        return userMapper.toUserDtoList(userRepository.findAllByIdIn(ids, PageRequest.of(from,size)));
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("userService: удвление пользователя");
        userRepository.deleteById(userId);
    }

    private void userValidation(NewUserRequest user) {
        if (user.getName() == null || user.getName().length() < 2 || user.getName().length() > 250 || user.getName().isBlank()){
            throw new ValidationException("не корректно имя");
        }
        if (user.getEmail() == null || user.getEmail().length() < 6 || user.getEmail().length() > 254){
            throw new ValidationException("не корректный имейл");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("не корректный email");
        }
        if (user.getEmail().split("@")[0].length() > 64 ) {
            throw new ValidationException("не корректный email");
        }
        for (User userToCompare : userRepository.findAll()) {
            if (userToCompare.getEmail().equals(user.getEmail())) {
                throw new ConflictExeption("пользователь с таким имейлом уже существует");
            }
        }
    }
}
