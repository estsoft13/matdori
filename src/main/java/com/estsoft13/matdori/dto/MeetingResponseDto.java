package com.estsoft13.matdori.dto;

import com.estsoft13.matdori.domain.Meeting;
import com.estsoft13.matdori.domain.Restaurant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
public class MeetingResponseDto {
    private Long id;

    private Long user_id;

    private String title;

    private String content;

    private String location;

    private LocalDateTime created_at;

    private String visitTime;

    private String username;
    private Restaurant restaurant;

    //private Long user_id;
    @Builder
    public MeetingResponseDto(Long id, String title, String content, Restaurant restaurant,
                              String location, LocalDateTime created_at, String username, String visitTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.location = location;
        this.created_at = created_at;
        this.username = username;
        this.restaurant = restaurant;
        this.visitTime = visitTime;
    }

    public MeetingResponseDto(Meeting meeting) {
        this.id = meeting.getId();
        this.user_id = meeting.getUser().getId();
        this.username = meeting.getUser().getEnteredUsername();
        this.title = meeting.getTitle();
        this.location = meeting.getLocation();
        this.content = meeting.getContent();
        this.restaurant = meeting.getRestaurant();
        this.created_at = meeting.getCreated_at();
        this.visitTime = meeting.getVisitTime();
    }


}
