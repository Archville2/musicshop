package by.kurlovich.musicshop.repository.dbconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPool.class);
    private static final String DB_PROPERTIES_FILE = "db.properties";
    private static ReentrantLock lock = new ReentrantLock();
    private static ConnectionPool instance;
    private BlockingQueue<ProxyConnection> connectionsQueue;
    private String dbDriver;
    private String connectionUrl;
    private String userName;
    private String password;
    private int maxConnections;
    private AtomicInteger currentConnections;

    private ConnectionPool() throws ConnectionException {
        paramInit();
        driverInit();
        connectionsInit();
    }

    public static ConnectionPool getInstance() throws ConnectionException {
        lock.lock();
        if (instance == null) {
            instance = new ConnectionPool();
        }

        lock.unlock();
        return instance;
    }

    public ProxyConnection getConnection() throws ConnectionException {
        ProxyConnection connection = null;
        lock.lock();
        try {
            if (!connectionsQueue.isEmpty()) {
                connection = connectionsQueue.take();

                if (connection.isClosed()) {
                    connection = getConnection();
                }

                currentConnections.decrementAndGet();

                LOGGER.debug("Get dbconnection. Connections left: {}.", currentConnections);
                return connection;

            } else {
                return newConnection();
            }

        } catch (InterruptedException | SQLException e) {
            throw new ConnectionException("Problems with getting connections from pool.\n" + e, e);
        } finally {
            lock.unlock();
        }
    }

    void releaseConnection(ProxyConnection connection) throws ConnectionException {
        lock.lock();
        try {
            if (connection != null) {
                if (currentConnections.intValue() < maxConnections) {

                    connectionsQueue.put(connection);
                    currentConnections.incrementAndGet();

                    LOGGER.debug("Release dbconnection. Connections left: {}.", currentConnections);
                } else {
                    LOGGER.debug("Connection pool is full. Closing connection.");
                    connection.getConnection().close();
                }
            }
        } catch (InterruptedException | SQLException e) {
            throw new ConnectionException("Problems in dbconnection queue.\n" + e, e);
        } finally {
            lock.unlock();
        }
    }

    void closeConnection(ProxyConnection connection) throws ConnectionException {
        try {
            connection.getConnection().close();
        } catch (SQLException e) {
            throw new ConnectionException("Problems in dbconnection closeConnection.\n" + e, e);
        }
    }

    private void paramInit() throws ConnectionException {
        try {
            InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
            Properties properties = new Properties();
            if (properties != null) {

                properties.load(inputStream);

                this.dbDriver = properties.getProperty("dbDriver");
                this.connectionUrl = properties.getProperty("url");
                this.userName = properties.getProperty("username");
                this.password = properties.getProperty("password");
                this.maxConnections = Integer.parseInt(properties.getProperty("maxConnections"));
                this.currentConnections = new AtomicInteger(maxConnections);
                this.connectionsQueue = new LinkedBlockingQueue<>(maxConnections);
            } else {
                throw new ConnectionException("Can't find " + DB_PROPERTIES_FILE + " file with db properties.");
            }
            LOGGER.debug("db pool parameters initialized.");
        } catch (IOException e) {
            throw new ConnectionException("Can't load parameters in dbconnection pool.\n" + e, e);
        }
    }

    private void driverInit() throws ConnectionException {
        try {
            Class.forName(dbDriver).newInstance();

            LOGGER.debug("db driver initialized.");
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new ConnectionException("Can't load db driver in dbconnection pool.\n" + e, e);
        }
    }

    private void connectionsInit() throws ConnectionException {
        lock.lock();
        try {
            for (int i = 0; i < maxConnections; i++) {

                if (newConnection() != null) {
                    connectionsQueue.put(newConnection());
                }

                LOGGER.debug("db pool {} connections initialized.", maxConnections);

            }
        } catch (InterruptedException e) {
            throw new ConnectionException("Problems in dbconnection queue.\n" + e, e);
        } finally {
            lock.unlock();
        }
    }

    private ProxyConnection newConnection() throws ConnectionException {
        ProxyConnection proxy;
        try {
            Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
            proxy = new ProxyConnection(connection);
        } catch (SQLException e) {
            throw new ConnectionException("Exception in newConnection of ConnectionPool.\n" + e, e);
        }

        return proxy;
    }
}
