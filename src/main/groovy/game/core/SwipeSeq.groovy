package game.core

private static int[] shiftTo(int[] input, RowSwipeDirection direction) {
    List<Integer> inputList = input.clone() as List<Integer> // for removeAll
    int zerosCount = inputList.count { it == 0 }
    inputList.removeAll { it == 0 }
    return ((direction == RowSwipeDirection.HEAD) ? (inputList + [0] * zerosCount) : ([0] * zerosCount + inputList)) as int[]
}

static int[] swipeSeq(int[] input, RowSwipeDirection direction) {
    input = shiftTo(input, direction)

    (0..(input.length - 2)).each { int i ->
        if (input[i] == input[i + 1] && input[i] != 0) {
            input[i] *= 2
            input[i + 1] = 0
        }
    }

    return shiftTo(input, direction)
}
