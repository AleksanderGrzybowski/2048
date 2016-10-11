package game.network

class Handler implements Runnable {

    /**
     * I have no idea what does this byte sequence represent, but it works with standard Linux telnet.
     */
    private static List<Character> SET_CHARACTER_MODE_COMMAND = [255, 253, 34, 255, 250, 34, 1, 0, 255, 240, 255,
                                                                 251, 1].collect { it as char }
    private static Integer CHARS_TO_SKIP = 54
    private Socket socket

    Handler(Socket clientSocket) {
        this.socket = clientSocket
    }

    @Override
    void run() {
        InputStream input = socket.inputStream
        OutputStream output = socket.outputStream
        Session session = new Session(new NetworkIO(input, output))

        OutputStreamWriter osw = new OutputStreamWriter(output, "ISO-8859-1")
        SET_CHARACTER_MODE_COMMAND.each { osw.write(it) }
        osw.flush()
        CHARS_TO_SKIP.times { input.read() }

        session.go()
        
        // There should be probably cleanup code here (or try-with-resources), but
        // in this case I don't really mind.
        socket.close()
    }
}
