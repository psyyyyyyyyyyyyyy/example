package com.example.mediaboard.config;

import com.example.mediaboard.entity.Comment;
import com.example.mediaboard.entity.Post;
import com.example.mediaboard.repository.CommentRepository;
import com.example.mediaboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터 확인
        if (postRepository.count() > 0) {
            return; // 이미 데이터가 있으면 초기화하지 않음
        }
        
        // 샘플 게시글 생성
        createSamplePosts();
    }
    
    private void createSamplePosts() {
        // 1. 풍경 카테고리 게시글
        Post post1 = new Post("풍경", "아름다운 일몰 풍경 🌅", 
                "오늘 찍은 멋진 일몰 사진입니다. 정말 아름다운 순간이었어요!", "포토그래퍼");
        post1.setCreatedAt(LocalDateTime.now().minusHours(3));
        post1.setLikes(24);
        post1.setViews(156);
        post1.setComments(8);
        Post savedPost1 = postRepository.save(post1);
        
        // 2. 일상 카테고리 게시글 (동영상)
        Post post2 = new Post("일상", "고양이가 장난감을 가지고 노는 영상 🐱", 
                "우리 집 고양이 '루루'가 새 장난감을 좋아해요 ㅎㅎ", "집사");
        post2.setCreatedAt(LocalDateTime.now().minusHours(7));
        post2.setLikes(45);
        post2.setViews(320);
        post2.setComments(15);
        Post savedPost2 = postRepository.save(post2);
        
        // 3. 음식 카테고리 게시글
        Post post3 = new Post("음식", "홈베이킹 성공! 🍰", 
                "처음으로 레몬 타르트를 만들어봤는데 생각보다 잘 나왔어요!", "베이킹러버");
        post3.setCreatedAt(LocalDateTime.now().minusHours(14));
        post3.setLikes(67);
        post3.setViews(234);
        post3.setComments(22);
        Post savedPost3 = postRepository.save(post3);
        
        // 4. 여행 카테고리 게시글 (동영상)
        Post post4 = new Post("여행", "여행 브이로그 - 제주도 3박 4일 ✈️", 
                "제주도 여행 하이라이트 영상이에요. 날씨가 정말 좋았어요!", "여행유튜버");
        post4.setCreatedAt(LocalDateTime.now().minusDays(1));
        post4.setLikes(89);
        post4.setViews(567);
        post4.setComments(34);
        Post savedPost4 = postRepository.save(post4);
        
        // 5. 인물 카테고리 게시글
        Post post5 = new Post("인물", "새로 산 카메라로 첫 인물사진! 📸", 
                "드디어 새 미러리스 카메라를 샀어요. 처음 찍어본 인물사진들 어떤가요?", "사진초보");
        post5.setCreatedAt(LocalDateTime.now().minusDays(2));
        post5.setLikes(31);
        post5.setViews(189);
        post5.setComments(12);
        Post savedPost5 = postRepository.save(post5);
        
        // 6. 일상 카테고리 게시글 2
        Post post6 = new Post("일상", "도심 속 건축물 스냅샷 🏙️", 
                "출근길에 찍은 멋진 건축물들. 빛과 그림자의 조화가 아름다워요.", "도시탐험가");
        post6.setCreatedAt(LocalDateTime.now().minusDays(3));
        post6.setLikes(18);
        post6.setViews(142);
        post6.setComments(6);
        Post savedPost6 = postRepository.save(post6);
        
        // 7. 음식 카테고리 게시글 (동영상)
        Post post7 = new Post("음식", "커피 드립 과정 ASMR ☕", 
                "오늘 아침 핸드드립 커피 내리는 과정을 찍었어요. 소리가 너무 좋아요!", "카페인중독자");
        post7.setCreatedAt(LocalDateTime.now().minusDays(4));
        post7.setLikes(52);
        post7.setViews(387);
        post7.setComments(19);
        Post savedPost7 = postRepository.save(post7);
        
        // 샘플 댓글들 생성
        createSampleComments(savedPost1);
        createSampleComments(savedPost2);
        createSampleComments(savedPost3);
        createSampleComments(savedPost4);
        createSampleComments(savedPost5);
        
        System.out.println("샘플 데이터 초기화 완료!");
    }
    
    private void createSampleComments(Post post) {
        String[] sampleComments = {
            "정말 멋진 작품이네요! 👏",
            "어떤 장비로 촬영하셨나요?",
            "와 진짜 예술이다...",
            "저도 한번 시도해보고 싶어요!",
            "색감이 정말 좋네요 ✨",
            "다음 작품도 기대됩니다!",
            "팁 공유해주세요~"
        };
        
        String[] sampleAuthors = {
            "익명", "사진애호가", "초보사진사", "관람자", 
            "팬", "동호인", "구독자", "방문자"
        };
        
        // 각 게시글에 2-4개의 댓글 추가
        int commentCount = (int) (Math.random() * 3) + 2;
        for (int i = 0; i < commentCount; i++) {
            String content = sampleComments[(int) (Math.random() * sampleComments.length)];
            String author = sampleAuthors[(int) (Math.random() * sampleAuthors.length)];
            
            Comment comment = new Comment(post, author, content);
            comment.setCreatedAt(LocalDateTime.now().minusHours(i + 1));
            commentRepository.save(comment);
        }
    }
}
