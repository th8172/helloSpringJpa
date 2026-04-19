package kr.ac.hansung.cse.exception;

/**
 * =====================================================================
 * DuplicateCategoryException - 카테고리 이름 중복 예외
 * =====================================================================
 *
 * 비즈니스 규칙: 카테고리 이름은 고유해야 합니다.
 * CategoryService.createCategory()에서 동일 이름이 이미 존재하면 이 예외를 던집니다.
 *
 * RuntimeException을 상속하므로 @Transactional 메서드에서 던질 경우
 * Spring이 기본적으로 롤백을 수행합니다.
 *
 * Controller에서는 try-catch로 잡아 BindingResult에 필드 오류로 등록해
 * 폼을 다시 표시합니다.
 */
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("이미 존재하는 카테고리입니다: " + name);
    }
}
