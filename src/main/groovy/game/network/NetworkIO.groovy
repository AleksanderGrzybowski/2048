package game.network

class NetworkIO implements IO {
    
    private InputStream input
    private PrintWriter output

    NetworkIO(InputStream input, OutputStream output) {
        this.input = input
        this.output = new PrintWriter(output)
    }

    @Override
    char read() {
        input.read()
    }

    @Override
    void write(String message) {
        output.write(message)
        output.flush()
    }
}
