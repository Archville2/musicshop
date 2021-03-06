package by.kurlovich.musicshop.command.common;

import by.kurlovich.musicshop.command.Command;
import by.kurlovich.musicshop.command.CommandException;
import by.kurlovich.musicshop.util.UserUtil;
import by.kurlovich.musicshop.web.CommandResult;
import by.kurlovich.musicshop.entity.Mix;
import by.kurlovich.musicshop.entity.User;
import by.kurlovich.musicshop.receiver.ReceiverException;
import by.kurlovich.musicshop.receiver.UserReceiver;
import by.kurlovich.musicshop.web.pages.PageStore;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

public class ShowAllMixesCommand implements Command {
    private static final String SHOW_MIXES_PAGE = PageStore.SHOW_MIXES_PAGE.getPageName();
    private UserReceiver receiver;

    public ShowAllMixesCommand(UserReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) throws CommandException {
        try {
            User currentUser = (User) request.getSession(true).getAttribute("user");

            String currentUserId = UserUtil.getId(currentUser);

            List<Mix> allMixes = receiver.getAllMixesWithOwner(currentUserId);
            allMixes.sort(Comparator.comparing(Mix::getName));

            request.getSession(true).setAttribute("mixList", allMixes);
            request.getSession(true).setAttribute("url", SHOW_MIXES_PAGE);
            return new CommandResult(CommandResult.ResponseType.FORWARD, SHOW_MIXES_PAGE);

        } catch (ReceiverException e) {
            throw new CommandException("Exception in ShowAllMixesCommand.\n" + e, e);
        }

    }
}
