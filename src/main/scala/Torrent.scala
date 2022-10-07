import scala.collection.mutable.ArrayBuffer

class Torrent(json_data: ujson.Value):
  val id: Long =
    json_data("id").strOpt.getOrElse(json_data("id").num.toLong.toString).toLong
  val name     = json_data("name").str
  val infoHash = json_data("info_hash").str
  val leechers =
    json_data("leechers").strOpt
      .getOrElse(json_data("leechers").num.toLong.toString)
      .toLong
  val seeders =
    json_data("seeders").strOpt
      .getOrElse(json_data("seeders").num.toLong.toString)
      .toLong
  val fileCount =
    json_data("num_files").strOpt
      .getOrElse(json_data("num_files").num.toLong.toString)
      .toLong
  val size =
    json_data("size").strOpt.getOrElse(json_data("size").num.toLong.toString).toLong
  val username = json_data("username").str
  val added =
    json_data("added").strOpt.getOrElse(json_data("added").num.toLong.toString).toLong
  val status = json_data("status").str
  val category =
    json_data("category").strOpt
      .getOrElse(json_data("category").num.toLong.toString)
      .toLong
  val imdb                 = json_data("imdb").strOpt
  var description: String  = null
  var language: String     = null
  var textLanguage: String = null

  def fetchMoreData =
    val json_data = ujson.read(
      requests.get("https://apibay.org/t.php", params = Map("id" -> this.id.toString))
    )
    this.description = json_data("descr").str
    this.language = json_data("language").str
    this.textLanguage = json_data("textlanguage").str

  def magnetLink: String =
    val trackers = List(
      "udp://tracker.coppersurfer.tk:6969/announce",
      "udp://9.rarbg.to:2920/announce",
      "udp://tracker.opentrackr.org:1337",
      "udp://tracker.internetwarriors.net:1337/announce",
      "udp://tracker.leechers-paradise.org:6969/announce",
      "udp://tracker.coppersurfer.tk:6969/announce",
      "udp://tracker.pirateparty.gr:6969/announce",
      "udp://tracker.cyberia.is:6969/announce"
    ).mkString("&tr=")
    s"magnet:?xt=urn:btih:${this.infoHash}&dn=${this.name}&tr=$trackers"

object Torrent:
  def query(searchQuery: String): List[Torrent] =
    (for i <- ujson
        .read(
          requests.get(s"https://apibay.org/q.php", params = Map("q" -> searchQuery))
        )
        .arr
    yield Torrent(i)).toList

  def fromId(id: Int): Torrent =
    Torrent(
      ujson.read(
        requests.get(s"https://apibay.org/t.php", params = Map("id" -> id.toString))
      )
    )
