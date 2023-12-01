package solutions22

import java.io.BufferedReader
import java.io.File
import java.io.FileReader




fun main() {

    abstract class Command(val argument: String = "", val result: List<String> = listOf())
    class CD_OUT : Command()
    class CD(argument: String) : Command(argument)
    class LS(result: List<String>) : Command("", result)
    class CD_ROOT : Command()
    class Item(val id: String, var children: MutableList<Item>?, private val size: Int, val parent: Item?) {
        fun getSize() : Int {
           return children?.sumOf { it.getSize() } ?: size
        }

        fun addChild(child: Item) = children?.add(child)

        val isDir = (children != null)
    }

    fun parseCommands(name: String): List<Command> {
        val file = File("src/input", "$name.txt")
        val commands = mutableListOf<Command>()
        BufferedReader(FileReader(file)).use { br ->
            var result = mutableListOf<String>()
            var readLSResult = false
            for (line in br.lines()) {
                val splitLine = line.split(" ")
                if (splitLine[0] == "$") {
                    if (readLSResult) {
                        commands.add(LS(result))
                        readLSResult = false
                    }
                    if (splitLine[1] == "ls") {
                        readLSResult = true
                        result = mutableListOf()
                    } else if (splitLine[1] == "cd") {
                        when (splitLine[2]) {
                            ".." -> commands.add(CD_OUT())
                            "/" -> commands.add(CD_ROOT())
                            else -> commands.add(CD(splitLine[2]))
                        }
                    }
                }
                else result.add(line)
            }
            if (readLSResult) {
                commands.add(LS(result))
            }
        }
        return commands
    }

    fun parseLSResult(list: List<String>, parent: Item): MutableList<Item> {
        val result = mutableListOf<Item>()
        for(line in list) {
            val splitLine = line.split(' ')
            if(splitLine[0] == "dir") {
                result.add(Item(splitLine[1], mutableListOf(), 0, parent))
            } else {
                result.add(Item(splitLine[1], null, splitLine[0].toInt(), parent))
            }
        }
        return result
    }

    fun buildDirectories(commands: List<Command>): Set<Item> {
        // find starting point - overkill for given input
        val idRootLS = commands.withIndex().first { it.value is CD_ROOT  && commands[it.index+1] is LS}.index
        val root = Item("/", mutableListOf(), 0, null)
        var currentDir = root
        val allItems: MutableSet<Item> = mutableSetOf(root)
        for (cmd in commands.drop(idRootLS+1)) {
            when (cmd) {
                is CD_OUT -> currentDir = currentDir.parent!!
                is CD_ROOT -> currentDir = root
                is LS ->
                    if (currentDir.children.isNullOrEmpty()) {
                        currentDir.children = parseLSResult(cmd.result, currentDir)
                        allItems.addAll(currentDir.children!!)
                    }

                is CD -> {
                    val next = currentDir.children?.find { it.id == cmd.argument }
                    if (next != null) currentDir = next
                    else {
                        val next = Item(cmd.argument, mutableListOf(), 0, currentDir)
                        currentDir.addChild(next)
                        currentDir = next
                        allItems.add(next)
                    }
                }
            }
        }
        return allItems
    }

    fun part1(items: Set<Item>): Int {
        val sum = items.filter {it.isDir}.sumOf { item ->
            val size = item.getSize()
            if(size <= 100000) size else 0
        }
        return sum
    }

    fun part2(items: Set<Item>): Int  {
        val root = items.find { it.id == "/" }
        val totalSpace = 70000000
        val unusedSpace = totalSpace - root!!.getSize()
        val neededSpace = 30000000 - unusedSpace
        val dir = items.filter { it.isDir && it.getSize() >= neededSpace }.minBy { it.getSize()}
        return dir.getSize()
    }

    val commands = parseCommands("Day07_test")
    val items = buildDirectories(commands)
    println(part1(items))
    println(part2(items))
}
