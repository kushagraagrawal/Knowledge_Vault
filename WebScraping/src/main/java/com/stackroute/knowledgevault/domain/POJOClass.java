package com.stackroute.knowledgevault.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class POJOClass {
    String url;
    String title;
    List<String> paragraph;
    String body;
}
