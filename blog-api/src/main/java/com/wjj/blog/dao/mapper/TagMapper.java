package com.wjj.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjj.blog.dao.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> findTagsByArticleId(Long id);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
