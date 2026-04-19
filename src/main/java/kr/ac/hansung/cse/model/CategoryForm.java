package kr.ac.hansung.cse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * =====================================================================
 * CategoryForm - 카테고리 등록/수정 폼 DTO
 * =====================================================================
 *
 * ProductForm 패턴과 동일하게 Bean Validation(JSR-380)을 적용해
 * 웹 폼 입력값을 엔티티와 분리하여 검증합니다.
 *
 * @NotBlank : null, "", " "(공백만) 모두 거부
 * @Size     : 문자열 최대 길이 제한 (Category.name 컬럼 길이 100과 별개로,
 *             과제 요구사항 50자 이내 적용)
 */
@Getter
@Setter
@NoArgsConstructor
public class CategoryForm {

    @NotBlank(message = "카테고리 이름을 입력하세요.")
    @Size(max = 50, message = "카테고리 이름은 50자 이내로 입력하세요.")
    private String name;
}
