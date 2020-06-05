package cn.zealon.readingcloud.homepage.service.impl;

import cn.zealon.readingcloud.homepage.service.BookCenterService;
import cn.zealon.readingcloud.common.pojo.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.zealon.readingcloud.book.feign.client.BookClient;

/**
 * 图书中心服务
 * @author: zealon
 * @since: 2019/7/4
 */
@Service
public class BookCenterServiceImpl implements BookCenterService {

    @Autowired
    private BookClient bookClient;

    @Override
    public Book getBookById(String bookId) {
        // 图书中心服务获取
        Book book = bookClient.getBookById(bookId).getData();
        return book;
    }
}
