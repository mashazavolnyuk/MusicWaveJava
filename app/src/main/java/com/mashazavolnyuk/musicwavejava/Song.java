package com.mashazavolnyuk.musicwavejava;

public class Song {

    private String title;
    private String name;
    private String artist;


    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public static class SongBuilder {

        private String title;
        private String name;
        private String artist;

        public void setName(String name) {
            this.name = name;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Song build(){
            Song song = new Song();
            song.title = title;
            song.name = this.name;
            song.artist = this.artist;
            return song;
        }
    }
}
