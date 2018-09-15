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

    init {
        myLives = params.getInt("my_lives")
        enemyLives = params.getInt("enemy_lives")
        carId = params.getJSONObject("proto_car").getInt("external_id")
        mapId = params.getJSONObject("proto_map").getInt("external_id")
        // ...
    }
}
