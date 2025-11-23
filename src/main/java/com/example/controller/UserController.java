package com.example.controller;

import com.example.dto.UserDto;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Возвращает всех пользователей
     */
    @Operation(
            summary = "Получить список всех пользователей",
            description = "Возвращает полный список пользователей"
    )
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()
        );
    }

    /**
     * Возвращает пользователя по ID
     */
    @Operation(
            summary = "Получить пользователя по ID",
            description = "Позволяет получить данные конкретного пользователя по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public EntityModel<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"),
                linkTo(methodOn(UserController.class).updateUser(id, user)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete")
        );
    }

    /**
     * Создаёт нового пользователя
     */
    @Operation(
            summary = "Создать нового пользователя",
            description = "Создаёт нового пользователя"
    )
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @PostMapping
    public EntityModel<UserDto> createUser(@RequestBody UserDto dto) {
        UserDto created = userService.createUser(dto);
        return EntityModel.of(created,
                linkTo(methodOn(UserController.class).getUserById(created.getId())).withSelfRel()
        );
    }

    /**
     * Обновляет данные пользователя
     */
    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет данные существующего пользователя по ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/{id}")
    public EntityModel<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        UserDto updated = userService.updateUser(id, dto);
        return EntityModel.of(updated,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel()
        );
    }

    /**
     * Удаляет пользователя по ID
     */
    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по переданному идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
