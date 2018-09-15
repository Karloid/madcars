import java.lang.Math.abs

class MyStrategy : Strategy {

    private var debugMessage: String = ""

    private lateinit var world: World
    private lateinit var move: Move


    val ON_AIR_PAUSE = 0

    var tick = 0

    var matchCounter = 0


    private lateinit var match: MatchConfig

    private var maxMinButtonY: Float = -1f
    private var desiredAngleForBus: Float = 0f

    private var isBus: Boolean = false

    override fun onMatchStarted(matchConfig: MatchConfig) {
        tick = 0
        matchCounter++
        this.match = matchConfig

        isBus = match.carId == 2

        debugMessage += "\n" + match.buttonPoly

        myLastAngle = 0f //TODO state

    }

    override fun onNextTick(world: World, move: Move) {
        pretick(move, world)

        simpleStrategy()
    }


    private var myLastAngle: Float = 0f

    private var myAngleSpeed: Float = 0f

    private fun simpleStrategy() {

        //move.d(" map_id ${match.mapId}  car_id ${match.carId} tick ${tick} my side ${getMySide()}")

        val myCarAngle = world.myCar.angle
        myAngleSpeed = myLastAngle - myCarAngle
        myLastAngle = myCarAngle

        move.d("myXY ${world.myCar.x.f()} - ${world.myCar.y.f()} a: ${myCarAngle.f()} as PI: ${world.myCar.angle.asPi().f()} " +
                "angleSpeed ${myAngleSpeed}")



        val angleKoeff = if (isBus) 1f else 0.7f

        var desiredAngle = (HALF_PI * angleKoeff) * getMySide()

        if (isBus) {
            val minButtonY = getMinButtonY(world.myCar)
            if (minButtonY > maxMinButtonY) {
                maxMinButtonY = minButtonY
                desiredAngleForBus = myCarAngle
                move.d("found best angle ${myCarAngle.f()} which y ${maxMinButtonY}")
            }

            if (tick > 80) {
               // move.d("set desiredAngle from ${desiredAngle.f()} to ${desiredAngleForBus}")
                desiredAngle = desiredAngleForBus

                var cmd = 1

                val delta = myCarAngle - desiredAngle
                if (delta > 0) {
                    cmd *= -1
                }

                if (Math.abs(myAngleSpeed) > 0.00436248
                        && ((cmd < 0 && myAngleSpeed > 0) || (cmd > 0 && myAngleSpeed < 0))) {
                    cmd *= -1
                }

                move.set(cmd)

                return
            }
        }



        var cmd = 1

        val delta = myCarAngle - desiredAngle
        if (delta > 0) {
            cmd *= -1
        }

        val isCloseToPerfectAngle = abs(delta) < HALF_PI / 2

        if (tick < ON_AIR_PAUSE && isCloseToPerfectAngle && tick % 5 != 0 && !isBus) {
            cmd *= -1
        }



        if (tick > 50 && tick % 5 != 0 && !isBus) {
            val myX = world.myCar.x
            val enemyX = world.enemyCar.x

            var leftCmd = -1
            var rightCmd = 1
            if (abs(myCarAngle) > 1) {  //whut
                //move.d("strange switch on")
                rightCmd = leftCmd
                leftCmd = 1
            }
            cmd = if (myX > enemyX) leftCmd else rightCmd
        }

        move.set(cmd)
    }

    private fun getMinButtonY(myCar: World.Car): Float {
        return match.buttonPoly.map {
            var point = it
            if (getMySide() == -1){
                point = Point2D(-it.x, it.y)
            }
            val rotated = point.rotate(myCar.angle.toDouble())
            rotated.y
        }.min()!!.toFloat()
    }

    private fun getRotatedButtonPoly(myCar: World.Car): List<Point2D> {

        return match.buttonPoly.map { it.rotate(myCar.angle.toDouble()) }
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
