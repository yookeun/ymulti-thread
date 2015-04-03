package book.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import book.common.Constant;
import book.controller.BookController;
import book.dto.TestBook;


@Component
@SuppressWarnings("resource")
public class BookMain {	
	private static final String[] SPRING_CONFIG_XML = new String[] {"applicationContext.xml"};
	private static final int QUEUE_SIZE = 1000;
	private static final Map<String, Object> queueMap = new HashMap<String, Object>();

	
	public static void main(String[] args) {		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING_CONFIG_XML);		
		BookMain main = ctx.getBean(BookMain.class);
		main.init();
		main.start();	
	}
	@Autowired
	private  BookController bookController;
	
	
	/**
	 * 큐생성
	 */
	private void init() {		
	
		BlockingQueue<TestBook> novelBookSendQueue = new ArrayBlockingQueue<TestBook>(QUEUE_SIZE);
		BlockingQueue<TestBook> businessBookSendQueue = new ArrayBlockingQueue<TestBook>(QUEUE_SIZE);
		BlockingQueue<TestBook> artBookSendQueue = new ArrayBlockingQueue<TestBook>(QUEUE_SIZE);
		
		BlockingQueue<TestBook> novelBookResultQueue = new ArrayBlockingQueue<TestBook>(QUEUE_SIZE);
		BlockingQueue<TestBook> businessBookResultQueue = new ArrayBlockingQueue<TestBook>(QUEUE_SIZE);
		BlockingQueue<TestBook> artBookResultQueue = new ArrayBlockingQueue<TestBook>(QUEUE_SIZE);
		
		//소설을 담을 발송큐
		queueMap.put("SEND_BOOK_TYPE_NOVEL", novelBookSendQueue);
		//비즈니스를 담을 발송큐
		queueMap.put("SEND_BOOK_TYPE_BUSINESS", businessBookSendQueue);		
		//예술을 담을 발송큐
		queueMap.put("SEND_BOOK_TYPE_ATR", artBookSendQueue);

		//소설을 담을 결과큐
		queueMap.put("RESULT_BOOK_TYPE_NOVEL", novelBookResultQueue);
		//비즈니스를 담을 결과큐
		queueMap.put("RESULT_BOOK_TYPE_BUSINESS", businessBookResultQueue);		
		//예술을 담을 결과큐
		queueMap.put("RESULT_BOOK_TYPE_ATR", artBookResultQueue);		
	}
	
	/**
	 * 스레드생성 
	 */
	private void start() {						
		//발송큐에 넣을 스레드 생성 (bookType = 1 : 소설책)
		// 각 분야당 1개씩 스레드를 생성한다.
		executeMultiExecutor(new BookPutQueueThread(bookController, queueMap, Constant.BOOK_TYPE_NOVEL), 1);
		executeMultiExecutor(new BookPutQueueThread(bookController, queueMap, Constant.BOOK_TYPE_BUSINESS), 1);
		executeMultiExecutor(new BookPutQueueThread(bookController, queueMap, Constant.BOOK_TYPE_ART), 1);
		
	
		//각분야의 큐에서 가져오기 각분야 결과큐에 넣는 스레드를 각각 10개씩 생성한다.
		executeMultiExecutor(new BookGetSendThread(bookController, queueMap, Constant.BOOK_TYPE_NOVEL), 10);
		executeMultiExecutor(new BookGetSendThread(bookController, queueMap, Constant.BOOK_TYPE_BUSINESS), 10);
		executeMultiExecutor(new BookGetSendThread(bookController, queueMap, Constant.BOOK_TYPE_ART), 10);
		
		//결과큐에 있는 레코드를 해당되는 테이블에 인서트하는 스레드 생성		
		//각 분야당 3개씩 스레드를 생성한다.
		executeMultiExecutor(new BookResultInsertThread(bookController, queueMap, Constant.BOOK_TYPE_NOVEL), 3);
		executeMultiExecutor(new BookResultInsertThread(bookController, queueMap, Constant.BOOK_TYPE_BUSINESS), 3);
		executeMultiExecutor(new BookResultInsertThread(bookController, queueMap, Constant.BOOK_TYPE_ART), 3);
	
	}
	
	/**
	 * 멀티 스레드풀 실행
	 * 
	 * @param object
	 * @param count
	 */
	private void executeMultiExecutor(Object object, int count) {
		ExecutorService executorService = Executors.newFixedThreadPool(count);  //항상 일정한 스레드 개수를 유지한다.
		for (int i = 0; i < count; i++) {
			executorService.execute((Runnable) object);
		}
	}
}
