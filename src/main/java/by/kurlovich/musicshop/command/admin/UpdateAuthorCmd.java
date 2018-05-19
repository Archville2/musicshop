package by.kurlovich.musicshop.command.admin;

import by.kurlovich.musicshop.command.CommandException;
import by.kurlovich.musicshop.entity.Author;
import by.kurlovich.musicshop.receiver.EntityReceiver;
import by.kurlovich.musicshop.receiver.ReceiverException;

public class UpdateAuthorCmd extends AuthorCommand {
    private EntityReceiver receiver;

    public UpdateAuthorCmd(EntityReceiver receiver) {
        super(receiver);
        this.receiver = receiver;
    }

    @Override
    public boolean doCommand(Author author) throws CommandException {
        try {
            return receiver.updateEntity(author);
        } catch (ReceiverException e) {
            throw new CommandException("Exception in UpdateAuthorCommand.\n" + e, e);
        }
    }
}