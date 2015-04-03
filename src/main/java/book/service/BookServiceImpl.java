package book.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.dao.BookDAO;
import book.dto.TestBook;

@Service
public class BookServiceImpl  implements BookService {
	
	@Autowired
	private BookDAO bookDAO;

	@Override
	public int selectCountTestBookByBookType(String bookType) {		
		return bookDAO.selectCountTestBookByBookType(bookType);
	}

	@Override
	public List<TestBook> selectTestBookByBookType(Map<String, Object> map) {	
		return bookDAO.selectTestBookByBookType(map);
	}	
	
	@Override
	public void insertTestBookBatch(List<TestBook> testBookList) {
		bookDAO.insertTestBookBatch(testBookList);
	}
	
	@Override
	public void updateProcessTestBookBatch(List<TestBook>testBookList) {
		bookDAO.updateProcessTestBookBatch(testBookList);
	}
}
