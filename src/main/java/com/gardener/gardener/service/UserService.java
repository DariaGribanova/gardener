package com.gardener.gardener.service;

import com.gardener.gardener.dto.UserDto;
import com.gardener.gardener.entity.User;
import com.gardener.gardener.entity.UserRole;
import com.gardener.gardener.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return mapToDto(user);
    }

    public UserDto createUser(UserDto userDTO) {
        User user = mapToEntity(userDTO);
        user = userRepository.save(user);
        return mapToDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDTO) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        User updatedUser = mapToEntity(userDTO);
        updatedUser.setId(id);
        updatedUser = userRepository.save(updatedUser);
        return mapToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDto mapToDto(User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setUserName(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User mapToEntity(UserDto userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(UserRole.MODER);
        userRepository.save(user);
    }
    public User getByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }
    public User create(User user) {
        if (userRepository.existsByUserName(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        return userRepository.save(user);
    }

}
