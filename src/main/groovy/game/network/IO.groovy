package game.network

interface IO {
    char read()

    void write(String message)
}