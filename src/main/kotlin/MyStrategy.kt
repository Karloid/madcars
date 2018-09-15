class MyStrategy : Strategy {

    private var debugMessage: String = ""

    val ON_AIR_PAUSE = 20


    private var mySide: Int = 1

    var thisMatchTick = 0
    var matchCounter = 0

    override fun onMatchStarted(matchConfig: MatchConfig) {
        thisMatchTick = 0
        matchCounter++

    }


    override fun onNextTick(tickState: TickState, move: Move) {
        thisMatchTick++

        if (!debugMessage.isNullOrEmpty()) {
            move.addDebug(debugMessage)
            debugMessage = ""
        }

        if (thisMatchTick == 1) {
            mySide = tickState.myCar.side
        }


        move.set(1)
    }


    override fun onParsingError(message: String) {
        debugMessage = message
    }

}
