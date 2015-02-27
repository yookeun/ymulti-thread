package book.dto;

import java.io.Serializable;

/**
 * 테이블 : books, sell_books_novel, sell_books_business, sell_books_art 테이블 엔티티 
 * @author ykkim
 *
 */
@SuppressWarnings("serial")
public class Book implements Serializable {
	
	private int bookID;				//고유키
	private String bookName;	//책명	
	private String bookAuthor;	//작가
	private String bookType;	//1: 소설, 2: 비즈니스, 3: 예술
	private String originPrice;	//원래가격
	private String sellPrice;		//팔가격 
	private String processYN;	//처리여부 : 해당되는 테이블로 처리되면 Y 
	private String registDate;	//등록일 
	
	public int getBookID() {
		return bookID;
	}
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	public String getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}
	public String getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getProcessYN() {
		return processYN;
	}
	public void setProcessYN(String processYN) {
		this.processYN = processYN;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}	
}
