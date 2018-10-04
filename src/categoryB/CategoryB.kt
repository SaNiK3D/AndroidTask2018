package categoryB

import java.util.*
import kotlin.math.abs

fun taskB1() {
    val brackets = "([ ] [{ }] ) [ ({}) ]({[]}) {[ ()] }"

    val bracketsPairs = mapOf(
            Pair(')', '('),
            Pair(']', '['),
            Pair('}', '{')
    )

    val openBracketsStack = Stack<Char>()
    for (ch in brackets) {
        if (ch == ' ') continue

        // если встречается открывающаяся скобка, то кладем ее в стек
        if (ch !in bracketsPairs.keys) {
            openBracketsStack.push(ch)
        }
        // в противном случае нам встретилась закрывающаяся скобка,
        // тогда проверяем, является ли предыдущая открывающаяся скобка ее парой. Для этого извлекаем из стека верхнюю скобку
        else {
            if (openBracketsStack.isEmpty() || openBracketsStack.pop() != bracketsPairs[ch]) {
                println("FAIL")
                return
            }
        }
    }

    println("SUCCESS")
}

fun taskB2Spiral() {
    val n = 3

    val rand = Random()
    val arr = IntArray(n * n) { rand.nextInt(10) }.sortedArray() // генерируем и сразу сортируем массив
    var indexOfCurrentElement = 0

    val matrix = Array(n) { IntArray(n) }
    var heightSteps = n
    var widthSteps = n

    var x = 0
    var y = 0
    var direction = DirectionSpiral.Right

    // пока остается место в ширину и в высоту, заполняем матрицу
    while (heightSteps != 0 || widthSteps != 0) {
        when (direction) {
            DirectionSpiral.Right -> {
                // заполняем горизонтальный ряд; его ширина определяется переменной widthSteps
                repeat(widthSteps) { matrix[x][y++] = arr[indexOfCurrentElement++] }
                y-- // возвращаеся на шаг назад, чтобы не выйти за границу матрицы

                heightSteps-- // уменьшаем количество шагов высоту, так как заполнился горизонтальный ряд
                direction = DirectionSpiral.Down
                x++ // перемещаеся вниз, чтобы не перезаписать последний элемент в горизонтальном ряду
            }

            DirectionSpiral.Down -> {
                repeat(heightSteps) { matrix[x++][y] = arr[indexOfCurrentElement++] }
                x--

                widthSteps--
                direction = DirectionSpiral.Left
                y--
            }
            DirectionSpiral.Left -> {
                repeat(widthSteps) { matrix[x][y--] = arr[indexOfCurrentElement++] }
                y++

                heightSteps--
                direction = DirectionSpiral.Up
                x--
            }
            DirectionSpiral.Up -> {
                repeat(heightSteps) { matrix[x--][y] = arr[indexOfCurrentElement++] }
                x++

                widthSteps--
                direction = DirectionSpiral.Right
                y++
            }
        }
    }

    for (rows in matrix) {
        for (elem in rows) {
            print(elem)
        }
        println()
    }
}

fun taskB2Snake() {
    val n = 3

    val rand = Random()
    val arr = IntArray(n * n) { rand.nextInt(10) }.sortedArray()
    var indexOfCurrentElement = 0

    val matrix = Array(n) { IntArray(n) }

    var x = 0
    var y = 0
    var direction = DirectionSnake.UpRight

    // пока не вышли за пределы матрицы, заполняем ее элементами из массива
    // алгоритм закончит работу, когда мы заполним нижнюю правую ячейку и сдвинемся за переделы матрицы
    while (x <= n - 1 || y <= n - 1) {
        when (direction) {
            DirectionSnake.UpRight -> {
                // сдвигаемся вверх и вправо и заполняем матрицу
                while (x >= 0 && y <= n - 1) {
                    matrix[x--][y++] = arr[indexOfCurrentElement++]
                }
                x++
                y-- // возвращаемся в пределы матрицы

                // если есть возможность - сдвигаемся вправо, иначе вниз
                if (y + 1 <= n - 1) y++ else x++

                direction = DirectionSnake.DownLeft
            }
            DirectionSnake.DownLeft -> {
                while (x <= n - 1 && y >= 0) {
                    matrix[x++][y--] = arr[indexOfCurrentElement++]
                }
                x--
                y++

                // если есть возможность - сдвигаемся вниз, иначе вправо
                if (x + 1 <= n - 1) x++ else y++

                direction = DirectionSnake.UpRight
            }
        }
    }

    for (rows in matrix) {
        for (elem in rows) {
            print(elem)
        }
        println()
    }
}


fun taskB3() {
    val n = 3

    val rand = Random()
    val source = Array(n){ IntArray(n) {rand.nextInt(100)}}

    class Node(val coords: Pair<Int, Int>, var priority: Int)

    val frontier = PriorityQueue<Node> { o1, o2 -> o1.priority - o2.priority }
    val start = Pair(0, 0)
    val end = Pair(n - 1, n - 1)
    frontier.add(Node(Pair(0, 0), 0))

    val cameFrom = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
    val costSoFar = mutableMapOf<Pair<Int, Int>, Int>()

    cameFrom[start] = null
    costSoFar[start] = 0

    fun Pair<Int, Int>.neighbors() : List<Pair<Int, Int>>  {
        val (x,y) = this
        val listOfNeighbors = mutableListOf<Pair<Int, Int>>()

        if (x + 1 < n) listOfNeighbors.add(Pair(x + 1, y))
        if (x - 1 >= 0) listOfNeighbors.add(Pair(x - 1, y))
        if (y + 1 < n) listOfNeighbors.add(Pair(x, y + 1))
        if (y - 1 >= 0) listOfNeighbors.add(Pair(x, y - 1))

        return listOfNeighbors
    }

    fun heuristic(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
        val (x1, y1) = a
        val (x2, y2) = b
        return abs(x1 - x2) + abs(y1 - y2)
    }

    while (frontier.isNotEmpty()){
        val current = frontier.poll().coords

        if (current == end)
            break

        for (next in current.neighbors()){
            val newCost = costSoFar[current]!! + source[next.first][next.second]
            if (next !in costSoFar || newCost < costSoFar[next]!!){
                costSoFar[next] = newCost
                val priority = newCost + heuristic(end, next)
                frontier.add(Node(next, priority))
                cameFrom[next] = current
            }
        }
    }
    val path = mutableSetOf<Pair<Int, Int>>()
    var current = cameFrom[end]!!
    while (current != start){
        path.add(current)
        current = cameFrom[current]!!
    }

    for (rows in source) {
        for (elem in rows) {
            if (elem < 10)
                print(elem.toString() + "  ")
            else
                print(elem.toString() + " ")
        }
        println()
    }
    println()
    for (x in source.indices) {
        for (y in source.indices) {
            if (x == 0 && y == 0) print("A  ")
            else if (x == n - 1 && y == n - 1) print("B  ")
            else if (Pair(x, y) in path) print("*  ")
            else if (source[x][y] < 10) print(source[x][y].toString() + " ")
            else  print(source[x][y].toString() + " ")
        }
        println()
    }

}


