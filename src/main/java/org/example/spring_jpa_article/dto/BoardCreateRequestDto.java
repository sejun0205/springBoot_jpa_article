package org.example.spring_jpa_article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardCreateRequestDto {
    private String boardTitle;
    private String boardContent;

}
