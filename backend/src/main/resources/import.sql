INSERT INTO ORGANIZATION (ID, NAME, DESCRIPTION, CREATED_AT, UPDATED_AT)
VALUES (1, 'Studienvertretung Architektur', 'Studienvertretung Architektur', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Studienvertretung Bau- und Umweltingenieurwesen', 'Eine FS', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'Studienvertretung Doktorat', 'Beschreibung', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'Studienvertretung Elektrotechnik', 'Test', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'Studienvertretung Informatik', 'Hilft den INF Studierenden!', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'Studienvertretung Lehramt', 'Fragen bitte per E-Mail',  CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'Studienvertretung Maschinenbau und Verfahrenstechnik', 'For you', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'Studienvertretung Raumplanung', 'der TU', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'Studienvertretung Technische Chemie', 'Chemie ist toll!', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (10, 'Studienvertretung Technische Mathematik', 'Wir sind die Zukunft', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (11, 'Studienvertretung Technische Physik', 'test', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (12, 'Studienvertretung Geodäsie und Geoinformation', 'GEO macht Freude', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO CALENDAR (ID, NAME, DESCRIPTION, CREATED_AT, UPDATED_AT)
VALUES (1, 'Events', 'Events der fs::arch', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Party', '#BYOB', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'Tutorien', 'E-Tuts von FSCH', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'Festln', 'Feier der FET. Nur für ÖH-Mitglieder.', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'Vorträge', 'Vortragsreihe "Wie genießt man Schwechater richtig?"', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'Inskriptionsberatung', 'Angebot verschiedener Studienvertretungen sorgen für eine reibungslose Inskription.', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'Essen', 'Gratis-Essen-Kalender von Herrn Michi Sabo', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'Treffen', 'Meet and greets with your profs.', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


INSERT INTO ORGANIZATION_CALENDAR (ORGANIZATION_ID, CALENDAR_ID)
VALUES (1, 1),
       (2, 2),
       (9, 3),
       (4, 4),
       (3, 5),
       (4, 6),
       (4, 7),
       (4, 8),
       (5, 7),
       (8, 1),
       (9, 5),
       (10, 6),
       (12, 6);

INSERT INTO USER (ID, EMAIL, IS_SYSADMIN, NAME, PASSWORD, CREATED_AT, UPDATED_AT)
VALUES (1, 'dillon@demo.whattudo.at', false, 'Dillon Dingle', '$2a$10$ua4K4vhX75mvmEWkHhckeuXfwf0/R7IbeJ1SyOCA0f1OkkC6dJ7Sa',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'troy@demo.whattudo.at', false, 'Troy Tipping', '$2a$10$lv8iS4LRtwu64rvoxSi9VexKb81nW1RHWYqrtjjI4mJ/XF6CkRqWW',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'tess@demo.whattudo.at', false, 'Tess Tenney', '$2a$10$kukXfWJY7i9lpaDbj5FtXuXWnr2QXBGOOjj4FwPHzi3ZhXBZbnA.K',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'levi@demo.whattudo.at', false, 'Levi Lenz', '$2a$10$3NmbvGh5gc7nqc1lHmX5nOiBusenJ.yakcao19xMHGgukZHrXqhKK',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'lorenzo@demo.whattudo.at', false, 'Lorenzo Landers', '$2a$10$crKBS1KD3LvSAVnBOUeL9.CgDtT.Ce0fUgi/tlnwhYkx0C7moUzfu',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'hasnain@demo.whattudo.at', false, 'Hasnain Hurley', '$2a$10$wNAkyTOQZayMc6H.lKeIg.qJTo/15X2tmv44VLgGzuuQCVuDmfZvW',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (7, 'barnaby@demo.whattudo.at', false, 'Barnaby Burger', '$2a$10$r1KdHV7FEYkmdeTACjHpiORwMSUX3J4xqs0gkwjzaDK3vegMtOkH.',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, 'leon@demo.whattudo.at', false, 'Leon Luke', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (9, 'admin@demo.whattudo.at', true, 'Admin', '$2a$10$fATtjZH3NLyI0gWujkUv..ZAzF2p3to8r3BUjIGVUVOJpd6JtZK0S',
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO ORGA_MEMBERSHIP (ORGANIZATION_ID, USER_ID, ROLE)
VALUES (1, 1, 'MOD'),
       (1, 2, 'MEMBER'),
       (2, 1, 'MEMBER'),
       (2, 3, 'MEMBER'),
       (4, 5, 'MEMBER'),
       (4, 6, 'MEMBER'),
       (4, 1, 'MEMBER'),
       (5, 7, 'MEMBER'),
       (5, 3, 'MOD');

INSERT INTO LOCATION (ID, NAME, ADDRESS, ZIP, LATITUDE, LONGITUDE, CREATED_AT, UPDATED_AT)
VALUES (1, 'FS Winf', 'Favoritenstraße 9-11', '1040', 48.195190, 16.369630, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (2, 'Resselpark', 'Resselpark 1', '1040', 48.199478, 16.369150, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (3, 'FS Inf', 'Treitlstraße 3', '1040', 48.198860, 16.367411, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, 'Freihaus', 'Wiedner Hauptstraße 8-10', '1040', 48.198860, 16.367410, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (5, 'EI 7', 'Gußhausstraße 25', '1040', 48.196750, 16.370490, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (6, 'AudiMax', 'Getreidemarkt 9', '1060', 48.201229, 16.363539, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO EVENT (ID, END_DATE_TIME, NAME, START_DATE_TIME, LOCATION_ID, CALENDAR_ID, CREATED_AT, UPDATED_AT)
VALUES (1, '2020-09-24 23:00:00', 'Bararbend der FS Architektur', '2020-09-24 16:00:00', 2, 1, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (2, '2020-09-26 14:00:00', 'Online-Beratung', '2020-09-26 10:00:00', null, 6, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (3, '2020-09-22 11:30:00', 'Gemeinsames Frühstück mit den Studienvertretungen', '2020-09-22 09:00:00', 3, 7,
        CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (4, '2020-09-26 18:00:00', 'Studienvertretungs-Rally', '2020-09-26 12:00:00', 5, 4, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (5, '2020-09-27 12:00:00', 'Seminar zum ES-Tut', '2020-09-27 10:30:00', 4, 3, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (6, '2020-09-23 15:00:00', 'Tutorium zum Thema Studienabschluss', '2020-09-22 14:00:00', 4, 5, CURRENT_TIMESTAMP(),
        CURRENT_TIMESTAMP()),
       (7, '2020-10-01 15:00:00', 'Bolder-Tut', '2020-10-01 14:00:00', 1, 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
       (8, '2020-10-02 15:00:00', 'Semesterabschlussfeier', '2020-10-02 14:00:00', 6, 8, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());


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
VALUES (1, '2020-09-15 11:30:00', '2020-09-15 13:30:00', 'War toll! Lots of fun and so on~!', 1, 1),
       (2, '2020-09-15 15:30:00', '2020-09-15 17:30:00', 'Das Event war schon?!', 1, 2),
       (3, '2020-09-15 15:30:00', '2020-09-15 17:30:00', 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
       1,1);
