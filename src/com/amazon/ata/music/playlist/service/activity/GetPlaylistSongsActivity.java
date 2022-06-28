package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazon.ata.music.playlist.service.models.SongOrder;
import com.amazon.ata.music.playlist.service.models.requests.GetPlaylistSongsRequest;
import com.amazon.ata.music.playlist.service.models.results.GetPlaylistSongsResult;
import com.amazon.ata.music.playlist.service.models.SongModel;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the GetPlaylistSongsActivity for the MusicPlaylistService's GetPlaylistSongs API.
 *
 * This API allows the customer to get the list of songs of a saved playlist.
 */
public class GetPlaylistSongsActivity implements RequestHandler<GetPlaylistSongsRequest, GetPlaylistSongsResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    ModelConverter modelConverter = new ModelConverter();
    //ModelConverter modelConverter;


    /**
     * Instantiates a new GetPlaylistSongsActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlist table.
     */
    @Inject
    public GetPlaylistSongsActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by retrieving the playlist from the database.
     * <p>
     * It then returns the playlist's song list.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     *
     * @param getPlaylistSongsRequest request object containing the playlist ID
     * @return getPlaylistSongsResult result object containing the playlist's list of API defined {@link SongModel}s
     */
    @Override
    public GetPlaylistSongsResult handleRequest(final GetPlaylistSongsRequest getPlaylistSongsRequest, Context context) {
        log.info("Received GetPlaylistSongsRequest {}", getPlaylistSongsRequest);

        Playlist playlist;
        List<SongModel> songModelList;
        ModelConverter modelConverter = new ModelConverter();
//        String requestId = getPlaylistSongsRequest.getId();
//        Playlist playlist;
//        ModelConverter modelConverter = new ModelConverter();

        try {
            playlist = playlistDao.getPlaylist(getPlaylistSongsRequest.getId());
        } catch (PlaylistNotFoundException e) {
            throw new PlaylistNotFoundException(e.getMessage());
        }

        SongOrder songOrder = getPlaylistSongsRequest.getOrder();
        if(songOrder == null) {
            songOrder = SongOrder.DEFAULT;
        }

        if(songOrder == SongOrder.SHUFFLED) {
            Collections.shuffle(playlist.getSongList());
        } else if(songOrder == SongOrder.REVERSED) {
            Collections.reverse(playlist.getSongList());
        }


        songModelList = modelConverter.toSongModelList(playlist.getSongList());

        return GetPlaylistSongsResult.builder()
                .withSongList(songModelList)
                .build();
    }
}
