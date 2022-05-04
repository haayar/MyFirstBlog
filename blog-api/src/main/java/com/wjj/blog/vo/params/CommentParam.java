package com.wjj.blog.vo.params;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

@Data
public class CommentParam {

    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;
}
