package book.thread;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import book.controller.BookController;
import book.dto.TestBook;


/**
 * bookType에 해당되는 큐를 읽어들여 원래 책값의 10%을 판매액으로 변경한후에 결과큐에 넣어준다.
 * @author ykkim
 *
 */
public class BookGetSendThread implements Runnable {	

	private BlockingQueue<TestBook> bookSendQueue;
	private BlockingQueue<TestBook> bookResultQueue;
	
	public BookGetSendThread(BookController bookController,Map<String, Object> queueMap, String bookType) {
		bookSendQueue = bookController.getSendQueue(queueMap, bookType);		
		bookResultQueue = bookController.getResultQueue(queueMap, bookType);
	}	



	@Override
	public void run() {

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
