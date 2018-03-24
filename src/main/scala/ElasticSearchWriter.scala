
import java.sql.{Connection, DriverManager}
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import net.liftweb.json.JsonDSL._
import net.liftweb.json.{NoTypeHints, Serialization}
import net.liftweb.json.Serialization.{read, write}
import com.sksamuel.elastic4s.http.ElasticDsl._

object ElasticSearchWriter extends App {

  case class Product(product_id: String, product_category_id: String, product_name: String, product_description: String, product_price: String, product_image: String)
  val url = "jdbc:mysql://10.0.0.3/retail_db"
  val client = HttpClient(ElasticsearchClientUri("localhost", 9200))
  implicit val formats = Serialization.formats(NoTypeHints)

  val driver = "com.mysql.jdbc.Driver"
  val username = "retail_dba"
  val password = "cloudera"

  var connection:Connection = _
  try {
    Class.forName(driver)
    val queryString = "select product_id, product_category_id, product_name, product_description, product_price, product_image from products"
    connection = DriverManager.getConnection(url, username, password)

    val statement = connection.createStatement
    val rs = statement.executeQuery(queryString)
    while (rs.next) {

      val product_id = rs.getString("product_id")
      val product_category_id = rs.getString("product_category_id")
      val product_name = rs.getString("product_name")
      val product_description = rs.getString("product_description")
      val product_price = rs.getString("product_price")
      val product_image = rs.getString("product_image")

      val product = Product(product_id, product_id, product_name, product_description, product_price, product_image)

      val json = ("product" ->("product_id" -> product_id)~("product_category_id" -> product_category_id)~("product_name" -> product_name)~("product_description"->product_description)~("product_price"->product_price)~("product_image"->product_image))
      println(json)

      client.execute {

        indexInto("products"/ "kamil").doc(write(json))
      }
    }
  } catch {
    case e: Exception => e.printStackTrace
  }
  client.close()

}