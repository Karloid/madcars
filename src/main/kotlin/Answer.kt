import org.json.JSONObject

class Answer internal constructor(cmd: String, dbg: String) {
    var command: String

    var debug: String

    init {
        command = cmd
        debug = dbg
    }

    internal fun send() {
        val json = JSONObject(this)
        JsonIO.writeToStdOut(json)
    }
}