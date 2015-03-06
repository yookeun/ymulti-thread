package book.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import book.common.Constant;
import book.controller.BookController;
import book.dto.TestBook;

/**
 * test_board_origin에서 bookType에 맡게 각각 BlockingQueue에 넣어주는  스레드 
 * @author ykkim
 *
 */
public class BookPutQueueThread implements Runnable {	
	private BookController bookController;
	private final Map<String, Object> queueMap;
	private String bookType;
	private BlockingQueue<TestBook> bookQueue;	
	
	public BookPutQueueThread(BookController bookController, Map<String, Object> queueMap, String bookType) {
		this.bookController = bookController;
		this.queueMap = queueMap;
		this.bookType = bookType;
		
	}	
	
	

	@Override
	public void run() {	
		//bookType에 해당되는 큐를 가져온다.
		bookQueue = bookController.getSendQueue(queueMap, bookType);		
		while (true) {
			bookController.selectTestBookAndPutQueue(bookQueue, bookType);
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
