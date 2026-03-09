/*
3. `Museum.kt` (THE GRAPH, BFS, AND THREAD-SAFETY MANAGER)
    This is the core of the practical. Note the BFS logic and the Deadlock
    avoidance in `transition()`.
* */
// package museumvisit

// import java.util.concurrent.TimeUnit
// import kotlin.concurrent.withLock

class Museum(
    val name: String,
    val entrance: MuseumRoom,
) {
    var admitted: Int = 0
        private set

    val outside = Outside()
    val rooms = mutableSetOf(entrance)

    // Adjacency List representing the Graph (Room -> Turnstiles)
    private val turnstiles =
        LinkedHashMap<MuseumRoom, MutableList<MuseumSite>>().apply {
            put(entrance, mutableListOf())
        }

    fun addRoom(room: MuseumRoom) {
        require(rooms.none { it.name == room}) {}
    }
}




/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */