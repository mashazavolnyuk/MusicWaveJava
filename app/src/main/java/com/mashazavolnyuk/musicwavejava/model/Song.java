package com.mashazavolnyuk.musicwavejava.model;

public class Song {

    public static final Song EMPTY_SONG = new Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "");

    public final int id;
    private final String title;
    private final int trackNumber;
    private final int year;
    private final long duration;
    public final String data;
    private final long dateModified;
    private final int albumId;
    private final String albumName;
    private final int artistId;
    private final String artistName;


    public Song(int id, String title, int trackNumber, int year, long duration, String data, long dateModified, int albumId, String albumName, int artistId, String artistName) {
        this.id = id;
        this.title = title;
        this.trackNumber = trackNumber;
        this.year = year;
        this.duration = duration;
        this.data = data;
        this.dateModified = dateModified;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }
}
