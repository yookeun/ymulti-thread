package book.thread;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import book.controller.BookController;
import book.dto.TestBook;

public class BookGetSendThread implements Runnable {
	
	private BookController bookController;
	private final Map<String, Object> queueMap;
	private String bookType;
	private BlockingQueue<TestBook> bookSendQueue;
	private BlockingQueue<TestBook> bookResultQueue;
	
	public BookGetSendThread(BookController bookController,Map<String, Object> queueMap, String bookType) {
		this.bookController = bookController;
		this.queueMap = queueMap;
		this.bookType = bookType;
	}	



	@Override
	public void run() {
		bookSendQueue = bookController.getSendQueue(queueMap, bookType);		
		bookResultQueue = bookController.getResultQueue(queueMap, bookType);
		TestBook testBook = null;
		while (true) {
			try {
				testBook = bookSendQueue.take();
				//원래 책값의 10%을 판매액으로 변경한다.
				testBook.setSellPrice(testBook.getOriginPrice() + testBook.getOriginPrice() * 0.1);
				//변경된 책을 결과큐에 넣어준다.
				bookResultQueue.put(testBook);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
