package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.user.NewUserRequestDto;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.user.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {

    private final UserService userService;

    /*
     * Получение информации о пользователях
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     * В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список
     * @param ids - id пользователей
     * @param from - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param int - количество элементов в наборе
     * Возвращает List<UserDto> - список пользователей, либо код 400
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size);
    }

    /*
     * Добавление нового пользователя
     * @param newUserRequestDto - данные добавляемого пользователя
     * Возвращает UserDto - нового пользователя, код 400 (запрос составлен некорректно),
     * либо 409 (нарушение целостности данных)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Validated @RequestBody NewUserRequestDto newUserRequestDto) {
        return userService.addUser(newUserRequestDto);
    }

    /*
     * Удаление пользователя
     * @param userId - id пользователя, которого нужно удалить
     * Возвращает 200 при успешном удалении, либо код 404 (пользователь не найден или недоступен)
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId)       {
        userService.deleteUser(userId);
    }

}
