package book.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import book.controller.BookController;
import book.dto.TestBook;

public class BookResultInsertThread implements Runnable {
	
	private BookController bookController;
	private final Map<String, Object> queueMap;
	private String bookType;
	private BlockingQueue<TestBook> bookResultQueue;
	private final int LIMIT_COUNT = 1000;
	
	public BookResultInsertThread(BookController bookController, Map<String, Object> queueMap, String bookType) {
		this.queueMap = queueMap;
		this.bookType = bookType;
	}	
	

	@Override
	public void run() {
		//bookType에 따른 결과큐를 가져온다.
		bookResultQueue = bookController.getResultQueue(queueMap, bookType);
		TestBook testBook = null;
		List<TestBook> testBookList = new ArrayList<TestBook>();
		while (true) {
			try {
				testBook = bookResultQueue.take();
				if (testBook != null) {
					sendTestBookList(testBookList, testBook);	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	private void sendTestBookList(List<TestBook> testBookList, TestBook testBook) {
		testBookList.add(testBook);
		if (testBookList.size() >= LIMIT_COUNT) {
			bookController.insertTestBookBatch(testBookList);
			testBookList.clear();
		}
	}
}
