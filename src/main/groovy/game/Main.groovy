package game

import game.network.Server
import game.web.WebConfig

class Main {

    private static int DEFAULT_PORT = 2048
    private static String ENV_VAR_PORT = '2048_PORT'

    static void main(String[] args) {
        String envPort = System.getenv(ENV_VAR_PORT)
        int port = (envPort != null) ? Integer.parseInt(envPort) : DEFAULT_PORT
        
        WebConfig.init()

        new Server(port).start()
    }
}
