import org.json.JSONObject

var isLocal: Boolean = false
object MainKt {
    private var robot: Strategy = MyStrategy()


    @JvmStatic
    fun main(args: Array<String>) {
        isLocal = args.size > 0

        var gameMessage: JSONObject?
        gameMessage = JsonIO.readFromStdIn()
        while (gameMessage != null) {
            val messageType: MessageType
            try {
                messageType = gameMessage.getEnum(MessageType::class.java, "type")
                when (messageType) {
                    MessageType.tick -> {
                        val tickState = World(gameMessage.getJSONObject("params"))
                        val move = Move()
                        robot.onNextTick(tickState, move)
                        move.send()
                    }

                    MessageType.new_match -> {
                        val matchConfig = MatchConfig(gameMessage.getJSONObject("params"))
                        robot.onMatchStarted(matchConfig)
                    }
                }
            } catch (e: Exception) {
                if (isLocal) {
                    e.printStackTrace()
                }
                robot.onParsingError(e.message ?: "unknown")
            }
            gameMessage = JsonIO.readFromStdIn()
        }
    }

}
