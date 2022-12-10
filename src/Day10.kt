import kotlin.math.absoluteValue

fun main() {

    class Device() {
        var register = 1
        var cycle = 0
        var signalStrength = 0
        val image = Array(6) {CharArray(40) {'.'}}

        fun drawPixel(){
            val row = cycle / 40
            val col = cycle % 40
            val sCol = register % 40
            if ((col - sCol).absoluteValue <= 1) {
                image[row][col] = '#'
            }
        }
        fun increment() {
            drawPixel()
            cycle += 1
            if((cycle - 20) % 40 == 0) {
                signalStrength += register*cycle
            }
        }

        fun execAdd(n: Int) {
            increment()
            increment()
            register += n
        }

        fun printImage() {
            for (i in image.indices) {
                for (j in image[i].indices) {
                    print(image[i][j])
                }
                println()
            }
        }
    }

    fun part1(input: List<String>): Device {
        val device = Device()
        for (line in input) {
            val split = line.split(" ")
            when(split[0]) {
                "noop" -> device.increment()
                "addx" -> device.execAdd(split[1].toInt())
            }
        }
        return device
    }

    fun part2(device: Device) {
        device.printImage()
    }


    val test = parseLines("Day10_test")
    val test_device = part1(test)
    check(test_device.signalStrength == 13140)


    val input = parseLines("Day10")
    val device = part1(input)
    println(device.signalStrength)

    //part2(test_device)
    part2(device)


}
