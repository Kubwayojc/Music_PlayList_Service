package com.amazon.ata.music.playlist.service.converters;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.SongModel;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelConverter {
    /**
     * Converts a provided {@link Playlist} into a {@link PlaylistModel} representation.
     * @param playlist the playlist to convert
     * @return the converted playlist
     */
    public PlaylistModel toPlaylistModel(Playlist playlist) {
        List<String> tags = null;
        if(playlist.getTags() != null) {
            tags = new ArrayList<>(playlist.getTags());
        }


        return PlaylistModel.builder()
            .withId(playlist.getId())
                .withName(playlist.getName())
                .withCustomerId(playlist.getCustomerId())
                .withSongCount(playlist.getSongCount())
                .withTags(tags)
                .build();
    }
    public SongModel toSongModel(AlbumTrack albumTrack) {
        return SongModel.builder()
                .withAsin(albumTrack.getAsin())
                .withTrackNumber(albumTrack.getTrackNumber())
                .withAlbum(albumTrack.getAlbumName())
                .withTitle(albumTrack.getSongTitle())
                .build();
    }

    public ArrayList<SongModel> toSongModelList(LinkedList<AlbumTrack> songs) {
        ArrayList<SongModel> songModelList = new ArrayList<>();
        for (AlbumTrack song : songs) {
            songModelList.add(toSongModel(song));


//            SongModel songModel = toSongModel(song);
//            songModel.setAsin(song.getAsin());
//            songModel.setTrackNumber(song.getTrackNumber());
//            songModel.setTitle(song.getSongTitle());
//            songModel.setAlbum(song.getAlbumName());
//            songModelList.add(songModel);
        }
        return songModelList;
    }


}
