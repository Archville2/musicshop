package by.kurlovich.musicshop.command.common;

import by.kurlovich.musicshop.command.Command;
import by.kurlovich.musicshop.content.CommandResult;
import by.kurlovich.musicshop.web.pages.PageStore;

import javax.servlet.http.HttpServletRequest;

public class ShowErrorPageCommand implements Command {
    private final static String ERROR_PAGE = PageStore.ERROR_PAGE.getPageName();

    public ShowErrorPageCommand() {
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {

        return new CommandResult(CommandResult.ResponseType.FORWARD, ERROR_PAGE);
    }
}
