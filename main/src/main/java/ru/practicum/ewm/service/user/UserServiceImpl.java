package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.NewUserRequestDto;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.user.UserRepository;
import ru.practicum.ewm.util.PageRequestUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends PageRequestUtil implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable page = createPageRequest(from, size);
        List<UserDto> users;
        if (ids != null) {
            users = userRepository.findByUserIds(ids, page)
                    .getContent()
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findAll(page)
                    .getContent()
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public UserDto addUser(NewUserRequestDto newUserRequestDto) {
        User user = userRepository.save(UserMapper.toUser(newUserRequestDto));
        log.info("Created new user: user = {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.getExistingUser(userId);
        userRepository.delete(user);
        log.info("Deleted user with ID = {}", user.getId());
    }
}
