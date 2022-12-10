class Directory(val dirName: String) {
    private var files = arrayOf<File>()
    private var directories = arrayOf<Directory>()
    var dirSize: Int = 0

    fun calculateSize() {
        println("calculate dir size $dirName")
        directories.forEach { it.calculateSize() }
        dirSize = files.sumOf { it.fileSize } + directories.sumOf { it.dirSize }
        println("calculated dir size $dirName is $dirSize")
    }

    fun getAllDirs(): Array<Directory> {
        var allDirs = arrayOf<Directory>(*directories)
        directories.forEach { allDirs = allDirs.plus(it.getAllDirs()) }
        return allDirs
    }

    fun addDir(directory: Directory) {
        println("add dir: ${directory.dirName}, current dir: ${dirName}")
        directories += directory
    }

    fun findDir(dirName: String): Directory {
        return directories.find { it.dirName == dirName }!!
    }

    fun addFile(file: File) {
        println("add file: ${file.fileName}, size: ${file.fileSize}, current dir: $dirName")
        files += file
    }
}

class File(val fileName: String, val fileSize: Int) {

}

fun main() {
    fun parseDirs(input: List<String>): Directory {
        val rootDir = Directory("/")
        var dirStacks = mutableListOf<Directory>(rootDir)

        for (line in input) {
            if (line[0].toString() == "$") {
                val splits = line.split(" ")
                val command = splits[1]
                if (command == "cd") {
                    val goToDirName = splits[2]
                    if (goToDirName == "/") {
                        dirStacks = mutableListOf<Directory>(rootDir)
                    } else if (goToDirName == "..") {
                        dirStacks.removeLast()
                    } else {
                        val currentDir = dirStacks.last()
                        dirStacks += currentDir.findDir(goToDirName)
                    }
                } else if (command == "ls") {
                    continue
                }
            } else {
                // content of ls command of currentDir
                val currentDir = dirStacks.last()
                if (line.substring(0, 3) == "dir") {
                    val dirName = line.split(" ")[1]
                    currentDir.addDir(Directory(dirName))
                } else {
                    val splits = line.split(" ")
                    val fileSize = splits[0]
                    val fileName = splits[1]
                    currentDir.addFile(File(fileName, fileSize.toInt()))
                }
            }
        }

        rootDir.calculateSize()
        return rootDir
    }

    fun part1(input: List<String>): Int {
        val rootDir = parseDirs(input)

        val allDirs = rootDir.getAllDirs()
        val dirAtMostSize = 100000
        return allDirs.filter { it.dirSize <= dirAtMostSize }.sumOf { it.dirSize }
    }

    fun part2(input: List<String>): Int {
        val rootDir = parseDirs(input)

        val totalDiskSpace = 70000000
        val requiredUnusedSpace = 30000000
        val unusedSpace = totalDiskSpace - rootDir.dirSize
        val deletedSpace = requiredUnusedSpace - unusedSpace

        val allDirSizes = rootDir.getAllDirs().map { it.dirSize }.sorted()
        for (dirSize in allDirSizes) {
            if (dirSize >= deletedSpace) {
                return dirSize
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day07_sample")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readTestInput("Day07")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
