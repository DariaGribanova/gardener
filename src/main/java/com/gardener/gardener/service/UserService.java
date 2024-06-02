package com.gardener.gardener.service;

import com.gardener.gardener.dto.PlantCultureDto;
import com.gardener.gardener.dto.UserDto;
import com.gardener.gardener.dto.response.GardenResponseDto;
import com.gardener.gardener.entity.Garden;
import com.gardener.gardener.entity.PlantCulture;
import com.gardener.gardener.entity.User;
import com.gardener.gardener.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + username));
        return mapToDto(user);
    }

    public List<GardenResponseDto> getGardensByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + username));
        return convertToDtoList(user.getGardens());
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
        return userDTO;
    }

    private GardenResponseDto mapToGardenDto(Garden garden) {
        GardenResponseDto gardenResponseDto = new GardenResponseDto();
        gardenResponseDto.setId(garden.getId());
        gardenResponseDto.setName(garden.getName());
        gardenResponseDto.setRegion(garden.getRegion());
        return gardenResponseDto;
    }

    private List<GardenResponseDto> convertToDtoList(List<Garden> gardens) {
        return gardens.stream()
                .map(this::mapToGardenDto)
                .collect(Collectors.toList());
    }

    private User mapToEntity(UserDto userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUserName());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        return user;
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username){
                return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        return userRepository.save(user);
    }

}