package categoryA

fun taskA1() {
    val pointA = Point(1, 1)
    val pointB = Point(0, -1)
    val pointC = Point(-1, -1)
    val pointD = Point(0, 0)

    // Вычисляем координаты векторов AB, BC и CA
    fun createVector(pointStart: Point, pointFinish: Point) = Point(pointFinish.x - pointStart.x, pointFinish.y - pointStart.y)

    val vecAB = createVector(pointA, pointB)
    val vecBC = createVector(pointB, pointC)
    val vecCA = createVector(pointC, pointA)

    // Вычисляем координаты векторов AD, BD и CD
    val vecAD = createVector(pointA, pointD)
    val vecBD = createVector(pointB, pointD)
    val vecCD = createVector(pointC, pointD)

    // k1, k2, k3 - модули векторных произведений AB*AD, BC*BD и CA*CD,
    // которые определяют положение точки D относительно соответсвенных векторов (левее. правее или на векторе/его продолжении).
    // Если k1, k2 и k3 одинаковых знаков, то точка D лежит внутри треугольника
    // Если k1, k2 или k3 равно 0, то точка лежит на векторе или его продолжении, т.е. также внутри треугольника
    fun vectorProduct(vec1: Point, vec2: Point) = vec1.x * vec2.y - vec1.y * vec2.x

    val k1 = vectorProduct(vecAB, vecAD)
    val k2 = vectorProduct(vecBC, vecBD)
    val k3 = vectorProduct(vecCA, vecCD)

    if ((k1 >= 0 && k2 >= 0 && k3 >= 0) || (k1 <= 0 && k2 <= 0 && k3 <= 0)) {
        println("IN")
    } else {
        println("OUT")
    }
}

fun taskA2() {
    val arr = arrayOf(
            intArrayOf(1, 3, 8),
            intArrayOf(9, 2, 1),
            intArrayOf(0, 3, 7)
    )

    val absSubtraction = arr[0][0] + arr[2][2] - arr[0][2] - arr[2][0]
    println(absSubtraction)
}

fun taskA3() {
    val n = 5
    for (i in n downTo 1) {
        val ladderRow = " ".repeat(i - 1).plus("#".repeat(n - i + 1))

        println(ladderRow)
    }
}

@Suppress("ReplaceRangeToWithUntil")
fun taskA4() {
    val arr = intArrayOf(1, 2, 3, 4, 5, 6)
    val k = 5
    var pairNumber = 0
    for (i in 0 .. arr.size - 2) {
        for (j in i + 1 .. arr.size - 1) {
            if ( (arr[i] + arr[j]) % k == 0 ) pairNumber++
        }
    }

    println(pairNumber)
}

fun taskA5() {
    val arr = arrayOf(
            intArrayOf(1,2,3,4,5,6,7,8,9,0),
            intArrayOf(2,2,2,2,1,2,1,2,3,1),
            intArrayOf(2,7,7,8,1,1,2,1,1,0),
            intArrayOf(6,7,7,8,1,2,3,4,6,8)
    )
    val window = arrayOf(
            intArrayOf(1,2,1),
            intArrayOf(1,1,2)
    )
    val windowWidth = window[0].size
    val windowHeight = window.size
    val arrayWidth = arr[0].size
    val arrayHeight = arr.size

    var windowRow : Int
    var windowCol : Int
    for (i in 0..arrayHeight - windowHeight) {
        for (j in 0..arrayWidth - windowWidth) {
            windowRow = 0
            windowCol = 0
            // как только встречаем совпадение с верхней левой цифрой из окна с текущей цифрой из массива,
            // начинаем проверять остальные цифры слева направо сверху вниз
            while (window[windowRow][windowCol] == arr[i + windowRow][j + windowCol]) {
                windowCol++
                if (windowCol == windowWidth) {
                    windowRow++
                    windowCol = 0
                    // если дошли до нижней правой цифры из окна, то выводим ответ и выходим из функции
                    if (windowRow == window.size){
                        println("($i,$j)")
                        return
                    }
                }
            }
        }
    }

    println("FAIL")
}