import org.json.JSONObject

var isLocal: Boolean = false
object MainKt {
    private var robot: Strategy = MyStrategy()


    @JvmStatic
    fun main(args: Array<String>) {
        isLocal = args.size > 0

        var gameMessage: JSONObject?

        while (true) {
            try {
                gameMessage = JsonIO.readFromStdIn() ?: throw NullPointerException("game message is null!")

                val messageType = gameMessage.getEnum(MessageType::class.java, "type") ?: throw NullPointerException("messageType is null!")

                when (messageType) {
                    MessageType.tick -> {
                        val tickState = World(gameMessage.getJSONObject("params"))
                        val move = Move()
                        robot.onNextTick(tickState, move)
                        move.send()
                    }

                    MessageType.new_match -> {
                        if (isLocal) {
                            System.err.println(gameMessage.toString(2))
                        }
                        val matchConfig = MatchConfig(gameMessage.getJSONObject("params"))
                        robot.onMatchStarted(matchConfig)
                    }
                }
            } catch (e: Exception) {
                if (isLocal) {
                    e.printStackTrace()
                }
                robot.onParsingError(e.message ?: "unknown")
                val move = Move()
                move.set(0)
                move.send()
            }
        }
    }

}
