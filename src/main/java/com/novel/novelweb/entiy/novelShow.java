package com.novel.novelweb.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class novelShow {
    private String name;
    private String introduction;
    private String writer;
    private String type;
}
