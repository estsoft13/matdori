package com.estsoft13.matdori.controller.meeting;

import com.estsoft13.matdori.domain.Meeting;
import com.estsoft13.matdori.domain.Restaurant;
import com.estsoft13.matdori.domain.User;
import com.estsoft13.matdori.dto.comment.CommentResponseDto;
import com.estsoft13.matdori.dto.meeting.MeetingResponseDto;
import com.estsoft13.matdori.repository.CommentRepository;
import com.estsoft13.matdori.service.CommentService;
import com.estsoft13.matdori.service.MeetingService;
import com.estsoft13.matdori.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MeetingPageController {
    private final MeetingService meetingService;
    private final RestaurantService restaurantService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;


    @GetMapping("/add-meeting")
    public String showCreateMeetingForm(@RequestParam(required = false) Long meetingId, Model model) {
        List<Restaurant> restaurants = restaurantService.findAll();
        model.addAttribute("restaurants", restaurants);

        if (meetingId == null) {
            model.addAttribute("meeting", new Meeting());
        } else {
            MeetingResponseDto meeting = meetingService.findById(meetingId);
            model.addAttribute("meeting", meeting);
        }

        return "meetingForm";
    }

    @GetMapping("/meeting/{meetingId}")
    public String showMeetingDetail(@PathVariable Long meetingId, Model model, @AuthenticationPrincipal User user) {
        //Review review = reviewService.findById(reviewId);

        try {
            MeetingResponseDto responseDto = meetingService.findById(meetingId);
            model.addAttribute("meeting", responseDto);

            // 현재 로그인한 유저가 글을 등록한 유저인지 확인 후 글을 수정할 수 있게끔
            Long userId = user.getId();
            boolean isOwner = responseDto.getUser_id().equals(userId);
            model.addAttribute("isOwner", isOwner);

            List<CommentResponseDto> comments = commentService.getAllCommentsOfMeeting(meetingId);
            List<CommentResponseDto> commentWithOwnership = comments.stream()
                    .map(x -> new CommentResponseDto(x, x.getUserId().equals(userId))).toList();
            model.addAttribute("comments", commentWithOwnership);

            return "detailedMeetingPage";
        } catch (IllegalArgumentException e) {
            return "redirect:/meetings";
        }

    }

    @GetMapping("/meetings")
    public String showAllMeeitngs(Model model,@AuthenticationPrincipal User user) {
        List<MeetingResponseDto> meetings = meetingService.getMeetings();
        model.addAttribute("meetings", meetings);
        return "mainMeetingPage";
    }

    @GetMapping("/searchMeetings")
    public String searchMeeting(@RequestParam("keyword") String keyword, Model model) {
        List<Meeting> searchResults = meetingService.searchMeeting(keyword, keyword, keyword);
        List<MeetingResponseDto> responseDtos = new ArrayList<>();
        for (Meeting meeting : searchResults) {
            MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
            responseDtos.add(meetingResponseDto);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("searchmeetings", responseDtos);
        return "meetingSearch";
    }

    @GetMapping("/review")
    public String reviewPage(Model model) {
        model.addAttribute("currentPage", "review");
        return "review"; // review 페이지의 뷰 이름
    }

    @GetMapping("/meeting")
    public String meetingPage(Model model) {
        model.addAttribute("currentPage", "meeting");
        return "meeting"; // meeting 페이지의 뷰 이름
    }
}
