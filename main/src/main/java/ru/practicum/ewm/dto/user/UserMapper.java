package ru.practicum.ewm.dto.user;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.user.User;

@UtilityClass
public class UserMapper {

    public static User toUser(NewUserRequestDto newUserRequestDto) {
        return new User(
                null,
                newUserRequestDto.getEmail(),
                newUserRequestDto.getName()
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
