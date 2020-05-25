INSERT INTO ORGANIZATION (ID, NAME)
VALUES (1, 'Fachschaft Architektur'),
       (2, 'Fachschaft Bau- und Umweltingenieurwesen'),
       (3, 'Fachschaft Doktorat'),
       (4, 'Fachschaft Elektrotechnik'),
       (5, 'Fachschaft Informatik'),
       (6, 'Fachschaft Lehramt'),
       (7, 'Fachschaft Maschinenbau und Verfahrenstechnik'),
       (8, 'Fachschaft Raumplanung'),
       (9, 'Fachschaft Technische Chemie'),
       (10, 'Fachschaft Technische Mathematik'),
       (11, 'Fachschaft Technische Physik'),
       (12, 'Fachschaft Geodäsie und Geoinformation'),
       (13, 'Fachschaft Wirtschaftsinformatik und Data Science');


INSERT INTO CALENDAR (ID, NAME)
VALUES (1, 'Events'),
       (2, 'Events'),
       (3, 'Tutorien'),
       (4, 'Festln'),
       (5, 'Tutorien'),
       (6, 'Journaldienst'),
       (7, 'Essen'),
       (8, 'Tuts'),
       (9, 'Events'),
       (10, 'Events'),
       (11, 'Party'),
       (12, 'Tuts'),
       (13, 'Events');

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

INSERT INTO USER (ID, EMAIL, IS_SYSADMIN, NAME, PASSWORD)
VALUES (1, 'person1@tuwien.ac.at', false, 'Person 1', '$2a$10$ua4K4vhX75mvmEWkHhckeuXfwf0/R7IbeJ1SyOCA0f1OkkC6dJ7Sa'),
       (2, 'person2@tuwien.ac.at', false, 'Person 2', '$2a$10$Obqcl.ckGwkVQzOFevPQVuiFIkEGtcrpB.rT68xKrMeJH5Wpjwek.'),
       (3, 'person3@tuwien.ac.at', false, 'Person 3', '$2a$10$lv8iS4LRtwu64rvoxSi9VexKb81nW1RHWYqrtjjI4mJ/XF6CkRqWW'),
       (4, 'person4@tuwien.ac.at', false, 'Person 4', '$2a$10$kukXfWJY7i9lpaDbj5FtXuXWnr2QXBGOOjj4FwPHzi3ZhXBZbnA.K'),
       (5, 'person5@tuwien.ac.at', false, 'Person 5', '$2a$10$3NmbvGh5gc7nqc1lHmX5nOiBusenJ.yakcao19xMHGgukZHrXqhKK'),
       (6, 'person6@tuwien.ac.at', false, 'Person 6', '$2a$10$crKBS1KD3LvSAVnBOUeL9.CgDtT.Ce0fUgi/tlnwhYkx0C7moUzfu'),
       (7, 'person7@tuwien.ac.at', false, 'Person 7', '$2a$10$wNAkyTOQZayMc6H.lKeIg.qJTo/15X2tmv44VLgGzuuQCVuDmfZvW'),
       (8, 'person8@tuwien.ac.at', false, 'Person 8', '$2a$10$r1KdHV7FEYkmdeTACjHpiORwMSUX3J4xqs0gkwjzaDK3vegMtOkH.'),
       (9, 'person9@tuwien.ac.at', false, 'Person 9', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S');

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
