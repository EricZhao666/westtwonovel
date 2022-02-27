package com.novel.novelweb.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class novel {
    private String name;
    private String introduction;
    private String writer;
    private String url;
    private String type;
}
