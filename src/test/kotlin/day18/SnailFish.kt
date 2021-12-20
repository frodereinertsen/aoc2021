package day18

import kotlin.math.ceil
import kotlin.math.floor

sealed class SnailFish(var parent: Pair? = null) {

    fun reduce(): SnailFish {
        do {
            val exploded = firstExploding()?.apply { explode() } != null
            val split = !exploded && firstSplitting()?.apply { split() } != null
        } while (exploded || split)

        return this
    }

    abstract fun magnitude(): Int

    protected abstract fun firstSplitting(): Regular?

    protected abstract fun firstExploding(depth: Int = 1): Pair?

    protected abstract fun firstRegularOnSide(side: Pair.() -> SnailFish): Regular

    operator fun plus(other: SnailFish): SnailFish = Pair(this, other)

    data class Pair(var left: SnailFish, var right: SnailFish) : SnailFish() {
        init {
            left.parent = this
            right.parent = this
        }

        fun replace(old: SnailFish, new: SnailFish) {
            if (left === old) {
                left = new
            } else {
                right = new
            }
            new.parent = this
        }

        fun explode() {
            firstNonSideParent(Pair::left)?.left?.firstRegularOnSide(Pair::right)?.also {
                it.value += (left as Regular).value
            }
            firstNonSideParent(Pair::right)?.right?.firstRegularOnSide(Pair::left)?.also {
                it.value += (right as Regular).value
            }

            this.parent?.replace(this, Regular(0))
        }

        private fun firstNonSideParent(side: Pair.() -> SnailFish): Pair? {
            var current = this

            while (current.parent != null) {
                if (current.parent!!.side() !== current) {
                    return current.parent
                } else {
                    current = current.parent!!
                }
            }

            return null
        }

        override fun magnitude(): Int = left.magnitude() * 3 + right.magnitude() * 2

        override fun firstSplitting(): Regular? = left.firstSplitting() ?: right.firstSplitting()

        override fun firstExploding(depth: Int): Pair? =
            if (depth == 5) {
                this
            } else {
                left.firstExploding(depth + 1) ?: right.firstExploding(depth + 1)
            }

        override fun firstRegularOnSide(side: Pair.() -> SnailFish): Regular = side().firstRegularOnSide(side)

        override fun toString(): String {
            return "[$left,$right]"
        }
    }

    data class Regular(var value: Int) : SnailFish() {

        fun split() {
            val l = floor(this.value / 2.0).toInt()
            val r = ceil(this.value / 2.0).toInt()
            val newNum = Pair(Regular(l), Regular(r))

            this.parent?.replace(this, newNum)
        }

        override fun magnitude(): Int = value

        override fun firstSplitting(): Regular? = if (this.value >= 10) this else null

        override fun firstExploding(depth: Int): Pair? = null

        override fun firstRegularOnSide(side: Pair.() -> SnailFish): Regular = this

        override fun toString(): String {
            return "$value"
        }
    }
}


fun <E> List<E>.permutations(length: Int): Sequence<List<E>> {
    return permutations(this, length)
}

//thank you, somebody out there
fun <E> permutations(list: List<E>, length: Int? = null): Sequence<List<E>> = sequence {
    val n = list.size
    val r = length ?: list.size

    val indices = list.indices.toMutableList()
    val cycles = (n downTo (n - r)).toMutableList()
    yield(indices.take(r).map { list[it] })

    while (true) {
        var broke = false
        for (i in (r - 1) downTo 0) {
            cycles[i]--
            if (cycles[i] == 0) {
                val end = indices[i]
                for (j in i until indices.size - 1) {
                    indices[j] = indices[j + 1]
                }
                indices[indices.size - 1] = end
                cycles[i] = n - i
            } else {
                val j = cycles[i]
                val tmp = indices[i]
                indices[i] = indices[-j + indices.size]
                indices[-j + indices.size] = tmp
                yield(indices.take(r).map { list[it] })
                broke = true
                break
            }
        }
        if (!broke) {
            break
        }
    }
}
