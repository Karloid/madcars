import org.json.JSONObject

val commands = arrayOf("left", "stop", "right")

class Move internal constructor() {

    var command: String = "stop"

    var debug: String = ""


    internal fun send() {
        val json = JSONObject(this)
        JsonIO.writeToStdOut(json)
    }

    fun set(cmd: Int) {
        command = commands[cmd - 1]
    }

    fun addDebug(debugMessage: String) {
        debug += "\n" + debugMessage
    }
}