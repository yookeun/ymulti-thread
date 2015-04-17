#ymulti-thread


1. 배경
----------------------

ymulti-thread는 멀티스레드기반의 Pure JAVA Application 입니다. 
Blocking Queue와 Producer-consumer 패턴과 Worker Thread패턴으로 구현되어 있습니다.



**ymulti-thread에 적용된 기술은 다음과 같습니다.**

사용된 오픈소스|버전
------------|---
java|1.7
spring|4.1.5
gradle|2.2.1
mybatis|3.2.8


2. 기능설명
-----------------
ymulti-thread는 등록된 서적을 각각 해당분류로 정리하는 프로그램입니다. 
아래 원본 테이블에  각각 10만건의 책들이 쌓여 있습니다.

**test_book_origin**

컬럼명|타입|용도
-----|---|
bookID|INT|고유키
bookName|VARCHAR(100)|제목
bookAuthor|VARCHAR(20)|작가
bookType|CHAR(1)|책구분: 1(소설),2(비즈니스),3(예술)
originPrice|DOUBLE|책원가
processYN|CHAR(1)|처리유무
registDate|DATETIME|입력날짜
----------------
여기에 3가지종류(소설, 비즈니스, 예술)의 책들이 쌓여 있습니다. ymulti-thread는 이 원본테이블을 각각 스레드별로 읽어들여 
책값을 조정하고 각각의 해당되는 테이블에 인서트해주는 역할을 합니다. 

즉, 소설(bookType=1)은 test_book_novel 테이블로 인서트하고, 
비즈니스(bookType=2)는 test_book_business 테이블로 인서트하고, 예술(bookType=3)은 test_book_art 테이블로 인서트하고 
완료가 된 레코드는 processYN='Y'로 처리합니다. 

추후에 test_book_origin에 추가값이 입력되면 해당 스레드가 같은 작업을 반복합니다. 

>각 테이블생성과 프로시저는 src/main/resource/installTable.sql에 존재합니다. 


3. 주요클래스 설명
----------------
###BookMain

맨처음 실행하는 메인 클래스이다. 스프링 설정파일인 ApplicationContext.xml를 읽어들이고, 각각의 책종류에 맞게 큐가 생성된다. 
큐는 각 책종류에 각각 2개씩 생성된다. 소설을 담는 발송큐와 소설을 처리하는 결과큐등으로 생성된다. 

이렇게 하는 이유는 발송큐를 가져와서 책종류에 맞게 원가를 조정하는 스레드는 각각 10개가 생성되는데 이 스레드가 결과를 해당테이블에 인서트하는 것은 총 30개의 스레드가 Database에 컨넥션을 맺는다는 의미이다. 그렇게 되면 쓸데없는 낭비를 초래하고 만다. 따라서 결과만 처리해주는 스레드를 
각각 3개씩 만들고(소설, 비즈니스, 예술) 그 스레드가 결과큐에 입려된 값을 가지고 와서 DB에 각각 테이블에 인서트하는 작업을 한다. 
그렇게 되면 총 9개의 스레드만 DB에 붙어서 결과를 처리하게 된다. 
그래서 이 메인클래스에서는 큐를 각각 만들어주고, 그 큐를 처리하는 스레드를 생성하는 역할을 한다. 

----------------
###BookPutQueueThread (Work Thread & Producer)
>_test_board_origin의 값이 있을 때까지 대기하는  WorkerThread이자 값이 있다면 발송큐에 넣어주는 Producer입니다._

test_board_origin에서 bookType에 맡게 각각 BlockingQueue에 넣어주는  스레드이다.
총 3개(소설, 비즈니스, 예술)의 스레드가 생성되어 자신에게 해당되는 레코드가 있다면 읽어들여 발송큐에 넣어준다. 
DB접속이 이루어진다.

----------------
###BookGetSendThread (Work Thread & Producer & Consumer)
>_발송큐에 값이 있을때까지 대기하는 Consumer & WorkerThread이자 값이 있다면 처리후에 결과큐에 넣어주는 Producer입니다._

bookType에 해당되는 발송큐를 읽어들여 원래 책값의 10%을 판매액으로 변경한후에 결과큐에 넣어준다. 
각 분야별로 10개의 클래스가 생성된다. (총 30개)
DB접속이 발생되지 않는다

---------------

###BookResultInsertThread (Work Thread & Consumer)
>_결과큐에 값이 있을때까지 대기하는 WorkerThread이자 값이 있다면 처리후에 결과큐에 넣어주는 Consumer입니다._

해당분야의 결과큐에 있는 값을 가져와서 해당테이블에 인서트한다. 각 분야당 각각 3개의 스레드가 생성된다. (총 9개)
DB접속이 이루어진다.


4. 실행방법
----------------
eclipe에 gradle plug-in이 사전에 설치되어 있어야 합니다. 
eclipse에서 import > Gradle > Gradle Project 를 통해서 생성하면 됩니다. 
src/main/resource/installTable.sql 에 생성테이블과 실행프로시저가 있으니 테이블을 만들고 프로시저를 실행하고 나서 
BookMain를 실행시키면 됩니다. 또한 BookMain이 실행된 상태에서 프로시저를 호출해도 작동이 됩니다.


