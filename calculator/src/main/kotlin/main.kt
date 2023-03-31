import java.util.Stack

sealed interface Expr {
    data class Addition(val left: Expr, val right: Expr): Expr
    data class Multiplication(val left: Expr, val right: Expr): Expr
    data class Negation(val expr: Expr): Expr
    data class Literal(val int: Int): Expr
}

fun eval(expr: Expr): Int {
    return when (expr) {
        is Expr.Addition -> eval(expr.left) + eval(expr.right)
        is Expr.Multiplication -> eval(expr.left) * eval(expr.right)
        is Expr.Negation -> -(eval(expr.expr))
        is Expr.Literal -> expr.int
    }
}

fun main() {
    val expr: Expr = Expr.Addition(
        Expr.Literal(1),
        Expr.Multiplication(
            Expr.Literal(2),
            Expr.Negation(
                Expr.Literal(4))))
    // println("$addition = ${eval(addition)}")

    val program: List<Instruction> = compile(expr)
    println("compile(expr) = $program")

    println("Neu: ${VM(faultyProgram).eval()}")
}

fun compile(expr: Expr): List<Instruction> {
    return when (expr) {
        is Expr.Literal -> listOf(Instruction.Const(expr.int))
        is Expr.Addition -> {
            val left = compile(expr.left)
            val right = compile(expr.right)
            left + right + listOf(Instruction.Add)
        }
        is Expr.Multiplication -> {
            val left = compile(expr.left)
            val right = compile(expr.right)
            left + right + listOf(Instruction.Mul)
        }
        is Expr.Negation -> {
            val inner = compile(expr.expr)
            inner + listOf(Instruction.Neg)
        }
    }
}

sealed interface Instruction {
    data class Const(val int: Int): Instruction
    object Add: Instruction
    object Mul: Instruction
    object Neg: Instruction
}

val faultyProgram: List<Instruction> = listOf(
    Instruction.Const(1),
    Instruction.Const(2),
    Instruction.Const(4),
    Instruction.Neg,
    Instruction.Mul,
    Instruction.Add,
    Instruction.Add
)
data class VM(val instructions: List<Instruction>, val stack: Stack<Int> = Stack()){
    fun eval(): Int {
        for (instruction in instructions) {
            when (instruction) {
                is Instruction.Const -> stack.push(instruction.int)
                Instruction.Add -> {
                    val left = stack.pop()
                    val right = stack.pop()
                    stack.push(left + right)
                }
                Instruction.Mul -> {
                    val left = stack.pop()
                    val right = stack.pop()
                    stack.push(left * right)
                }
                Instruction.Neg ->  {
                    val int = stack.pop()
                    stack.push(-int)
                }
            }
        }

        return stack.pop()
    }
}