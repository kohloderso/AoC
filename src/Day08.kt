import kotlin.math.min

fun main() {

    fun countAndMarkVisible(grid: Array<Array<MarkedInt>>): Int {
        // add all leftmost trees
        var sum = 0
        for (line in grid) {
            var maxTree = -1
            for (i in line.indices) {
                val tree = line[i].value
                if(tree > maxTree) {
                    maxTree = tree
                    if(!line[i].marked) {
                        sum += 1
                        line[i].marked = true
                    }
                }
            }
        }
        return sum
    }


    // Sanity check
    fun countMarked(grid: Array<Array<MarkedInt>>): Int {
        return grid.sumOf{it.filter { it.marked }.size}
    }

    fun countTreesOneDir(line: List<Int>, height: Int): Int {
        val visibleTrees = line.takeWhile { it < height}.size
        return min(visibleTrees+1, line.size)
    }

    fun scenicScore(i: Int, j: Int, grid: Array<Array<Int>>, transposed: Array<Array<Int>>): Int {
        var scenicScore = 1
        val treeHeight = grid[i][j]
        scenicScore *= countTreesOneDir(grid[i].drop(j+1), treeHeight)
        scenicScore *= countTreesOneDir(grid[i].take(j).reversed(), treeHeight)
        scenicScore *= countTreesOneDir(transposed[j].drop(i+1), treeHeight)
        scenicScore *= countTreesOneDir(transposed[j].take(i).reversed(), treeHeight)
        return scenicScore
    }

    fun part1(grid: Array<Array<MarkedInt>>): Int {
        val transposed = transpose(grid)
        var sum = countAndMarkVisible(grid)
        sum += countAndMarkVisible(transposed)
        grid.forEach {it.reverse()}
        transposed.forEach {it.reverse()}
        sum += countAndMarkVisible(grid)
        sum += countAndMarkVisible(transposed)
        //check(sum == countMarked(grid))
        return sum
    }

    fun part2(grid: Array<Array<Int>>): Int {
        val transposed = transpose(grid)
        var maxScenicScore = -1
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val sScore = scenicScore(i, j, grid, transposed)
                if(sScore > maxScenicScore)
                    maxScenicScore = sScore
            }
        }
        return maxScenicScore
    }


//    val test = parseMarkedIntGrid("Day08_test")
//    check(part1(test.toTypedArray()) == 21)
//
//    val grid = parseMarkedIntGrid("Day08")
//    println(part1(grid.toTypedArray()))

//    val test = parseIntGrid("Day08_test")
//
//    check(scenicScore(1, 2, test.toTypedArray(), transpose(test.toTypedArray())) == 4)
//    check(scenicScore(3, 2, test.toTypedArray(), transpose(test.toTypedArray())) == 8)
//    check(part2(test.toTypedArray()) == 8)
    val input = parseIntGrid("Day08")
    println(part2(input.toTypedArray()))

}
