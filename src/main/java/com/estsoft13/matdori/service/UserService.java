package com.estsoft13.matdori.service;

import com.estsoft13.matdori.domain.User;
import com.estsoft13.matdori.dto.user.UserDto;
import com.estsoft13.matdori.repository.UserRepository;
import com.estsoft13.matdori.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    // 사용자 정보 저장
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword())); // PW는 암호화
        user.setRole(Role.ROLE_BEGINNER); // 초기 등급으로 설정
        userRepository.save(user);
    }

    // 관리자 정보 저장
    public void saveAdmin(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword())); // PW는 암호화
        user.setRole(Role.ROLE_ADMIN); // 관리자 등급으로 설정
        userRepository.save(user);
    }

    // 이메일로 사용자 조회
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }

    // 이메일 및 사용자 이름으로 사용자 조뢰
    public User findByUsernameAndEmail(String email, String username) {
        return userRepository.findByUsernameAndEmail(email, username)
                .orElseThrow(() -> new IllegalArgumentException("User not found by: " + email + " and " + username));
    }

    // 이메일 기반으로 사용자가 비어 있는지 확인
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    // 비밀번호 설정(암호화)
    public void resetPassword(User user, String newPassword) {
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    // 모든 사용자 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 사용자 id로 등급 변경
    public void upgradeRoles(Long userId, Role newRole) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        user.setRole(newRole);
        userRepository.save(user);
    }

    // 계정 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // 아이디로 사용자 조회
    public User findById(Long userId) {
         return userRepository.findById(userId).orElseThrow(
                 () -> new IllegalArgumentException("Invalid user Id:" + userId));
    }

    // 비밀번호 일치 여부 확인
    public boolean checkPassword(User user, String password) {
        return encoder.matches(password, user.getPassword());
    }
}
