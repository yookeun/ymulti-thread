
/*
 * 창고에 있는 책들 
 * bookType = 1: 소설, 2: 비즈니스, 3: 예술
 * processYN = 레코드 처리여부 (Y: 처리됨, N: 미처리)
 */
CREATE TABLE books
(
	bookID INT NOT NULL  AUTO_INCREMENT,
	bookName VARCHAR(100) NOT NULL,
	bookAuthor VARCHAR(20) NOT NULL,
	bookType CHAR(1) NOT NULL, 
	originPrice DOUBLE NOT NULL,
	processYN CHAR(1) NOT NULL DEFAULT 'N',
	registDate DATETIME NOT NULL,
	PRIMARY KEY(bookID)
);


/**
 * 가격이 조정된 소설책들 
 */
CREATE TABLE sell_books_novel
(
	bookID INT NOT NULL  AUTO_INCREMENT,
	bookName VARCHAR(100) NOT NULL,
	bookAuthor VARCHAR(20) NOT NULL,
	bookType CHAR(1) NOT NULL, 
	originPrice DOUBLE NOT NULL,
	sellPrice DOUBLE NOT NULL,
	registDate DATETIME NOT NULL,
	PRIMARY KEY(bookID)
);

/**
 * 가격이 조정된 비즈니스 책들 
 */
CREATE TABLE sell_books_business
(
	bookID INT NOT NULL  AUTO_INCREMENT,
	bookName VARCHAR(100) NOT NULL,
	bookAuthor VARCHAR(20) NOT NULL,
	bookType CHAR(1) NOT NULL, 
	originPrice DOUBLE NOT NULL,
	sellPrice DOUBLE NOT NULL,
	sellDate DATETIME NOT NULL,
	PRIMARY KEY(bookID)
);

/**
 * 가격이 조정된 예술책들 
 */
CREATE TABLE sell_books_art
(
	bookID INT NOT NULL  AUTO_INCREMENT,
	bookName VARCHAR(100) NOT NULL,
	bookAuthor VARCHAR(20) NOT NULL,
	bookType CHAR(1) NOT NULL, 
	originPrice DOUBLE NOT NULL,
	sellPrice DOUBLE NOT NULL,
	registDate DATETIME NOT NULL,
	PRIMARY KEY(bookID)
);