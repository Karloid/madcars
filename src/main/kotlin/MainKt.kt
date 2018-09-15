import org.json.JSONObject

object MainKt {
    private var robot: Strategy = MyStrategy()

    @JvmStatic
    fun main(args: Array<String>) {

        var gameMessage: JSONObject?
        gameMessage = JsonIO.readFromStdIn()
        while (gameMessage != null) {
            val messageType: MessageType
            try {
                messageType = gameMessage.getEnum(MessageType::class.java, "type")
                when (messageType) {
                    MessageType.tick -> {
                        val tickState = TickState(gameMessage.getJSONObject("params"))
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
                robot.onParsingError(e.message ?: "unknown")
            }
            gameMessage = JsonIO.readFromStdIn()
        }
    }

}
