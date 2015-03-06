package book.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import book.dto.TestBook;
import book.service.BookService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class BookControllerTest {
	
	@Autowired
	private BookService bookService;
	
	/**
	 * bookType에 맞는 리스트를 가져온다.
	 * @param bookType
	 * @return
	 */
	public List<TestBook> selectTestBookByBookType(Map<String, Object> map) {		
		return bookService.selectTestBookByBookType(map);
	}
	
	@Test
	public void testSelectTestBook() {
		BlockingQueue<TestBook> testQueue = new ArrayBlockingQueue<TestBook>(1000);
		String bookType = "1";
		selectTestBookAndPutQueue(testQueue, bookType);
	}

	
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
				start += testBookList.size();
				for(TestBook testBook : testBookList ) {			
					bookQueue.put(testBook);		
					deleteQueue(bookQueue);
				}// end for	
				System.out.println("total==="+start);
			}// end while 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteQueue(BlockingQueue<TestBook> testQueue) {		
		testQueue.clear();		
	}
}
