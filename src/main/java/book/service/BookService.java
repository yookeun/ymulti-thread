package book.service;

import java.util.List;
import java.util.Map;

import book.dto.TestBook;



public interface BookService {
	
	/**
	 * test_book_origin 테이블에서 bookType에 맞는 처리안된(processYN='N') 레코드가 있는지 확인  
	 * @return
	 */
	public int selectCountTestBookByBookType(String bookType);
	
	
	/**
	 * bookType에 맞는 리스트를 가져온다.
	 * @param bookType
	 * @return
	 */
	public List<TestBook> selectTestBookByBookType(Map<String, Object> map);
	
	/**
	 * 결과를 각 테이블에 넣는 배치 
	 * @param testBookList
	 */
	public void insertTestBookBatch(List<TestBook> testBookList);
	
	/**
	 * 스레드에서 가져간 책들  표시  
	 * @param testBookList
	 */
	public void updateProcessTestBookBatch(List<TestBook>testBookList);
}
