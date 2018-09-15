import org.json.JSONArray
import org.json.JSONObject

/**
 * Состояние мира, присылаемое сервером на каждом тике.
 * Передается на вход обработчика `onNextTick` интерфейса [Strategy]
 */

class TickState(params: JSONObject) {
    var myCar: Car
    var enemyCar: Car
    var deadLine: Float = 0.toFloat()

    init {
        myCar = Car(params.getJSONArray("my_car"))
        enemyCar = Car(params.getJSONArray("enemy_car"))

        deadLine = params.getFloat("deadline_position")
    }

    inner class Car(carParam: JSONArray) {
        var side: Int = 1 // слева = +1, справа = -1

        var x: Float = 0f
        var y: Float = 0f
        var angle: Float = 0f

        var wheel = WheelPair()

        init {
            val pos = carParam.getJSONArray(0)
            x = pos.getFloat(0)
            y = pos.getFloat(1)

            angle = carParam.getFloat(1)
            side = carParam.getInt(2)

            wheel.rear = Wheel(carParam.getJSONArray(3))
            wheel.front = Wheel(carParam.getJSONArray(4))
        }

        inner class WheelPair {
            var rear: Wheel? = null
            var front: Wheel? = null
        }

        inner class Wheel(wheelParam: JSONArray) {
            var x: Float = 0f
            var y: Float = 0f
            var angle: Float = 0f

            init {
                x = wheelParam.getFloat(0)
                y = wheelParam.getFloat(1)
                angle = wheelParam.getFloat(2)
            }
        }
    }
}