--Deletion of old database--
DROP DATABASE IF EXISTS "MediaRatingPlatform";
DROP OWNED BY mrp;
--Creation of the database user--
do
$$
    begin
        if not exists (select * from pg_user where usename = 'mrp') then
            CREATE USER mrp with encrypted password 'mrp';
        end if;
    end
$$
;
--Database creation process--
CREATE DATABASE "MediaRatingPlatform" owner mrp;
\c "MediaRatingPlatform"
Alter schema public owner TO mrp;

SET ROLE mrp;

create table "user"
(
    "userId"   serial not null
        constraint user_pk
            primary key,
    username varchar(25) not null UNIQUE,
    password varchar     not null
);

create table genre
(
    "genreId" serial not null
        constraint genre_pk
            primary key,
    name varchar(255) not null
);

create table media
(
    "mediaId" serial not null
        constraint media_pk
            primary key,
    "userId" integer not null,
    title varchar(255) not null,
    description varchar(255) not null,
    "releaseYear" integer not null,
    "averageScore" decimal(1,2) not null,
    "mediaType" varchar(10) not null,
    "ageRestriction" integer not null,
    CONSTRAINT "user_fk" FOREIGN KEY ("userId") REFERENCES public."user"("userId")
);

create table "mediaGenre"
(
    "genreId" integer not null,
        "mediaId" integer not null,
    constraint "mediaGenre_pk"
        primary key ("genreId", "mediaId"),
    CONSTRAINT "media_fk" FOREIGN KEY ("mediaId") REFERENCES public."media"("mediaId"),
    CONSTRAINT "genre_fk" FOREIGN KEY ("genreId") REFERENCES public."genre"("genreId")
);

create table rating
(
    "ratingId" serial not null,
    "mediaId" integer not null,
    "userId" integer not null,
    comment varchar(255) not null,
    star integer not null,
    "isPubliclyVisibile" boolean not null DEFAULT false,
    "createdAt" timestamp without time zone not null,
    constraint "rating_pk"
        primary key ("ratingId"),
    CONSTRAINT "media_fk" FOREIGN KEY ("mediaId") REFERENCES public."media"("mediaId"),
    CONSTRAINT "user_fk" FOREIGN KEY ("userId") REFERENCES public."user"("userId"),
    UNIQUE("mediaId","userId")
);

create table favorites
(
    "mediaId" integer not null,
    "userId" integer not null,
    constraint favorites_pk
        primary key ("mediaId", "userId"),
            CONSTRAINT "media_fk" FOREIGN KEY ("mediaId") REFERENCES public."media"("mediaId"),
    CONSTRAINT "user_fk" FOREIGN KEY ("userId") REFERENCES public."user"("userId")
);

create table ratingLikes
(
    "ratingId" integer not null,
    "userId" integer not null,
    constraint "ratingLikes_pk"
        primary key ("ratingId", "userId"),
            CONSTRAINT "user_fk" FOREIGN KEY ("userId") REFERENCES public."user"("userId"),
    CONSTRAINT "rating_fk" FOREIGN KEY ("ratingId") REFERENCES public."rating"("ratingId")
);