package org.zerock.mreview.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.mreview.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositotyImpl extends QuerydslRepositorySupport implements SearchBoardRepositoty {

    public SearchBoardRepositotyImpl() {
        super(Review.class);
    }

    @Override
    public Movie serch1() {
        log.info("search1...............");
        QReview review = QReview.review;
//        QMovie movie = QMovie.movie;
//        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Review> jpqlQuery = from(review);
//        jpqlQuery.leftJoin(member).on(review.writer.eq(member));
//        jpqlQuery.leftJoin(reply).on(reply.board.eq(review));

//        JPQLQuery<Tuple> tuple = jpqlQuery.select(movie, member.email, reply.count());
        JPQLQuery<Tuple> tuple = jpqlQuery.select(review, member.email);
        tuple.groupBy(review);

        log.info("========================");
        log.info(tuple);
        log.info("=======================");

        List<Tuple> result = tuple.fetch();
        log.info(result);
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage..............");
        QMovie movie = QMovie.movie;
//        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Movie> jpqlQuery = from(movie);
//        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
//        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //select b, w , count(r) from board b
        //left join b.writer w left join reply r on r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(movie, member);

        BooleanBuilder booleanBuilder = new BooleanBuilder();//where 생성
        booleanBuilder.and(movie.mno.gt(0L));

        if (type != null) {
            String[] typearr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for (String t : typearr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(movie.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
//                    case "c":
//                        conditionBuilder.or(board.content.contains(keyword));
//                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);
        //oder by
        Sort sort = pageable.getSort();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder orderByExpression = new PathBuilder(Movie.class, "board");
            tuple.orderBy((new OrderSpecifier(direction, orderByExpression.get(order.getProperty()))));
        });
        tuple.groupBy(movie);
        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());
        List<Tuple> result = tuple.fetch();
        log.info(result);
        long count = tuple.fetchCount();
        log.info("count : " + count);
        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }
}
