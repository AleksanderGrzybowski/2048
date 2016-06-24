package game.network

class Handler implements Runnable {

    private static List<Character> SET_CHARACTER_MODE_COMMAND = [255, 253, 34, 255, 250, 34, 1, 0, 255, 240, 255,
                                                                 251, 1].collect { it as char }
    public static Integer CHARS_TO_SKIP = 54
    private Socket socket

    Handler(Socket clientSocket) {
        this.socket = clientSocket
    }

    @Override
    void run() {
        InputStream input = socket.inputStream
        OutputStream output = socket.outputStream
        Session session = new Session(new NetworkIO(input, output))

        OutputStreamWriter osw = new OutputStreamWriter(output, "ISO-8859-1");
        SET_CHARACTER_MODE_COMMAND.each { osw.write(it) }
        osw.flush();
        CHARS_TO_SKIP.times { input.read() } // at least standard telnet does this

        session.go()
        socket.close()
    }
}
