INSERT into CATEGORY (id, name) VALUES (1, 'Crime Novel');
INSERT into CATEGORY (id, name) VALUES (2, 'Romance');

INSERT into BOOK (id, category_id, title, price) VALUES (1, 1, 'Lost hours', 9.10);
INSERT into BOOK (id, category_id, title, price) VALUES (2, 1, 'The unquiet grave', 8.70);
INSERT into BOOK (id, category_id, title, price) VALUES (3, 1, 'The cure', 6.35);
INSERT into BOOK (id, category_id, title, price) VALUES (4, 2, 'If I stay', 5.30);
INSERT into BOOK (id, category_id, title, price) VALUES (5, 2, 'The fault in our starts', 4.50);

-- Important redefine the max id
ALTER TABLE BOOK ALTER COLUMN id RESTART WITH 6;
ALTER TABLE CATEGORY ALTER COLUMN id RESTART WITH 3;

-- If a FK exists, drop it first (name may differ in your DB)
ALTER TABLE book DROP CONSTRAINT IF EXISTS fk_book_category;

-- Recreate with ON DELETE SET NULL
ALTER TABLE book
    ADD CONSTRAINT fk_book_category
        FOREIGN KEY (category_id)
            REFERENCES category(id)
            ON DELETE SET NULL;

ALTER TABLE book ALTER COLUMN category_id DROP NOT NULL;