package com.estsoft13.matdori.controller.review;

import com.estsoft13.matdori.domain.Restaurant;
import com.estsoft13.matdori.domain.Review;
import com.estsoft13.matdori.domain.ReviewImage;
import com.estsoft13.matdori.domain.User;
import com.estsoft13.matdori.dto.comment.CommentResponseDto;
import com.estsoft13.matdori.dto.review.ReviewResponseDto;
import com.estsoft13.matdori.service.CommentService;
import com.estsoft13.matdori.service.RestaurantService;
import com.estsoft13.matdori.service.ReviewImageService;
import com.estsoft13.matdori.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewPageController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final CommentService commentService;

    // 리뷰 추가, 수정 페이지
    @GetMapping("/add-review")
    public String showCreateReviewForm(@RequestParam(required = false) Long reviewId, Model model) {

        List<Restaurant> restaurants = restaurantService.findAll();
        model.addAttribute("restaurants", restaurants);

        // 리뷰 추가
        if (reviewId == null) {
            model.addAttribute("review", new Review());

            // 리뷰 수정
        } else {
            Optional<Review> reviewOptional = reviewService.findById(reviewId);
            Review review = reviewOptional.get();

            model.addAttribute("review", review);
        }

        return "review-form";
    }

    // 리뷰 상세 페이지
    @GetMapping("/review/{reviewId}")
    public String showReviewDetail(@PathVariable Long reviewId, Model model, @AuthenticationPrincipal User user) {

        Optional<Review> reviewOptional = reviewService.findById(reviewId);

        // 리뷰가 없을 경우 /reviews 으로 리다이렉트
        if (!reviewOptional.isPresent()) {
            return "redirect:/reviews";
        }

        Review review = reviewOptional.get();
        //조회수 up
        reviewService.countUpViewCount(reviewId);
        review.setViewCount(review.getViewCount());
        model.addAttribute("review", review);

        // 리뷰에 등록된 이미지 불러오기
        List<ReviewImage> images = reviewImageService.findAllByReviewId(reviewId);
        model.addAttribute("images", images);

        // 리뷰 작성자와 현재 로그인한 사용자가 같은지 확인
        Long userId = user.getId();
        boolean isOwner = review.getUser().getId().equals(userId);
        model.addAttribute("isOwner", isOwner);

        // 리뷰에 달린 댓글 불러오기
        List<CommentResponseDto> comments = commentService.getAllCommentsOfReview(reviewId);
        List<CommentResponseDto> commentWithOwnership = comments.stream()
                .map(x -> new CommentResponseDto(x, x.getUserId().equals(userId))).toList();
        model.addAttribute("comments", commentWithOwnership);

        return "detailedReviewPage";
    }

    // 리뷰 목록 페이지
    @GetMapping("/reviews")
    public String showReviews(@RequestParam(required = false) String sort, Model model) {

        // sort에 따라 리뷰 정렬
        List<Review> reviews;
        if("rating".equals(sort)) {
            reviews = reviewService.findAllByOrderByRatingDesc();
        } else if ("viewCount".equals(sort)) {
            reviews = reviewService.findAllByOrderByViewCountDesc();
        } else if ("latest".equals(sort)){
            reviews = reviewService.findAllByOrderByCreatedAtDesc();
        } else {
            reviews = reviewService.findAll();
        }

        // 리뷰에 등록된 이미지 불러오기
        List<ReviewResponseDto> responseDtoList = new ArrayList<>();
        setImgPathsToResponseDto(reviews, responseDtoList);

        model.addAttribute("reviews", responseDtoList);

        return "review-main";
    }

    // 검색기능 ->  검색시 리뷰의 타이틀, 내용, 관련 식당에 키워드가 있으면 표시
    // /reviews/에서 검색 시 /search?keyword={keyword}로 이동
    @GetMapping("/searchReviews")
    public String searchByKeyword(@RequestParam("keyword") String keyword, Model model) {

        // 제목, 내용, 식당이름, 식당카테고리에 키워드가 포함된 리뷰 찾기
        List<Review> searchResults = reviewService.findByTitleContainingOrContentContainingOrRestaurantNameContainingOrRestaurantCategoryContaining(keyword, keyword, keyword, keyword);

        // 리뷰에 등록된 이미지 불러오기
        List<ReviewResponseDto> responseDtoList = new ArrayList<>();
        setImgPathsToResponseDto(searchResults, responseDtoList);

        model.addAttribute("reviews", responseDtoList);
        model.addAttribute("keyword", keyword);

        return "review-main";
    }

    // ReviewResponseDto에 이미지 경로 추가 메소드
    public void setImgPathsToResponseDto(List<Review> reviews, List<ReviewResponseDto> responseDtoList) {

        for (Review review : reviews) {

            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
            Long reviewId = review.getId();

            List<ReviewImage> reviewImages = reviewImageService.findAllByReviewId(reviewId);
            List<String> imgPaths = new ArrayList<>();

            for (ReviewImage reviewImage : reviewImages) {
                imgPaths.add(reviewImage.getImgPath());
            }
            reviewResponseDto.setImgPaths(imgPaths);
            responseDtoList.add(reviewResponseDto);
        }
    }

}
