package by.kurlovich.musicshop.command.admin;

import by.kurlovich.musicshop.command.Command;
import by.kurlovich.musicshop.command.CommandException;
import by.kurlovich.musicshop.web.CommandResult;
import by.kurlovich.musicshop.entity.Content;
import by.kurlovich.musicshop.receiver.EntityReceiver;
import by.kurlovich.musicshop.receiver.ReceiverException;
import by.kurlovich.musicshop.web.pages.PageStore;
import by.kurlovich.musicshop.util.validator.AccessValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DeleteTrackFromAlbumCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTrackFromAlbumCommand.class);
    private static final String EDIT_ALBUMS_CONTENT_PAGE = PageStore.EDIT_ALBUMS_CONTENT_PAGE.getPageName();
    private static final String ERROR_PAGE = PageStore.ERROR_PAGE.getPageName();
    private EntityReceiver receiver;

    public DeleteTrackFromAlbumCommand(EntityReceiver receiver) {

        this.receiver = receiver;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        try {
            List<String> accessRoles = Arrays.asList("admin");
            String userRole = (String) request.getSession(true).getAttribute("role");

            if (AccessValidator.validate(accessRoles, userRole)) {
                Content content = createContent(request);

                LOGGER.debug("Deleting track {} from album {}.", content.getTrackId(), content.getEntityId());

                if (receiver.deleteEntity(content)) {
                    List<Content> currentAlbumContent = receiver.getSpecifiedEntities(content.getEntityId());
                    currentAlbumContent.sort(Comparator.comparing(Content::getTrackName));

                    request.getSession(true).setAttribute("albumContent", currentAlbumContent);
                    request.getSession(true).setAttribute("url", EDIT_ALBUMS_CONTENT_PAGE);
                    return new CommandResult(CommandResult.ResponseType.REDIRECT, EDIT_ALBUMS_CONTENT_PAGE);
                }

                request.setAttribute("message", "undelete");

            } else {
                request.setAttribute("message", "denied");
                request.getSession(true).setAttribute("url", ERROR_PAGE);
            }

            return new CommandResult(CommandResult.ResponseType.FORWARD, ERROR_PAGE);

        } catch (ReceiverException e) {
            throw new CommandException("Exception in DeleteTrackFromAlbumCommand.\n" + e, e);
        }
    }

    private Content createContent(HttpServletRequest request) {
        Content content = new Content();

        content.setEntityId(request.getParameter("submit_album_id"));
        content.setTrackId(request.getParameter("submit_track_id"));

        return content;
    }
}
