BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Artist" (
	"artist_id"	INTEGER,
	"artist_name"	TEXT,
	"genre"	TEXT,
	"songs"	TEXT,
	PRIMARY KEY("artist_id")
);
CREATE TABLE IF NOT EXISTS "Song" (
	"song_id"	INTEGER,
	"song_title"	TEXT,
	"song_artist"	TEXT,
	PRIMARY KEY("song_id")
);
INSERT INTO "Artist" ("artist_id","artist_name","genre","songs") VALUES 
 (101,'Adele','Pop','Rolling in the Deep, Someone Like You'),
 (102,'Ed Sheeran','Pop','Shape of You, Perfect'),
 (103,'Michael Jackson','Pop','Billie Jean, Beat It'),
 (104,'The Beatles','Rock','Hey Jude, Let It Be'),
 (105,'Queen','Rock','Bohemian Rhapsody, We Will Rock You')
 (106,'Ariana Grande', 'Pop', 'Thank U, Next, Problem'),
 (107,'Elton John', 'Rock', 'Rocket Man, Your Song'),
 (108,'Drake', 'Hip-Hop', 'In My Feelings, Hotline Bling'),
 (109,'Dua Lipa', 'Pop', 'New Rules, Levitating'),
 (110,'Queen', 'Rock', 'Radio Ga Ga, We Are the Champions'),
 (112,'Kendrick Lamar', 'Hip-Hop', 'HUMBLE., Swimming Pools (Drank)'),
 (113,'Alicia Keys', 'R&B', 'No One, Fallin'''),
 (114,'John Lennon', 'Rock', 'Imagine, Jealous Guy'),
 (115,'Billie Eilish', 'Pop', 'Bad Guy, Ocean Eyes'),
 (116,'Bob Marley', 'Reggae', 'No Woman, No Cry, One Love');
INSERT INTO "Song" ("song_id","song_title","song_artist") VALUES 
 (1,'Rolling in the Deep','Adele'),
 (2,'Shape of You','Ed Sheeran'),
 (3,'Billie Jean','Michael Jackson'),
 (4,'Hey Jude','The Beatles'),
 (5,'Bohemian Rhapsody','Queen')
 (6,'Thank U, Next', 'Ariana Grande'),
 (7,'Rocket Man', 'Elton John'),
 (8,'In My Feelings', 'Drake'),
 (9,'New Rules', 'Dua Lipa'),
 (10,'Radio Ga Ga', 'Queen'),
 (11,'HUMBLE.', 'Kendrick Lamar'),
 (12,'No One', 'Alicia Keys'),
 (13,'Imagine', 'John Lennon'),
 (14,'Bad Guy', 'Billie Eilish'),
 (15,'No Woman, No Cry', 'Bob Marley');
COMMIT;
