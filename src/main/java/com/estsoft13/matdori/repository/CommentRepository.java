package com.estsoft13.matdori.repository;

import com.estsoft13.matdori.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReview_Id(Long reviewId);
    Optional<Comment> findByIdAndReview_Id(Long id, Long reviewId);
    List<Comment> findByMeeting_Id(Long meetingId);
    Comment findByUser_Id(Long UserId);
    Optional<Comment> findByIdAndMeeting_Id(Long id, Long meetingId);

    void deleteByReview_Id(Long reviewId);

    void deleteByMeeting_Id(Long meetingId);
}
