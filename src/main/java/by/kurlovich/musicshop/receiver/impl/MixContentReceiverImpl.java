package by.kurlovich.musicshop.receiver.impl;

import by.kurlovich.musicshop.entity.Content;
import by.kurlovich.musicshop.entity.SearchData;
import by.kurlovich.musicshop.receiver.EntityReceiver;
import by.kurlovich.musicshop.receiver.ReceiverException;
import by.kurlovich.musicshop.repository.EntityRepository;
import by.kurlovich.musicshop.repository.RepositoryException;
import by.kurlovich.musicshop.repository.Specification;
import by.kurlovich.musicshop.repository.impl.MixContentRepositoryImpl;
import by.kurlovich.musicshop.repository.specification.GetAllMixesContentSpecification;
import by.kurlovich.musicshop.repository.specification.GetMixContentByMixIdSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class MixContentReceiverImpl implements EntityReceiver<Content> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MixContentReceiverImpl.class);

    @Override
    public boolean addNewEntity(Content item) throws ReceiverException {
        try {
            EntityRepository<Content> entityRepository = new MixContentRepositoryImpl();

            if (item.getTrackName().isEmpty()) {
                return false;
            }

            switch (entityRepository.getStatus(item)) {
                case ACTIVE:
                    LOGGER.debug("found active content for: {}", item.getTrackName());
                    return false;
                case DELETE:
                    LOGGER.debug("found deleted content for: {}", item.getTrackName());
                    entityRepository.undelete(item);
                    return true;
                case NA:
                    entityRepository.add(item);
                    return true;
                default:
                    return false;
            }

        } catch (RepositoryException e) {
            throw new ReceiverException("Exception in addNewEntity of MixContentReceiverImpl.\n" + e, e);
        }
    }

    @Override
    public boolean deleteEntity(Content item) throws ReceiverException {
        try {
            EntityRepository<Content> entityRepository = new MixContentRepositoryImpl();
            LOGGER.debug("trying to delete content from mix.");

            if (item.getTrackId().isEmpty()) {
                return false;
            }

            entityRepository.delete(item);
            return true;

        } catch (RepositoryException e) {
            throw new ReceiverException("Exception in deleteEntity of MixContentReceiverImpl.\n" + e, e);
        }
    }

    @Override
    public boolean updateEntity(Content item) throws ReceiverException {
        return false;
    }

    @Override
    public List<Content> getAllEntities() throws ReceiverException {
        try {
            EntityRepository<Content> entityRepository = new MixContentRepositoryImpl();
            Specification specification = new GetAllMixesContentSpecification();
            LOGGER.debug("Trying to getId content for the mix.");

            return entityRepository.query(specification);

        } catch (RepositoryException e) {
            throw new ReceiverException("Exception in getAllEntities from MixContentReceiverImpl.\n" + e, e);
        }
    }

    @Override
    public List<Content> getSearchedEntities(SearchData searchData, String userId) throws ReceiverException {
        return Collections.emptyList();
    }

    @Override
    public List<Content> getSpecifiedEntities(String param) throws ReceiverException {
        try {
            EntityRepository<Content> entityRepository = new MixContentRepositoryImpl();
            Specification specification = new GetMixContentByMixIdSpecification(param);
            LOGGER.debug("trying to getId specified content for mix with id {}.", param);

            return entityRepository.query(specification);

        } catch (RepositoryException e) {
            throw new ReceiverException("Exception in getSpecifiedEntities of MixContentReceiverImpl.\n" + e, e);
        }
    }
}
