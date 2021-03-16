package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService {
    //영화의 모든 영화리뷰를 가져온다
    List<ReviewDTO> getListOfMovie(Long mno);

    //영화 리뷰를 추가
    Long register(ReviewDTO moviereviewDTO);

    //특정한 영화리뷰 수정
    void modify(ReviewDTO moviereviewDTO);

    //영화 리뷰 삭제
    void remove(Long revienwm);

    default Review dtoToEntity(ReviewDTO moviereviewDTO) {
        Review movieReview = Review.builder()
                .reviewnum(moviereviewDTO.getReviewnum())
                .movie(Movie.builder().mno(moviereviewDTO.getMno()).build())
                .member(Member.builder().mid(moviereviewDTO.getMid()).build())
                .grade(moviereviewDTO.getGrade())
                .text(moviereviewDTO.getText())
                .build();
        return movieReview;
    }

    default ReviewDTO entityToDto(Review movieReview) {
        ReviewDTO movieReviewDTO = ReviewDTO.builder()
                .reviewnum(movieReview.getReviewnum())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickname(movieReview.getMember().getNickname())
                .email(movieReview.getMember().getEmail())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();
        return movieReviewDTO;
    }
}
