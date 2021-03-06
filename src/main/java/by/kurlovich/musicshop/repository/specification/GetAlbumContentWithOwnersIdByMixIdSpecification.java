package by.kurlovich.musicshop.repository.specification;

import by.kurlovich.musicshop.repository.SqlSpecification;

public class GetAlbumContentWithOwnersIdByMixIdSpecification implements SqlSpecification {
    private String userId;
    private String albumId;

    public GetAlbumContentWithOwnersIdByMixIdSpecification (String userId, String albumId) {
        this.userId = userId;
        this.albumId = albumId;
    }

    @Override
    public String toSqlQuery() {
        return String.format("SELECT t.id, t.name, a.name AS author, g.name AS genre, t.year, t.length, t.status, pt.user_id AS owner FROM tracks t LEFT OUTER JOIN purchased_tracks pt ON pt.track_id = t.id AND pt.user_id='%1$s', genres g, authors a, albums_content ac WHERE t.genre=g.id AND t.author=a.id AND ac.track_id=t.id AND ac.album_id='%2$s' AND ac.status='active' AND t.status='active'", userId, albumId);
    }
}
