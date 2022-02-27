package com.novel.novelweb.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class novelcollect implements Comparable<novelcollect> {
    private String name;
    private int reader;
    private int id;

    @Override
    public int compareTo(novelcollect o) {
           int i=this.reader-o.reader;

            return i;


    }
}
