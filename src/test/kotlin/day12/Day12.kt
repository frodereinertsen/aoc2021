package day12

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day12 {

    @Test
    fun puzzle1() {
        Graph(input.toNodes()).countPathsWithSingleSmall() shouldBe 10
        Graph(biggerExample.toNodes()).countPathsWithSingleSmall() shouldBe 19
        Graph(evenBiggerExample.toNodes()).countPathsWithSingleSmall() shouldBe 226
        Graph(puzzleInput.toNodes()).countPathsWithSingleSmall() shouldBe 5228
    }

    @Test
    fun puzzle2() {
        Graph(input.toNodes()).countPathsWithAtMostOneDoubleSmall() shouldBe 36
        Graph(biggerExample.toNodes()).countPathsWithAtMostOneDoubleSmall() shouldBe 103
        Graph(evenBiggerExample.toNodes()).countPathsWithAtMostOneDoubleSmall() shouldBe 3509
        Graph(puzzleInput.toNodes()).countPathsWithAtMostOneDoubleSmall() shouldBe 131228
    }
}

class Graph(private val startNode: Node) {

    fun countPathsWithSingleSmall(): Int = countPaths { _, connectedNode, predecessors ->
        !predecessors.contains(connectedNode)
    }

    fun countPathsWithAtMostOneDoubleSmall(): Int {
        fun List<Node>.numberOfSmallNodesThatOccurTwice(): Int =
            filter { it.isSmall() }.groupBy { it.name }.count { it.value.size == 2 }

        return countPaths { currentNode, connectedNode, predecessors ->
            (!predecessors.contains(connectedNode) || (predecessors + currentNode).numberOfSmallNodesThatOccurTwice() == 0)
        }
    }

    private fun countPaths(smallNodeShouldBeAdded: (currentNode: Node, connectedNode: Node, predecessors: List<Node>) -> Boolean): Int {
        val paths = mutableListOf<List<Node>>()

        fun countPaths(node: Node, predecessors: List<Node>) {
            if (node.isEnd()) {
                paths.add(predecessors + node)
            } else {
                node.connected.forEach { connectedNode ->
                    when {
                        connectedNode.isBig() || connectedNode.isEnd() -> {
                            countPaths(connectedNode, predecessors + node)
                        }
                        connectedNode.isSmall() && smallNodeShouldBeAdded(node, connectedNode, predecessors) -> {
                            countPaths(connectedNode, predecessors + node)
                        }
                    }
                }
            }
        }

        countPaths(startNode, emptyList())

        return paths.size
    }
}

data class Node(val name: String, val connected: MutableSet<Node> = mutableSetOf()) {
    fun isStart() = name == "start"
    fun isEnd() = name == "end"
    fun isSmall() = !isStart() && !isEnd() && name.all { it.isLowerCase() }
    fun isBig() = name.all { it.isUpperCase() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Node(name='$name', connected=${connected.size})"
    }
}

private fun String.toNodes(): Node {
    val nodes: MutableMap<String, Node> = mutableMapOf()

    lines().forEach { line ->
        line.split("-").forEach { name ->
            nodes.computeIfAbsent(name) { name -> Node(name) }
        }
    }
    lines().forEach { line ->
        line.split("-").let {
            val from = nodes[it[0]]!!
            val to = nodes[it[1]]!!
            from.connected.add(to)
            to.connected.add(from)
        }
    }

    return nodes["start"]!!
}

val input = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent()

val biggerExample = """
        dc-end
        HN-start
        start-kj
        dc-start
        dc-HN
        LN-dc
        HN-end
        kj-sa
        kj-HN
        kj-dc
    """.trimIndent()

val evenBiggerExample = """
        fs-end
        he-DX
        fs-he
        start-DX
        pj-DX
        end-zg
        zg-sl
        zg-pj
        pj-he
        RW-he
        fs-DX
        pj-RW
        zg-RW
        start-pj
        he-WI
        zg-he
        pj-fs
        start-RW
    """.trimIndent()

val puzzleInput = """
        EO-jc
        end-tm
        jy-FI
        ek-EO
        mg-ek
        jc-jy
        FI-start
        jy-mg
        mg-FI
        jc-tm
        end-EO
        ds-EO
        jy-start
        tm-EO
        mg-jc
        ek-jc
        tm-ek
        FI-jc
        jy-EO
        ek-jy
        ek-LT
        start-mg
    """.trimIndent()
