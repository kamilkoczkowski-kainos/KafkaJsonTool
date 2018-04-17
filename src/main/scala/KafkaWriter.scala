import java.sql
import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import cakesolutions.kafka.KafkaProducer.Conf
import org.apache.kafka.common.serialization.StringSerializer
import net.liftweb.json.prettyRender
import java.sql.{Connection, DriverManager}
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import net.liftweb.json.JsonDSL._
import net.liftweb.json.{NoTypeHints, Serialization}

object KafkaWriter extends App {
  case class Customer(customer_id: String, customer_fname: String, customer_lname: String, customer_email: String, customer_password: String, customer_street: String, customer_city: String, customer_state: String, customer_zipcode: String)

  val url = "jdbc:mysql://10.0.0.3/retail_db"
  val client = HttpClient(ElasticsearchClientUri("localhost", 9200))
  implicit val formats = Serialization.formats(NoTypeHints)

  val driver = "com.mysql.jdbc.Driver"
  val username = "retail_dba"
  val password = "cloudera"
  var connection:sql.Connection = _

  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
  } catch {
    case e: Exception => e.printStackTrace
  }

    val queryString = "select customer_id, customer_fname, customer_lname, customer_email, customer_password, customer_street, customer_city, customer_state, customer_zipcode from customers"

    val statement = connection.createStatement
    val rs = statement.executeQuery(queryString)

  val producer = KafkaProducer(
    Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = "localhost:9092")
  )
    while (rs.next) {
      val customer_id = rs.getString("customer_id")
      val customer_fname = rs.getString("customer_fname")
      val customer_lname = rs.getString("customer_lname")
      val customer_email = rs.getString("customer_email")
      val customer_password = rs.getString("customer_password")
      val customer_street = rs.getString("customer_street")
      val customer_city = rs.getString("customer_city")
      val customer_state = rs.getString("customer_state")
      val customer_zipcode = rs.getString("customer_zipcode")

      val json = ("customer" -> ("customer_id" -> customer_id)~("customer_fname" -> customer_fname)~("customer_lname" -> customer_lname)~("customer_email"->customer_email)~("customer_password"->customer_password)~("customer_street"->customer_street)~("customer_city"->customer_city)~("customer_state"->customer_state)~("customer_zipcode"->customer_zipcode))
      println(json)

      producer.send(KafkaProducerRecord("test", Some("customer"), prettyRender(json)))

    }
  producer.close()
  client.close()
}