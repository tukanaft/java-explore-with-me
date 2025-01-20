package ru.practicum.dto.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.users.User;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public User toUser(NewUserRequest userRequest) {
        return new User(
                null,
                userRequest.getName(),
                userRequest.getEmail()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(new UserDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            ));
        }
        return usersDto;
    }

    public UserShortDto toUserShortDto(User user){
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
