package cn.zealon.readingcloud.homepage.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 热搜词
 *
 * @author: zealon
 * @since: 2020/5/29
 */
public interface HotSearchWordMapper {

    /**
     * 获取热搜词
     *
     * @param size
     * @return
     */
    @Select("SELECT name FROM hot_search_word order by frequency desc limit #{size}")
    List<String> getHotSearchWordList(@Param("size") int size);

    @Select("insert into hot_search_word(name) values('${name}')")
    void saveHotSearchWord(@Param("name") String name);

    @Select("select hot_search_word set frequency=#{frequency} where name='${name}'")
    void updateHotSearchWord(@Param("name") String name, @Param("frequency") String frequency);
}
