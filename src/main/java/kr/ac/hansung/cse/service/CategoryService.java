package kr.ac.hansung.cse.service;

import kr.ac.hansung.cse.exception.DuplicateCategoryException;
import kr.ac.hansung.cse.model.Category;
import kr.ac.hansung.cse.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * =====================================================================
 * CategoryService - 카테고리 비즈니스 로직 계층
 * =====================================================================
 *
 * [설계 포인트]
 * 1. 클래스 레벨 @Transactional(readOnly = true): 조회 전용 트랜잭션이 기본값.
 *    Hibernate가 더티 체킹(변경 감지)을 생략하므로 성능 이점이 있음.
 * 2. 쓰기 메서드(createCategory, deleteCategory)에만 @Transactional을 덧붙여
 *    readOnly를 오버라이드 → 쓰기 트랜잭션으로 전환.
 * 3. 이름 중복 검사는 DB 레벨 UNIQUE 제약 대신 서비스 계층에서 Optional 반환으로
 *    처리 → 사용자 친화적인 메시지를 Form 오류로 피드백 가능.
 * 4. 연결된 Product가 있는 Category 삭제는 COUNT 쿼리 선행 → IllegalStateException.
 */
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리 조회 (목록 화면 + ProductList 드롭다운 공용)
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 새 카테고리 등록
     *
     * 중복 검사 흐름:
     *   1) findByName()이 Optional<Category> 반환
     *   2) isPresent() 이면 DuplicateCategoryException 발생
     *   3) 정상 흐름이면 save() → 영속성 컨텍스트에 persist
     *
     * @throws DuplicateCategoryException 동일 이름의 카테고리가 이미 존재하는 경우
     */
    @Transactional
    public Category createCategory(String name) {
        categoryRepository.findByName(name)
                .ifPresent(existing -> {
                    throw new DuplicateCategoryException(name);
                });
        return categoryRepository.save(new Category(name));
    }

    /**
     * 카테고리 삭제
     *
     * 안전 장치: 연결된 Product가 있는 카테고리를 삭제하면 FK 제약 위반
     * 또는 cascade로 상품이 함께 지워질 수 있으므로, 선행 COUNT 쿼리로 확인 후
     * IllegalStateException을 던져 Controller가 사용자에게 안내하도록 함.
     *
     * @throws IllegalStateException 연결된 상품이 1건 이상 존재할 때
     */
    @Transactional
    public void deleteCategory(Long id) {
        long productCount = categoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new IllegalStateException(
                    "이 카테고리에는 상품 " + productCount + "개가 연결되어 있어 삭제할 수 없습니다.");
        }
        categoryRepository.delete(id);
    }
}
