import org.json.JSONObject

/**
 * Параметры матча (характеристики машин, контуры карт и т.д.), присылаемые в начале каждого матча.
 * Передается на вход обработчика `onMatchStarted` интерфейса [Strategy]
 */

class MatchConfig
// ...

(params: JSONObject) {
    //todo добавить нужные поля и классы и реализовать десериализацию json-объекта
    internal var myLives: Int = 0
    internal var enemyLives: Int = 0

    init {
        myLives = params.getInt("my_lives")
        enemyLives = params.getInt("enemy_lives")
        // ...
    }
}
