package game

private static List<Integer> shiftTo(List<Integer> input, SwipeDirection direction) {
    input = input.clone() as List<Integer>
    int zerosCount = input.count { it == 0 }
    input.removeAll { it == 0 }
    return (direction == SwipeDirection.HEAD) ? (input + [0] * zerosCount) : ([0] * zerosCount + input)

}

static List<Integer> swipe(List<Integer> input, SwipeDirection direction) {
    input = shiftTo(input, direction)

    (0..(input.size() - 2)).each { int i ->
        if (input[i] == input[i + 1] && input[i] != 0) {
            input[i] *= 2
            input[i + 1] = 0
        }
    }

    return shiftTo(input, direction)
}
