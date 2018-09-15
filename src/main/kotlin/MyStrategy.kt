import java.lang.Math.abs

class MyStrategy : Strategy {

    private var debugMessage: String = ""

    private lateinit var world: World
    private lateinit var move: Move


    val ON_AIR_PAUSE = 20

    var tick = 0

    var matchCounter = 0


    private lateinit var match: MatchConfig


    private var isBus: Boolean = false

    override fun onMatchStarted(matchConfig: MatchConfig) {
        tick = 0
        matchCounter++
        this.match = matchConfig

        isBus = match.carId == 2

    }

    override fun onNextTick(world: World, move: Move) {
        pretick(move, world)

        simpleStrategy()
    }

    private fun simpleStrategy() {

        move.d(" map_id ${match.mapId}  car_id ${match.carId} tick ${tick} my side ${getMySide()}")

        var myCarAngle = world.myCar.angle

        var angleKoeff = if (isBus) 1f else 0.7f
        var desiredAngle = (HALF_PI * angleKoeff) * getMySide()

        var cmd = 1

        var delta = myCarAngle - desiredAngle
        if (delta > 0) {
            cmd *= -1
        }

        var isCloseToPerfectAngle = abs(delta) < HALF_PI / 2

        if (tick < 10 && isCloseToPerfectAngle && tick % 5 != 0 && !isBus) {
            cmd *= -1
        }

        if (tick > 50 && tick % 5 != 0 && !isBus) {
            var myX = world.myCar.x
            var enemyX = world.enemyCar.x

            var leftCmd = -1
            var rightCmd = 1
            if (abs(myCarAngle) > 1) {  //whut
                move.d("strange switch on")
                rightCmd = leftCmd
                leftCmd = 1
            }
            cmd = if (myX > enemyX) leftCmd else rightCmd
        }

        move.set(cmd)
    }


    fun pretick(move: Move, world: World) {
        this.move = move;
        this.world = world;
        tick++

        if (!debugMessage.isEmpty()) {
            move.d(debugMessage)
            debugMessage = ""
        }
    }

    fun getMySide() = world.myCar.side


    override fun onParsingError(message: String) {
        debugMessage = message
    }

}