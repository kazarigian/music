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
INSERT INTO "Artist" ("artist_id","artist_name","genre","songs") VALUES (101,'Adele','Pop','Rolling in the Deep, Someone Like You'),
 (102,'Ed Sheeran','Pop','Shape of You, Perfect'),
 (103,'Michael Jackson','Pop','Billie Jean, Beat It'),
 (104,'The Beatles','Rock','Hey Jude, Let It Be'),
 (105,'Queen','Rock','Bohemian Rhapsody, We Will Rock You');
INSERT INTO "Song" ("song_id","song_title","song_artist") VALUES (1,'Rolling in the Deep','Adele'),
 (2,'Shape of You','Ed Sheeran'),
 (3,'Billie Jean','Michael Jackson'),
 (4,'Hey Jude','The Beatles'),
 (5,'Bohemian Rhapsody','Queen');
COMMIT;
