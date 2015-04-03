package book.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import book.common.Constant;
import book.dto.TestBook;
import book.service.BookService;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookSerive;

	
	/**
	 * test_book_origin 테이블에서 bookType에 맞는 처리안된(processYN='N') 레코드가 있는지 확인  
	 * @return
	 */
	public int selectCountTestBookByBookType(String bookType) {
		return bookSerive.selectCountTestBookByBookType(bookType);
	}
	
	/**
	 * bookType에 맞는 리스트를 가져온다.
	 * @param bookType
	 * @return
	 */
	public List<TestBook> selectTestBookByBookType(Map<String, Object> map) {
		return bookSerive.selectTestBookByBookType(map);
	}
	
	
	/**
	 * bookType에 맞는 레코드를 조회해서  큐에 넣어준다.
	 * @param bookQueue
	 * @param bookType
	 */
	public void selectTestBookAndPutQueue(BlockingQueue<TestBook> bookQueue, String bookType) {
		Map<String, Object> map = new HashMap<String, Object>();		
		try {
			map.put("bookType", bookType);
			List<TestBook> testBookList = null;
			final int  LIMIT_COUNT = 1000;
			int start = 0;		
			int total = 0;
			while (true) {			
				map.put("start", start);
				map.put("limit", LIMIT_COUNT);
				testBookList = selectTestBookByBookType(map);
				if (testBookList.size() == 0 || testBookList == null) {
					break;
				}
				total += testBookList.size();
				bookSerive.updateProcessTestBookBatch(testBookList);
				for(TestBook testBook : testBookList ) {			
					if (testBook != null) {
						bookQueue.put(testBook);	
					}
									
				} // end for	
				
				System.out.println("["+getBookTableName(bookType)+"] count = " + total);
			}// end while 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * bootype에 해당되는 발송큐를 가져온다.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BlockingQueue<TestBook> getSendQueue(final Map<String, Object> queueMap, String bookType) {
		BlockingQueue<TestBook> bookQueue = null;
		if (bookType.equals(Constant.BOOK_TYPE_NOVEL)) {
			bookQueue = (BlockingQueue<TestBook>) queueMap.get("SEND_BOOK_TYPE_NOVEL");
		}  else if (bookType.equals(Constant.BOOK_TYPE_BUSINESS)) {
			bookQueue = (BlockingQueue<TestBook>) queueMap.get("SEND_BOOK_TYPE_BUSINESS");
		}  else if (bookType.equals(Constant.BOOK_TYPE_ART)) {
			bookQueue = (BlockingQueue<TestBook>) queueMap.get("SEND_BOOK_TYPE_ATR");
		} 
		return bookQueue;
	}
	
	/**
	 * bootype에 해당되는 결과큐를 가져온다.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BlockingQueue<TestBook> getResultQueue(final Map<String, Object> queueMap, String bookType) {
		BlockingQueue<TestBook> bookQueue = null;
		if (bookType.equals(Constant.BOOK_TYPE_NOVEL)) {
			bookQueue = (BlockingQueue<TestBook>) queueMap.get("RESULT_BOOK_TYPE_NOVEL");
		}  else if (bookType.equals(Constant.BOOK_TYPE_BUSINESS)) {
			bookQueue = (BlockingQueue<TestBook>) queueMap.get("RESULT_BOOK_TYPE_BUSINESS");
		}  else if (bookType.equals(Constant.BOOK_TYPE_ART)) {
			bookQueue = (BlockingQueue<TestBook>) queueMap.get("RESULT_BOOK_TYPE_ATR");
		} 
		return bookQueue;
	}	
	
	/**
	 * 결과를 각 테이블에 넣는 배치 
	 * @param testBookList
	 */
	public void insertTestBookBatch(List<TestBook> testBookList) {
		bookSerive.insertTestBookBatch(testBookList);
	}
	
	
	/**
	 * bookType에 따른 테이블명 
	 * @param bookType
	 * @return
	 */
	public String getBookTableName(String bookType) {
		String bookTable = "";
		if (bookType.equals(Constant.BOOK_TYPE_ART)) {
			bookTable = "test_book_art";
		} else if (bookType.equals(Constant.BOOK_TYPE_BUSINESS)) {
			bookTable = "test_book_business";
		} else if (bookType.equals(Constant.BOOK_TYPE_NOVEL)) {
			bookTable = "test_book_novel";
		}		
		return bookTable;
	}
	
}
 