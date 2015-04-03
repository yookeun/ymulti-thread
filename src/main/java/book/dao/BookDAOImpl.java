package book.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import book.dto.TestBook;

@Component
public class BookDAOImpl implements BookDAO {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int selectCountTestBookByBookType(String bookType) {		
		return sqlSession.selectOne("TestBook.selectCountTestBookByBookType", bookType);
	}

	@Override
	public List<TestBook> selectTestBookByBookType(Map<String, Object> map) {	
		return sqlSession.selectList("TestBook.selectTestBookByBookType", map);
	}

	@Override
	public void insertTestBookBatch(List<TestBook> testBookList) {
		for (TestBook  testBook : testBookList) {
			sqlSession.insert("TestBook.insertTestBookBatch", testBook);
		}
	}
	
	@Override
	public void updateProcessTestBookBatch(List<TestBook>testBookList) {
		for (TestBook  testBook : testBookList) {
			sqlSession.update("TestBook.updateProcessTestBookBatch", testBook);
		}
	}
	
}
