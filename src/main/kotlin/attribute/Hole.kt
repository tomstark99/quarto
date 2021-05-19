package attribute

enum class Hole(var id: Int, var hole: String, var hasHole: Boolean) {
    YES(0, "yes", true),
    NO(1, "no", false),
    EMPTY(2, "empty", false)
}