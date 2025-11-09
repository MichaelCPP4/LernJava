package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers()
    {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDto(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAge(),
                        u.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id)
    {
        return userRepository.findById(id)
                .map(u -> new UserDto(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getAge(),
                        u.getCreatedAt()))
                .orElse(null);
    }

    public UserDto createUser(UserDto dto)
    {
        UserEntity entity = new UserEntity(
                dto.getUsername(),
                dto.getEmail(),
                dto.getAge());

        UserEntity saved = userRepository.save(entity);

        return new UserDto(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getAge(),
                saved.getCreatedAt());
    }

    public UserDto updateUser(Long id, UserDto dto)
    {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getUsername());
                    existing.setEmail(dto.getEmail());
                    existing.setAge(dto.getAge());
                    UserEntity updated = userRepository.save(existing);
                    return new UserDto(
                            updated.getId(),
                            updated.getUsername(),
                            updated.getEmail(),
                            updated.getAge(),
                            updated.getCreatedAt());
                })
                .orElse(null);
    }

    public void deleteUser(Long id)
    {
        userRepository.deleteById(id);
    }
}
