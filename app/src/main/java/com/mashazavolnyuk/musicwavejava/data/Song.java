package com.mashazavolnyuk.musicwavejava.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Song implements Parcelable {

    public static final Song EMPTY_SONG = new Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "");

    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final String title;
    public final int trackNumber;
    public final int year;
    public final long duration;
    public final String data;
    public final long dateModified;
    public final int albumId;
    public final String albumName;
    public final int artistId;
    public final String artistName;


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

    protected Song(Parcel in) {
        id = in.readInt();
        title = in.readString();
        trackNumber = in.readInt();
        year = in.readInt();
        duration = in.readLong();
        data = in.readString();
        dateModified = in.readLong();
        albumId = in.readInt();
        albumName = in.readString();
        artistId = in.readInt();
        artistName = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(trackNumber);
        parcel.writeInt(year);
        parcel.writeLong(duration);
        parcel.writeString(data);
        parcel.writeLong(dateModified);
        parcel.writeInt(albumId);
        parcel.writeString(albumName);
        parcel.writeInt(artistId);
        parcel.writeString(artistName);
    }
}
