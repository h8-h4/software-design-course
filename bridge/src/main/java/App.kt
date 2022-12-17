import drawing.AsciiDrawingApi
import drawing.DrawingApi
import drawing.JavaAwtDrawingApi
import graph.AdjacencyMatrixGraph
import graph.EdgeListGraph
import graph.Graph
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.system.exitProcess

const val USAGE = """
            Mandatory args:
                -ascii/-awt -- drawing api
                -matrix/-list -- graph input type
            Input format:
                if -matrix arg is specified:
                    Adjacenty matrix row that contains zeroes and ones:
                    0 1 0 0
                    1 0 1 0
                    1 0 0 1
                    0 0 1 0
                if -list arg is specified:
                    Edge list row with node index as first number:
                    0 1 2
                    1 0 2
                    2 0 1
            """
const val ASCII = "-ascii"
const val AWT = "-awt"
const val MATRIX = "-matrix"
const val LIST = "-list"

private fun printUsage(): Nothing {
    println(USAGE)
    exitProcess(0)
}

fun main(args: Array<String>) {
    val drawingApi: DrawingApi = when {
        AWT in args -> JavaAwtDrawingApi()
        ASCII in args -> AsciiDrawingApi()
        else -> printUsage()
    }

    val graph: Graph = when {
        LIST in args -> EdgeListGraph(drawingApi)
        MATRIX in args -> AdjacencyMatrixGraph(drawingApi)
        else -> printUsage()
    }

    BufferedReader(InputStreamReader(System.`in`)).use { reader ->
        reader.lines()
            .map { it.toIntList() }
            .forEach {
                graph.addNode(it)
            }
    }

    graph.drawGraph()
}

private fun String.toIntList() = split(" ").map { it.toInt() }