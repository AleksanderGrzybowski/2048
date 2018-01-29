package game.web

import spark.Request
import spark.Spark

import java.text.MessageFormat

class WebConfig {

    private static final String TEMPLATE = '''Welcome! To play, please connect to game server with telnet command from any Linux box:

$ telnet {0} 2048
'''

    static void init() {
        Spark.port(8080)
        Spark.get('/') { req, res ->
            res.type('text/plain')

            String banner = loadBanner()
            String message = MessageFormat.format(TEMPLATE, extractHostname(req))

            return banner + '\n\n' + message
        }
    }

    private static String loadBanner() {
        System.getResourceAsStream('/banner.txt').readLines().join('\n')
    }

    private static String extractHostname(Request request) {
        String host = request.headers('Host')
        host.contains(':') ? host.substring(0, host.indexOf(':')) : host
    }
}
