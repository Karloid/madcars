class MyStrategy : Strategy {
    // todo заменить этот анонимный класс-заглушку реальным классом стратегии
    val ON_AIR_PAUSE = 50
    val commands = arrayOf("left", "stop", "right")

    var thisMatchTick = 0
    var matchCounter = 0
    var debugMessage = ""

    override fun onMatchStarted(matchConfig: MatchConfig) {
        thisMatchTick = 0
        matchCounter++

        debugMessage = String.format(".... Match #%d: lives=%d, ", matchCounter, matchConfig.myLives)
    }

    override fun onNextTick(tickState: TickState) {
        thisMatchTick++

        if (thisMatchTick == 1)
            debugMessage += String.format("my side=%s.... ", commands[1 - tickState.myCar.mirror])

        val cmd = if (thisMatchTick <= ON_AIR_PAUSE) "stop" else commands[0]
        debugMessage += String.format("%d.%d: %s", matchCounter, thisMatchTick, cmd)

        Answer(cmd, debugMessage).send()
        debugMessage = ""
    }

    override fun onParsingError(message: String) {
        debugMessage = message
    }

}
