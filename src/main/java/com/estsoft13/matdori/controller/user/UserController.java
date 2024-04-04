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

    @GetMapping("/login")
    public String login() {
        return "login";   //login.html
    }

    @PostMapping("/login")
    public String login( @RequestParam("email") String email,
                        @RequestParam("password") String password) {
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signup";    // signup.html
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("userDto") UserDto userDto) {
        if (!userService.isEmailUnique(userDto.getEmail())) {
            return "signup";
        } else {
            userService.saveUser(userDto);
            return "login";
        }
    }

    @GetMapping("/forgot")
    public String forgot() {
        return "forgot";    // forgot.html
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> resetPassword(@RequestParam("username") String username, @RequestParam("email") String email) {
        User user = userService.findByUsernameAndEmail(username, email);
        if (user != null) {
            String newPassword = generateRandomPassword();
            userService.resetPassword(user, newPassword);
            return ResponseEntity.ok("newPassword=" + newPassword);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 정보가 없습니다!");
        }
    }

    @GetMapping("/change")
    public String myPage(){
            return "myPage";
    }
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

    private String generateRandomPassword() {
    return GeneratePassword.generateRandomPassword(8);
    }

}
