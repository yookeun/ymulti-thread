package book.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import book.controller.BookController;
import book.dto.TestBook;


/**
 * 해당분야의 결과큐에 있는 값을 가져와서 해당테이블에 인서트한다. 
 * @author ykkim
 *
 */
public class BookResultInsertThread implements Runnable {
	
	private BookController bookController;

	
	private BlockingQueue<TestBook> bookResultQueue;
	private final int LIMIT_COUNT = 500;
	
	public BookResultInsertThread(BookController bookController, Map<String, Object> queueMap, String bookType) {		
		this.bookController = bookController;
		this.	bookResultQueue = bookController.getResultQueue(queueMap, bookType);
	}	
	

	@Override
	public void run() {
		//bookType에 따른 결과큐를 가져온다.
	
		TestBook testBook = null;
		boolean empty = false;
		List<TestBook> testBookList = new ArrayList<TestBook>();
		
		
		//각각에 해당되는 큐에 값을 읽어들인다. 그리고 arrayList에 담는다. 그리고 LIMIT_COUNT 만큼 채워지면 각각의 테이블에 배치로 인서트된다.
		//그런데 만약 LIMIT_COUNT 만큼 차있지 않았다면 (즉 나머지), 그것을 마저 넣어주어야 한다. (즉 큐에는 비어 있는데 LIMIT_COUNT에 해당되지 않은 경우)
		while (true) {
			try {				
				//1. 결과큐가 비어 있는지 확인한다.
				synchronized (bookResultQueue) {
					if (bookResultQueue.isEmpty()) {
						empty = true;
					} else {
						empty = false;
					}
				}
				
				//2. 큐에 값이 잇다면 가져오고 없다면 널이 된다. 
				testBook = bookResultQueue.poll();	//poll은 take달리 대기상태가 되지 않는다. 
				if (testBook != null) {
					//bookType에 맞는 테이블명을 가져온다.
					testBook.setBookTable(bookController.getBookTableName(testBook.getBookType()));
					testBookList.add(testBook);
				}
				
				//3.   더이상 결과큐에 없고, testBookList > 0인 것들(즉 나머지들) 이거나, testBookList >= LIMIT_COUNT(500) 이면 처리한다. 
				if ( (empty && testBookList.size() > 0 ) || (testBookList.size() >= LIMIT_COUNT)) {
					sendResult(testBookList);
				}
				
				//4. 결과큐가 비어져 있고,testBookList = 0 이라면 대기하자 (take)
				else if (empty && testBookList.size() == 0) {
					testBook = bookResultQueue.take();  //모두 비웠으면 대기상태가 될 것이고 이상태에서 값이 들어오면 다시 리스트에 넣어주자.
					
					if (testBook != null) {
						//bookType에 맞는 테이블명을 가져온다.						
						//System.out.println("bookController.getBookTableName(testBook.getBookType())==============="+bookController.getBookTableName(testBook.getBookType()));
						if (bookController == null) {
							System.out.println("bookController is NULL");
						}
						testBook.setBookTable(bookController.getBookTableName(testBook.getBookType()));
						testBookList.add(testBook);
					}
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 결과테이블에 넣어준다.
	 * @param testBookList
	 */
	private void  sendResult(List<TestBook> testBookList) {
		bookController.insertTestBookBatch(testBookList);
		testBookList.clear();
	}
}
