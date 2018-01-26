package game.network

class NetworkIO implements IO {

    private final InputStream input
    private final PrintWriter output

    NetworkIO(InputStream input, OutputStream output) {
        this.input = input
        this.output = new PrintWriter(output)
    }

    @Override
    char read() {
        input.read()
    }

    @Override
    void write(String text) {
        output.write(text)
        output.flush()
    }
}
