INSERT INTO ORGANIZATION (ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (1, 'Fachschaft Architektur', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Fachschaft Bau- und Umweltingenieurwesen', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'Fachschaft Doktorat', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'Fachschaft Elektrotechnik', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'Fachschaft Informatik', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'Fachschaft Lehramt', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'Fachschaft Maschinenbau und Verfahrenstechnik', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'Fachschaft Raumplanung', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'Fachschaft Technische Chemie', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (10, 'Fachschaft Technische Mathematik', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (11, 'Fachschaft Technische Physik', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (12, 'Fachschaft Geodäsie und Geoinformation', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO CALENDAR (ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (1, 'Events', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Party', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'Tutorien', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'Festln', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'Vorträge', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'Journaldienst', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'Essen', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'Party', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'Treffen', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO ORGANIZATION_CALENDAR (ORGANIZATION_ID, CALENDAR_ID)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 4),
       (3, 5),
       (4, 6),
       (4, 7),
       (4, 8),
       (5, 7),
       (5, 9),
       (8, 1),
       (9, 5),
       (10, 6),
       (12, 6);

INSERT INTO USER (ID, EMAIL, IS_SYSADMIN, NAME, PASSWORD, CREATED_AT, UPDATED_AT)
VALUES (1, 'dillon@demo.whattudo.at', false, 'Dillon Dingle', '$2a$10$ua4K4vhX75mvmEWkHhckeuXfwf0/R7IbeJ1SyOCA0f1OkkC6dJ7Sa',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'alexis@demo.whattudo.at', false, 'Alexis Ault', '$2a$10$Obqcl.ckGwkVQzOFevPQVuiFIkEGtcrpB.rT68xKrMeJH5Wpjwek.',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'troy@demo.whattudo.at', false, 'Troy Tipping', '$2a$10$lv8iS4LRtwu64rvoxSi9VexKb81nW1RHWYqrtjjI4mJ/XF6CkRqWW',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'tess@demo.whattudo.at', false, 'Tess Tenney', '$2a$10$kukXfWJY7i9lpaDbj5FtXuXWnr2QXBGOOjj4FwPHzi3ZhXBZbnA.K',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'levi@demo.whattudo.at', false, 'Levi Lenz', '$2a$10$3NmbvGh5gc7nqc1lHmX5nOiBusenJ.yakcao19xMHGgukZHrXqhKK',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'lorenzo@demo.whattudo.at', false, 'Lorenzo Landers', '$2a$10$crKBS1KD3LvSAVnBOUeL9.CgDtT.Ce0fUgi/tlnwhYkx0C7moUzfu',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'hasnain@demo.whattudo.at', false, 'Hasnain Hurley', '$2a$10$wNAkyTOQZayMc6H.lKeIg.qJTo/15X2tmv44VLgGzuuQCVuDmfZvW',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'barnaby@demo.whattudo.at', false, 'Barnaby Burger', '$2a$10$r1KdHV7FEYkmdeTACjHpiORwMSUX3J4xqs0gkwjzaDK3vegMtOkH.',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'leon@demo.whattudo.at', false, 'Leon Luke', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (10, 'admin@demo.whattudo.at', true, 'Admin', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO ORGA_MEMBERSHIP (ORGANIZATION_ID, USER_ID, ROLE)
VALUES (1, 1, 'MOD'),
       (1, 2, 'MEMBER'),
       (2, 1, 'MEMBER'),
       (2, 3, 'MEMBER'),
       (3, 4, 'MEMBER'),
       (4, 5, 'MEMBER'),
       (4, 6, 'MEMBER'),
       (4, 1, 'MEMBER'),
       (5, 7, 'MEMBER'),
       (6, 8, 'MEMBER'),
       (7, 9, 'MEMBER'),
       (8, 9, 'MEMBER'),
       (9, 9, 'MEMBER');


INSERT INTO EVENT (ID, END_DATE_TIME, NAME, START_DATE_TIME, CALENDAR_ID, CREATED_AT, UPDATED_AT)
VALUES (1, '2020-06-24 23:00:00', 'Bararbend der FS Architektur', '2020-06-24 16:00:00', 1, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (2, '2020-06-26 14:00:00', 'Online-Beratung', '2020-06-26 10:00:00', 6, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (3, '2020-06-22 11:30:00', 'Gemeinsames Frühstück mit den Fachschaften', '2020-06-22 09:00:00', 7,
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, '2020-06-26 18:00:00', 'Fachschaft-Rally', '2020-06-26 12:00:00', 4, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (5, '2020-06-27 21:00:00', 'Seminar zum ES-Tut', '2020-06-27 19:30:00', 3, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (6, '2020-06-23 15:00:00', 'Tutorium zum Thema Studienabschluss', '2020-06-22 14:00:00', 5, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (7, '2020-07-01 15:00:00', 'Bolder-Tut', '2020-07-01 14:00:00', 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, '2020-07-02 15:00:00', 'Semesterabschlussfeier', '2020-07-02 14:00:00', 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO LABEL (ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (1, 'Schnitzelparty', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Kino', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO LABEL_EVENT (LABEL_ID, EVENT_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 2),
       (1, 4);

INSERT INTO COMMENT (ID, CREATED_AT, UPDATED_AT, TEXT, EVENT_ID, USER_ID)
VALUES (1, '2020-06-15 11:30:00', '2020-06-15 13:30:00', 'War toll! Lots of fun and so on~!', 1, 1),
       (2, '2020-06-15 15:30:00', '2020-06-15 17:30:00', 'Das Event war schon?!', 1, 2),
       (3, '2020-06-15 15:30:00', '2020-06-15 17:30:00', 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
       1,1);

