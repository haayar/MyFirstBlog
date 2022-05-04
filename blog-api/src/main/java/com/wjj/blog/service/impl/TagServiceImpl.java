package com.wjj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjj.blog.dao.mapper.TagMapper;
import com.wjj.blog.dao.pojo.Tag;
import com.wjj.blog.service.TagService;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    @Override
    public List<TagVo> findTagsByArticleId(Long id) {
        //mybatis-plus不可以进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(id);
        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        /*
        最热标签
        首先标签所拥有的文章数量最多就是最热标签
        查询根据tag_id分组计数，从大到小排列，取前limit
        select count(*) as count,tag_id from 'ms_article_tag" group by tag_id order by count desc limit 2

         */
        List<Long> tagIds=tagMapper.findHotsTagIds(limit);

        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        /*
        select * from tag where id in()
         */
        List<Tag> tagList=tagMapper.findTagsByTagIds(tagIds);

        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);

        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());

        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
//        return null;
        return Result.success(copy(tag));
    }
}
