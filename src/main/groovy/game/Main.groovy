package game

import game.network.Server

class Main {

    private static int DEFAULT_PORT = 5432
    private static String ENV_VAR_PORT = "2048_PORT"

    static void main(String[] args) {
        String envPort = System.getenv(ENV_VAR_PORT)
        int port = (envPort != null) ? Integer.parseInt(envPort) : DEFAULT_PORT

        new Server(port).start()
    }
}
