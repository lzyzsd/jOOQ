DROP TABLE IF EXISTS t_book_to_book_store;
DROP TABLE IF EXISTS t_book_store;
DROP TABLE IF EXISTS t_book;
DROP TABLE IF EXISTS t_author;
DROP SEQUENCE IF EXISTS s_author_id;
CREATE SEQUENCE s_author_id START WITH 1;

CREATE TABLE t_author (
  id INT NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth INT,
  address VARCHAR(50),

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
);

CREATE TABLE t_book (
  id INT NOT NULL,
  author_id INT NOT NULL,
  co_author_id INT,
  details_id INT,
  title VARCHAR(400) NOT NULL,
  published_in INT NOT NULL,
  language_id INT NOT NULL,
  content_text CLOB,
  content_pdf BLOB,

  rec_version INT,
  rec_timestamp TIMESTAMP,

  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES t_author(id),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (co_author_id) REFERENCES t_author(id)
);

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
);

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id INTEGER NOT NULL,
  stock INTEGER,

  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
                             ON DELETE CASCADE
);

INSERT INTO t_author VALUES (next value for s_author_id, 'George', 'Orwell', '1903-06-25', 1903, null);
INSERT INTO t_author VALUES (next value for s_author_id, 'Paulo', 'Coelho', '1947-08-24', 1947, null);

INSERT INTO t_book VALUES (1, 1, null, null, '1984', 1948, 1, 'To know and not to know, to be conscious of complete truthfulness while telling carefully constructed lies, to hold simultaneously two opinions which cancelled out, knowing them to be contradictory and believing in both of them, to use logic against logic, to repudiate morality while laying claim to it, to believe that democracy was impossible and that the Party was the guardian of democracy, to forget, whatever it was necessary to forget, then to draw it back into memory again at the moment when it was needed, and then promptly to forget it again, and above all, to apply the same process to the process itself -- that was the ultimate subtlety; consciously to induce unconsciousness, and then, once again, to become unconscious of the act of hypnosis you had just performed. Even to understand the word ''doublethink'' involved the use of doublethink..', null, 1, '2010-01-01 00:00:00');
INSERT INTO t_book VALUES (2, 1, null, null, 'Animal Farm', 1945, 1, null, null, null, '2010-01-01 00:00:00');
INSERT INTO t_book VALUES (3, 2, null, null, 'O Alquimista', 1988, 4, null, null, 1, null);
INSERT INTO t_book VALUES (4, 2, null, null, 'Brida', 1990, 2, null, null, null, null);

INSERT INTO t_book_store (name) VALUES
	('Orell Füssli'),
	('Ex Libris'),
	('Buchhandlung im Volkshaus');

INSERT INTO t_book_to_book_store VALUES
	('Orell Füssli', 1, 10),
	('Orell Füssli', 2, 10),
	('Orell Füssli', 3, 10),
	('Ex Libris', 1, 1),
	('Ex Libris', 3, 2),
	('Buchhandlung im Volkshaus', 3, 1);
