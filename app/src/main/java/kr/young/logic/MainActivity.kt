package kr.young.logic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run()
    }

    private lateinit var rows: Array<IntArray>
    private lateinit var cols: Array<IntArray>

    private fun run() {
//        printNums()
        rows = r3
        cols = c3
        map = Array(rows.size) { IntArray(cols.size) { 0 } }

        val startTime = System.currentTimeMillis()
        recursiveSolve(true, 0)
        printMap()
        val endTime = System.currentTimeMillis()
        println("time is ${endTime - startTime}")
    }

    private fun recursiveSolve(forward: Boolean, idx: Int): Boolean {
        if (idx >= rows.size * cols.size) {
            return true
        }
        //i: rows, j: cols
        val i = idx / cols.size
        val j = idx % cols.size
        return when {
            map!![i][j] == 0 -> {
                val result = fillNum(i, j)
                if (result) {
                    recursiveSolve(true, idx+1)
                } else {
                    recursiveSolve(false, idx-1)
                }
            }
            map!![i][j] == 2 -> {
                map!![i][j] = -1
                recursiveSolve(true, idx+1)
            }
            map!![i][j] == 1 -> {
                map!![i][j] = 0
                recursiveSolve(false, idx-1)
            }
            map!![i][j] == -1 -> {
                map!![i][j] = 0
                recursiveSolve(false, idx-1)
            }
            else -> {
                if (forward) {
                    recursiveSolve(forward, idx + 1)
                } else {
                    recursiveSolve(forward, idx - 1)
                }
            }
        }
    }

    private fun fillNum(i: Int, j: Int): Boolean {
        val cr = checkRow(i, j)
        val cc = checkCol(i, j)
        if (cr == 0 && cc == 0) {
            map!![i][j] = 2
        } else if (cr == 0) {
            map!![i][j] = cc
        } else if (cc == 0) {
            map!![i][j] = cr
        } else if (cc == cr) {
            map!![i][j] = cc
        } else {
//            println("wrong(${i+1}, ${j+1})")
//            printMap()
            return false
        }
        return true
    }

    private fun checkRow(i: Int, j: Int): Int {
//        print("$i row - ")
//        for (row in rows[i]) {
//            print("$row ")
//        }
//        println()
        return if (j == 0) {
            checkStartFill(rows[i], 0, cols.size, 0)
        } else if (map!![i][j-1] < 0) {
            var hintIdx = 0
            var ingNum = 0
            for (idx in 0 until j) {
                if (map!![i][idx] == 0) {
                    break
                } else if (map!![i][idx] < 0) {
                    ingNum = 0
                    if (idx != 0 && map!![i][idx-1] > 0) {
                        hintIdx++
                    }
                } else {
                    ingNum++
                }
            }
            if (rows[i].size <= hintIdx) {
                -1
            } else {
                checkStartFill(rows[i], hintIdx, cols.size, j)
            }
        } else {
            checkContinueFill(true, i, j)
        }
    }

    private fun checkCol(i: Int, j: Int): Int {
//        print("$j col - ")
//        for (col in cols[j]) {
//            print("$col ")
//        }
//        println()
        return if (i == 0) {
            checkStartFill(cols[j], 0, rows.size, 0)
        } else if (map!![i-1][j] < 0) {
            var hintIdx = 0
            var ingNum = 0
            for (idx in 0 until i) {
                if (map!![idx][j] == 0) {
                    break
                } else if (map!![idx][j] < 0) {
                    ingNum = 0
                    if (idx != 0 && map!![idx-1][j] > 0) {
                        hintIdx++
                    }
                } else {
                    ingNum++
                }
            }
            if (cols[j].size <= hintIdx) {
                -1
            } else {
                checkStartFill(cols[j], hintIdx, rows.size, i)
            }
        } else {
            checkContinueFill(false, i, j)
        }
    }

    //start fill
    private fun checkStartFill(hintArray: IntArray, hintIdx: Int, size: Int, startIdx: Int): Int {
        if (hintArray[0] == 0) {
            return -2
        } else if (hintArray[0] == size) {
            return 3
        }
        var sum = hintArray.size - hintIdx - 1
        for (idx in hintIdx until hintArray.size) {
            sum += hintArray[idx]
        }
        return if (sum == size - startIdx) {
            1
        } else {
            0
        }
    }

    private fun a(hintArray: IntArray) {

    }

    //continue fill
    private fun checkContinueFill(isRow: Boolean, i: Int, j: Int): Int {
        var hintIdx = 0
        var ingNum = 0
        return if (isRow) {
            for (idx in 0 until j) {
                if (map!![i][idx] == 0) {
                    break
                } else if (map!![i][idx] < 0) {
                    ingNum = 0
                    if (idx != 0 && map!![i][idx-1] > 0) {
                        hintIdx++
                    }
                } else {
                    ingNum++
                }
            }
            if (rows[i][hintIdx] != ingNum) {
                1
            } else {
                -1
            }
        } else {
            for (idx in 0 until i) {
                if (map!![idx][j] == 0) {
                    break
                } else if (map!![idx][j] < 0) {
                    ingNum = 0
                    if (idx != 0 && map!![idx-1][j] > 0) {
                        hintIdx++
                    }
                } else {
                    ingNum++
                }
            }
            if (cols[j][hintIdx] != ingNum) {
                1
            } else {
                -1
            }
        }
    }

    private fun printMap() {
        for ((i, r) in map!!.withIndex()) {
            for ((j, num) in r.withIndex()) {
                when {
                    num == 0 -> {
                        println("------------")
                        return
                    }
                    num > 0 -> {
                        print(String.format(Locale.getDefault(), " %d ", num))
                    }
                    num == -2 -> {
                        print(" : ")
                    }
                    else -> {
                        print(" . ")
                    }
                }
            }
            println()
        }
        println("------------")
    }

    private fun printNums() {
        for ((i, r) in r1.withIndex()) {
            for ((j, num) in r.withIndex()) {
                print(num)
            }
            println()
        }
        for ((i, c) in c1.withIndex()) {
            for ((j, num) in c.withIndex()) {
                print(num)
            }
            println()
        }
    }

    private var map: Array<IntArray>? = null

    private val c1 = arrayOf(
        intArrayOf(4, 1),
        intArrayOf(5, 2),
        intArrayOf(4, 2, 2),
        intArrayOf(5, 1),
        intArrayOf(6, 2),
        intArrayOf(4, 1, 3),
        intArrayOf(1, 7),
        intArrayOf(1, 1, 3),
        intArrayOf(1, 1, 5),
        intArrayOf(1, 2, 3)
    )

    private val r1 = arrayOf(
        intArrayOf(7, 2),
        intArrayOf(6, 1),
        intArrayOf(6, 1),
        intArrayOf(8, 1),
        intArrayOf(1, 2, 1, 1),
        intArrayOf(1, 3, 1),
        intArrayOf(1, 1, 1, 1),
        intArrayOf(7),
        intArrayOf(2, 6),
        intArrayOf(2, 5)
    )

    private val c2 = arrayOf(
        intArrayOf(3, 2, 2),
        intArrayOf(1, 2),
        intArrayOf(1, 3),
        intArrayOf(2, 3),
        intArrayOf(2),
        intArrayOf(0),
        intArrayOf(3, 5),
        intArrayOf(1, 1, 2, 2),
        intArrayOf(1, 2),
        intArrayOf(2, 3)
    )

    private val r2 = arrayOf(
        intArrayOf(2, 2),
        intArrayOf(1, 2, 1),
        intArrayOf(1, 1),
        intArrayOf(1, 1, 1),
        intArrayOf(1, 1, 1, 2),
        intArrayOf(1, 2, 2, 1),
        intArrayOf(2, 3),
        intArrayOf(2, 1, 2),
        intArrayOf(1, 1, 2, 1),
        intArrayOf(1, 1, 1, 1)
    )

    private val c3 = arrayOf(
        intArrayOf(1, 1), intArrayOf(1, 6), intArrayOf(2, 5),
        intArrayOf(1, 4), intArrayOf(8), intArrayOf(7, 1),
        intArrayOf(1, 3, 1), intArrayOf(2, 1, 6), intArrayOf(2, 5),
        intArrayOf(1, 4), intArrayOf(8, 3), intArrayOf(7, 1, 1, 1),
        intArrayOf(1, 3, 3, 1), intArrayOf(2, 1, 1, 1), intArrayOf(1, 3)
    )

    private val r3 = arrayOf(
        intArrayOf(2, 2), intArrayOf(2, 1, 2, 1), intArrayOf(5, 5),
        intArrayOf(1, 3, 1, 3), intArrayOf(2, 3, 1, 3), intArrayOf(1, 3, 1, 3),
        intArrayOf(4, 4), intArrayOf(4, 4), intArrayOf(15),
        intArrayOf(2, 2, 1), intArrayOf(2, 2, 1), intArrayOf(1, 1, 1, 1),
        intArrayOf(1, 1, 1, 1), intArrayOf(1, 1), intArrayOf(5)
    )
}