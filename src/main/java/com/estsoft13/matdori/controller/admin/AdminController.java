package com.estsoft13.matdori.controller.admin;

import com.estsoft13.matdori.domain.User;
import com.estsoft13.matdori.dto.user.UserDto;
import com.estsoft13.matdori.service.AdminService;
import com.estsoft13.matdori.service.UserService;
import com.estsoft13.matdori.util.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    @GetMapping("/new")
    public String adminSignup(Model model){
        model.addAttribute("userDto", new UserDto());
        return "new";
    }

    @PostMapping("/new")
    public String adminSignup(@ModelAttribute("userDto") UserDto userDto) {
        if (!userService.isEmailUnique(userDto.getEmail())) {
            return "new";
        } else {
            userService.saveAdmin(userDto);
            return "login";
        }
    }

    @GetMapping("/manage")
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers();
        List<UserDto> associate = new ArrayList<>();
        List<UserDto> member = new ArrayList<>();

        boolean hasEligibleUser = false;
        for (User user : users) {
            // 코멘트 수에 따라 사용자 등급을 업데이트하는 로직
            if (adminService.isEligibleForAssociate(user)) {
                // 예를 들어, 등급 상승 로직
                associate.add(user.toDto());
                hasEligibleUser = true;
            } else if (adminService.isEligibleForMember(user)) {
                member.add(user.toDto());
                hasEligibleUser = true;
            }
        }

        if (!hasEligibleUser) {
            model.addAttribute("message", "승급할 사용자가 없습니다.");
        }
        model.addAttribute("associates", associate);
        model.addAttribute("members", member);
        model.addAttribute("users", users);
        return "manage";
    }

    @PostMapping("/manage")
    public String upgradeRoles(@RequestParam Long userId, @RequestParam Role newRole) {
        userService.upgradeRoles(userId, newRole);
        return "redirect:/admin/manage";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName().equals(userService.findById(userId).getEmail())) {
            // 현재 로그인한 사용자가 삭제되는 경우 로그아웃 처리
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        userService.deleteUser(userId);
        return "redirect:/admin/manage";
    }
}


