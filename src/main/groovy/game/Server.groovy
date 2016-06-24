package game

import game.network.Handler
import groovy.util.logging.Log

@Log
class Server {

    private int port

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
            new Thread(new Handler(clientSocket)).start()
        }
    }
}

