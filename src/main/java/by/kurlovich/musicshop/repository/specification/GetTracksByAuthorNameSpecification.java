package by.kurlovich.musicshop.repository.specification;

import by.kurlovich.musicshop.repository.SqlSpecification;

public class GetTracksByAuthorNameSpecification implements SqlSpecification {
    private String authorName;

    public GetTracksByAuthorNameSpecification(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toSqlQuery() {
        return String.format("SELECT t.id, t.name, a.name AS author, g.name AS genre, t.year, t.length, t.status FROM tracks t, genres g, authors a WHERE t.genre=g.id AND t.author=a.id AND a.name='%1$s'", authorName);
    }
}
