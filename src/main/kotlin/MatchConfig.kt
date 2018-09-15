import org.json.JSONArray
import org.json.JSONObject

/**
 * Параметры матча (характеристики машин, контуры карт и т.д.), присылаемые в начале каждого матча.
 * Передается на вход обработчика `onMatchStarted` интерфейса [Strategy]
 */

class MatchConfig(params: JSONObject) {
    //todo добавить нужные поля и классы и реализовать десериализацию json-объекта
    var myLives: Int = 0
    var enemyLives: Int = 0

    val carId: Int
    var mapId: Int

    var buttonPoly = ArrayList<Point2D>()

    init {
        myLives = params.getInt("my_lives")
        enemyLives = params.getInt("enemy_lives")
        val protoCar = params.getJSONObject("proto_car")
        carId = protoCar.getInt("external_id")

        buttonPoly.clear()
        val buttonPolyJson = protoCar.getJSONArray("button_poly")
        for (o in buttonPolyJson) {
            if (o is JSONArray) {
                buttonPoly.add(Point2D(o.getInt(0), o.getInt(1)))
            }
        }

        mapId = params.getJSONObject("proto_map").getInt("external_id")
        // ...
    }
}
