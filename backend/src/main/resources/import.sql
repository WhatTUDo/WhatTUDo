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
       (12, 'Fachschaft Geodäsie und Geoinformation', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (13, 'Fachschaft Wirtschaftsinformatik und Data Science', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO CALENDAR (ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (1, 'Events', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Events', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'Tutorien', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'Festln', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'Tutorien', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'Journaldienst', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'Essen', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'Tuts', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'Events', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (10, 'Events', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (11, 'Party', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (12, 'Tuts', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (13, 'Events', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO ORGANIZATION_CALENDAR (ORGANIZATION_ID, CALENDAR_ID)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 4),
       (3, 5),
       (4, 6),
       (4, 7),
       (4, 8),
       (5, 9),
       (6, 10),
       (6, 11),
       (6, 12),
       (7, 13),
       (8, 1),
       (5, 7);

INSERT INTO USER (ID, EMAIL, IS_SYSADMIN, NAME, PASSWORD, CREATED_AT, UPDATED_AT)
VALUES (1, 'person1@tuwien.ac.at', false, 'Person 1', '$2a$10$ua4K4vhX75mvmEWkHhckeuXfwf0/R7IbeJ1SyOCA0f1OkkC6dJ7Sa', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'person2@tuwien.ac.at', false, 'Person 2', '$2a$10$Obqcl.ckGwkVQzOFevPQVuiFIkEGtcrpB.rT68xKrMeJH5Wpjwek.', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'person3@tuwien.ac.at', false, 'Person 3', '$2a$10$lv8iS4LRtwu64rvoxSi9VexKb81nW1RHWYqrtjjI4mJ/XF6CkRqWW', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'person4@tuwien.ac.at', false, 'Person 4', '$2a$10$kukXfWJY7i9lpaDbj5FtXuXWnr2QXBGOOjj4FwPHzi3ZhXBZbnA.K', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'person5@tuwien.ac.at', false, 'Person 5', '$2a$10$3NmbvGh5gc7nqc1lHmX5nOiBusenJ.yakcao19xMHGgukZHrXqhKK', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'person6@tuwien.ac.at', false, 'Person 6', '$2a$10$crKBS1KD3LvSAVnBOUeL9.CgDtT.Ce0fUgi/tlnwhYkx0C7moUzfu', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'person7@tuwien.ac.at', false, 'Person 7', '$2a$10$wNAkyTOQZayMc6H.lKeIg.qJTo/15X2tmv44VLgGzuuQCVuDmfZvW', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'person8@tuwien.ac.at', false, 'Person 8', '$2a$10$r1KdHV7FEYkmdeTACjHpiORwMSUX3J4xqs0gkwjzaDK3vegMtOkH.', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'person9@tuwien.ac.at', false, 'Person 9', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (10, 'admin1@tuwien.ac.at', true, 'admin', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

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
VALUES (1, '2020-05-26 18:00:00', 'Grillparty der FS Architektur', '2020-05-26 12:00:00', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, '2020-05-28 20:00:00', 'Online-Beratung', '2020-05-28 19:00:00', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, '2020-05-25 11:30:00', 'Gemeinsames Fruehstueck mit den Fachschaften', '2020-05-25 09:00:00', 7, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, '2020-05-29 18:00:00', 'Feier zum 150.Gruendungstag', '2020-05-29 12:00:00', 4, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, '2020-06-02 21:00:00', 'Seminar zum ES-Tut', '2020-06-02 19:30:00', 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, '2020-05-29 15:00:00', 'Tutorium zum Thema Studienabschluss', '2020-05-25 14:00:00', 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO LABEL (ID, NAME, CREATED_AT, UPDATED_AT)
VALUES (1, 'Schnitzelparty',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Kino',CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO LABEL_EVENT (LABEL_ID, EVENT_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 2),
       (1, 4);

