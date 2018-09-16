import java.lang.Math.abs

class MyStrategy : Strategy {

    private var debugMessage: String = ""

    private lateinit var w: World
    private lateinit var move: Move


    val ON_AIR_PAUSE = 0

    var tick = 0

    var matchCounter = 0


    private lateinit var match: MatchConfig

    private var maxMinButtonY: Float = -1f
    private var desiredAngleForBus: Float = 0f

    private var isBus: Boolean = false

    private var s = State()

    override fun onMatchStarted(matchConfig: MatchConfig) {
        tick = 0
        matchCounter++
        this.match = matchConfig

        isBus = match.carId == 2

        debugMessage += "\n" + match.buttonPoly

        s = State()
    }

    override fun onNextTick(world: World, move: Move) {
        pretick(move, world)

        simpleStrategy()
    }


    private fun simpleStrategy() {

        //move.d(" map_id ${match.mapId}  car_id ${match.carId} tick ${tick} my side ${getMySide()}")

        s.myCarAngle = w.myCar.angle

        move.d("myXY ${w.myCar.x.f()} - ${w.myCar.y.f()} a: ${s.myCarAngle.f()} as PI: ${w.myCar.angle.asPi().f()} " +
                "angleSpeed ${w.myCar.angleSpeed}")

       // move.d("enemyXY ${w.enemyCar.x.f()} - ${w.enemyCar.y.f()}")


        when (match.carType) {
            CarType.Bus -> {

                when (match.mapType) {
                    MapType.PillMap -> {
                    }
                    MapType.PillHubbleMap -> {
                    }
                    MapType.PillHillMap -> {
                        doPillHillMapStrat { doBusStart() }
                        return
                    }
                    MapType.PillCarcassMap -> {
                    }
                    MapType.IslandMap -> {
                    }
                    MapType.IslandHoleMap -> {
                    }
                }
                doBusStart()
                return
            }
            CarType.Buggy, CarType.SquareWheelsBuggy -> {

                when (match.mapType) {
                    MapType.PillMap -> {
                    }
                    MapType.PillHubbleMap -> {
                    }
                    MapType.PillHillMap -> {
                        doPillHillMapStrat { doSimpleAngleStrat() }
                        return
                    }
                    MapType.PillCarcassMap -> {
                    }
                    MapType.IslandMap -> {
                    }
                    MapType.IslandHoleMap -> {
                    }
                }

                doSimpleAngleStrat()
            }
        }
    }

    private fun doPillHillMapStrat(afterReach: () -> Any) {
        val x = w.myCar.getMirroredX()
        var cmd = 1
        val accelerationX = if (isBus) 120 else 270
        val stopOnX =  if (isBus) 444 else 400

        s.reach120x = x < accelerationX || s.reach120x

        s.reach440x = (s.reach120x && x > stopOnX) || s.reach440x
        if (tick < 30) {
            cmd = 0
        } else if (s.reach440x) {
            s.allowedToAttack = false
            run(afterReach)
            return
        } else if (s.reach120x) {
            cmd = 1
        } else {
            cmd = -1
        }

        move.set(cmd * w.myCar.side)

    }

    fun doSimpleAngleStrat() {
        var desiredAngle = (HALF_PI * 0.7f) * getMySide()

        var cmd = 1

        val delta = s.myCarAngle - desiredAngle
        if (delta > 0) {
            cmd *= -1
        }

        val isCloseToPerfectAngle = abs(delta) < HALF_PI / 2

        if (tick < ON_AIR_PAUSE && isCloseToPerfectAngle && tick % 5 != 0 && !isBus) {
            cmd *= -1
        }



        if (tick > 50 && tick % 5 != 0 && !isBus && s.allowedToAttack) {
            val myX = w.myCar.x
            val enemyX = w.enemyCar.x

            var leftCmd = -1
            var rightCmd = 1
            if (abs(s.myCarAngle) > 1) {  //whut
                //move.d("strange switch on")
                rightCmd = leftCmd
                leftCmd = 1
            }
            cmd = if (myX > enemyX) leftCmd else rightCmd
        }

        move.set(cmd)
    }

    fun doBusStart() {
        val myCarAngle = w.myCar.angle

        var desiredAngle = (HALF_PI * 1) * getMySide()
        val minButtonY = getMinButtonY(w.myCar)
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

            if (abs(w.myCar.angleSpeed) > 0.00436248
                    && ((cmd < 0 && w.myCar.angleSpeed < 0) || (cmd > 0 && w.myCar.angleSpeed > 0))) {
                cmd *= -1
            }

            move.set(cmd)

            return
        }
        var cmd = 1

        val delta = myCarAngle - desiredAngle
        if (delta > 0) {
            cmd *= -1
        }
    }

    private fun getMinButtonY(myCar: Car): Float {
        return match.buttonPoly.map {
            var point = it
            if (getMySide() == -1){
                point = Point2D(-it.x, it.y)
            }
            val rotated = point.rotate(myCar.angle.toDouble())
            rotated.y
        }.min()!!.toFloat()
    }

    private fun getRotatedButtonPoly(myCar: Car): List<Point2D> {

        return match.buttonPoly.map { it.rotate(myCar.angle.toDouble()) }
    }


    fun pretick(move: Move, world: World) {
        this.move = move;
        if (tick != 0) {
            world.processPre(this.w)
        }
        this.w = world;
        tick++

        if (!debugMessage.isEmpty()) {
            move.d(debugMessage)
            debugMessage = ""
        }
    }

    fun getMySide() = w.myCar.side


    override fun onParsingError(message: String) {
        debugMessage = message
    }

}

class State {

    //pillHill
    var reach120x: Boolean = false
    var reach440x: Boolean = false
    var myCarAngle: Float = 0f
    var allowedToAttack: Boolean = true
}
