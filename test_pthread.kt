import platform.posix.*
fun main() {
    val t1 = pthread_self()
    val t2 = pthread_self()
    println(t1 == t2)
    println(pthread_equal(t1, t2))
}
