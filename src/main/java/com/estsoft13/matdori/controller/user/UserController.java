package com.estsoft13.matdori.controller.user;

import com.estsoft13.matdori.domain.User;
import com.estsoft13.matdori.dto.user.UserDto;
import com.estsoft13.matdori.service.UserService;
import com.estsoft13.matdori.util.GeneratePassword;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인 페이지 조회
    @GetMapping("/login")
    public String login() {
        return "login";   //login.html
    }

    // 로그인 메서드
    @PostMapping("/login")
    public String login( @RequestParam("email") String email,
                        @RequestParam("password") String password) {
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {  // 사용자의 존재 여부 및 비밀번호 일치 여부 확인
            return "redirect:/"; // 리뷰 페이지로 이동
        } else {
            return "redirect:/login"; // 로그인 페이지로 이동
        }
    }

    // 로그아웃 메서드
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login"; // 로그인 페이지로 이동
    }

    // 회원가입 페이지 조회
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signup";    // signup.html
    }

    //회원가입 메서드
    @PostMapping("/signup")
    public String signup(@ModelAttribute("userDto") UserDto userDto, Model model) {
        if (!userService.isEmailUnique(userDto.getEmail())) { // 이메일 중복일 경우 회원가입 페이지로 이동
            model.addAttribute("error", "해당 이메일은 사용중입니다.");
            return "signup";
        } else {
            userService.saveUser(userDto); //이메일 중복이 아닐 경우 로그인 페이지로 이동
            return "login";
        }
    }

    // 비밀번호 찾기 페이지 조회
    @GetMapping("/forgot")
    public String forgot() {
        return "forgot";    // forgot.html
    }

    // 비밀번호 찾기 메서드
    @PostMapping("/forgot")
    public ResponseEntity<?> resetPassword(@RequestParam("username") String username, @RequestParam("email") String email) {
        User user = userService.findByUsernameAndEmail(username, email);
        if (user != null) {
            String newPassword = generateRandomPassword(); //임시 비밀번호 생성
            userService.resetPassword(user, newPassword); //임시 비밀번호으로 초기화
            return ResponseEntity.ok("newPassword=" + newPassword);  //임시 비밀번호 반환
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 정보가 없습니다!"); //에러 메세지 반환
        }
    }

    // 마이페이지 조회
    @GetMapping("/change")
    public String myPage(){
            return "myPage";
    }

    // 비밀번호 변경 메서드
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("password") String password,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {
        String email = principal.getName();
        User user = userService.findByEmail(email);

        if (user != null && userService.checkPassword(user, password)) {
            // 현재 비밀번호가 일치하는 경우, 새로운 비밀번호로 변경 처리
            userService.resetPassword(user, newPassword);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/login";
        } else {
            model.addAttribute("error", "현재 비밀번호가 올바르지 않습니다.");
            return "myPage";
        }
    }

    // 탈퇴하기 메서드
    @PostMapping("/remove")
    public String removeUser(@RequestParam("password") String password, Principal principal, HttpServletRequest request, HttpServletResponse response, Model model) {
        String email = principal.getName(); // 로그인한 사용자의 이메일을 가져옵니다.
        User user = userService.findByEmail(email);

        if (user != null && userService.checkPassword(user, password)) {
            // 현재 로그인한 사용자가 삭제되는 경우 로그아웃 처리
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName().equals(email)) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            userService.deleteUser(user.getId());
            return "redirect:/login";
        } else {
            model.addAttribute("error", "비밀번호가 올바르지 않습니다.");
            return "myPage";
        }
    }

    // 임시 비밀번호 생성 함수(길이 제한 = 8)
    private String generateRandomPassword() {
    return GeneratePassword.generateRandomPassword(8);
    }

}
