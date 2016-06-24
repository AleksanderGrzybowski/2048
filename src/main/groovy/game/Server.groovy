package game

import groovy.util.logging.Log

@Log
class Server {

    private static int DEFAULT_PORT = 5432
    public static String ENV_VAR_PORT = "2048_PORT"
    
    public static void main(String[] args) {
        String envPort = System.getenv(ENV_VAR_PORT)
        int port = (envPort != null) ? Integer.parseInt(envPort) : DEFAULT_PORT

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

