package by.kurlovich.musicshop.command.admin;

import by.kurlovich.musicshop.command.Command;
import by.kurlovich.musicshop.command.CommandException;
import by.kurlovich.musicshop.content.CommandResult;
import by.kurlovich.musicshop.entity.Content;
import by.kurlovich.musicshop.entity.Mix;
import by.kurlovich.musicshop.entity.Track;
import by.kurlovich.musicshop.store.PageStore;
import by.kurlovich.musicshop.receiver.EntityReceiver;
import by.kurlovich.musicshop.receiver.ReceiverException;
import by.kurlovich.musicshop.receiver.impl.TrackReceiverImpl;
import by.kurlovich.musicshop.validator.AccessValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ShowEditMixesContentPage implements Command {
    private final static String EDIT_MIXES_CONTENT_PAGE = PageStore.EDIT_MIXES_CONTENT_PAGE.getPageName();
    private final static String ERROR_PAGE = PageStore.ERROR_PAGE.getPageName();
    private EntityReceiver receiver;

    public ShowEditMixesContentPage(EntityReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        AccessValidator accessValidator = new AccessValidator();

        try {
            List<String> accessRoles = Arrays.asList("admin");
            String userRole = (String) request.getSession(true).getAttribute("role");

            Set<String> names = new HashSet<>();
            Set<String> authors = new HashSet<>();
            Set<String> genres = new HashSet<>();

            if (accessValidator.validate(accessRoles, userRole)) {
                String mixId = request.getParameter("submit_id");
                EntityReceiver trackReceiver = new TrackReceiverImpl();

                List<Track> trackList = trackReceiver.getAllEntities();

                for (Track track : trackList) {
                    names.add(track.getName());
                    authors.add(track.getAuthor());
                    genres.add(track.getGenre());
                }

                List<Mix> mixList = (List<Mix>) request.getSession(true).getAttribute("mixList");
                List<Content> contentList = receiver.getAllEntities();

                for (Mix mix : mixList) {
                    if (mix.getId().equals(mixId)) {
                        request.getSession(true).setAttribute("currentMix", mix);
                    }
                }

                request.getSession(true).setAttribute("namesSet", names);
                request.getSession(true).setAttribute("authorsSet", authors);
                request.getSession(true).setAttribute("genresSet", genres);
                request.getSession(true).setAttribute("trackList", trackList);
                request.getSession(true).setAttribute("contentList", contentList);
                request.getSession(true).setAttribute("url", EDIT_MIXES_CONTENT_PAGE);
                return new CommandResult(CommandResult.ResponseType.FORWARD, EDIT_MIXES_CONTENT_PAGE);
            }

            request.getSession(true).setAttribute("url", ERROR_PAGE);
            request.setAttribute("message", "denied");
            return new CommandResult(CommandResult.ResponseType.FORWARD, ERROR_PAGE);

        } catch (ReceiverException e) {
            throw new CommandException("Exception in ShowEditMixesContentPage.\n" + e, e);
        }
    }
}