package game.network

import groovy.util.logging.Log

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Log
class Server {

    private static final int THREAD_POOL_SIZE = 100
   
    private int port

    private ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    Server(int port) {
        this.port = port
    }

    void start() {
        ServerSocket serverSocket = new ServerSocket(port)
        log.info("Server started at :${port}")

        //noinspection GroovyInfiniteLoopStatement
        while (true) {
            Socket clientSocket = serverSocket.accept()
            log.info("New connection from " + clientSocket.inetAddress)
            pool.submit(new Handler(clientSocket))
        }
    }
}

