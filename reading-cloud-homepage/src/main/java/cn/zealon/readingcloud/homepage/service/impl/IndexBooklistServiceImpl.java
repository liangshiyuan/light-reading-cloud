package cn.zealon.readingcloud.homepage.service.impl;

import cn.zealon.readingcloud.common.pojo.index.IndexBooklist;
import cn.zealon.readingcloud.homepage.common.Const;
import cn.zealon.readingcloud.common.enums.BooklistMoreTypeEnum;
import cn.zealon.readingcloud.homepage.dao.IndexBooklistMapper;
import cn.zealon.readingcloud.homepage.service.IndexBooklistItemService;
import cn.zealon.readingcloud.homepage.service.IndexBooklistService;
import cn.zealon.readingcloud.homepage.vo.BooklistBookVO;
import cn.zealon.readingcloud.homepage.vo.IndexBooklistVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 书单服务
 *
 * @author: zealon
 * @since: 2020/4/6
 */
@Service
public class IndexBooklistServiceImpl implements IndexBooklistService {

    @Autowired
    private IndexBooklistMapper indexBooklistMapper;

    @Autowired
    private IndexBooklistItemService indexBooklistItemService;

    /**
     * 获取书单VO
     *
     * @param booklistId
     * @param clientRandomNumber
     * @return
     */
    @Override
    public IndexBooklistVO getIndexBooklistVO(Integer booklistId, Integer clientRandomNumber) {
        IndexBooklist booklist = this.getIndexBooklistById(booklistId);
        // 是否随机获取
        boolean randomFlag = booklist.getMoreType() == BooklistMoreTypeEnum.EXCHANGE.getValue() ? true : false;
        IndexBooklistVO booklistVO = null;
        if (randomFlag) {
            booklistVO = this.getRandomIndexBooklistVO(booklist, clientRandomNumber);
        } else {
            // DB 顺序获取
            List<BooklistBookVO> books = this.indexBooklistItemService.getBooklistOrderBooks(booklist.getId(), booklist.getBookIds(), booklist.getShowNumber(), booklist.getShowLikeCount());
            if (books.size() > 0) {
                booklistVO = new IndexBooklistVO();
                BeanUtils.copyProperties(booklist, booklistVO);
                booklistVO.setBooks(books);
                booklistVO.setRandomNumber(1);
            }
        }
        return booklistVO;
    }

    @Override
    public IndexBooklist getIndexBooklistById(Integer booklistId) {
        IndexBooklist booklist = this.indexBooklistMapper.selectById(booklistId);
        return booklist;
    }

    @Override
    public IndexBooklistVO getRandomIndexBooklistVO(IndexBooklist booklist, Integer clientRandomNumber) {
        Random random = new Random();
        Integer randomNumber = random.nextInt(Const.BOOKLIST_RANDOM_COUNT);
        if (clientRandomNumber != null) {
            while (randomNumber.intValue() == clientRandomNumber) {
                randomNumber = random.nextInt(Const.BOOKLIST_RANDOM_COUNT);
            }
        }
        IndexBooklistVO booklistVO = null;
        // DB 随机获取
        List<BooklistBookVO> books = this.indexBooklistItemService.getBooklistRandomBooks(booklist.getId(), booklist.getBookIds(), booklist.getShowNumber(), clientRandomNumber, booklist.getShowLikeCount());
        if (books.size() > 0) {
            booklistVO = new IndexBooklistVO();
            BeanUtils.copyProperties(booklist, booklistVO);
            booklistVO.setBooks(books);
            booklistVO.setRandomNumber(randomNumber);
        }
        return booklistVO;
    }
}
