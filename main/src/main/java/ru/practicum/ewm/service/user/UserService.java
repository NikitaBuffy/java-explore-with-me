package ru.practicum.ewm.service.user;

import ru.practicum.ewm.dto.user.NewUserRequestDto;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto addUser(NewUserRequestDto newUserRequestDto);

    void deleteUser(Long userId);
}
