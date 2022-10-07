def print_table(
    table: List[List[String]],
    titles: List[String],
    align: List[Int],
    padding: Int
) =
  val collens =
    for i <- 0 to (titles.length - 1)
    yield (for j <- List(titles) concat table yield j(i).length).max
  var div = 0
  for (i, len) <- (titles, collens).zipped do
    val line = i + " " * (len - i.length)
    print(line)
    print(" " * padding)
    div += line.length + padding
  div -= padding
  print("\n")
  println("-" * div)
  for row <- table do
    for (i, len, align) <- (row, collens, align).zipped do
      align match
        case 0  => print(i + " " * (len - i.length))
        case -1 => print(" " * (len - i.length) + i)
      print(" " * padding)
    print("\n")

def search(search_query: String) =
  var query: List[Torrent] = null
  try query = Torrent.query(search_query)
  catch
    case e: requests.TimeoutException =>
      println("Timed out")
      System.exit(1)
  print_table(
    table =
      for torrent <- query
      yield List(
        torrent.id.toString,
        torrent.name,
        torrent.size.toString,
        torrent.seeders.toString,
        torrent.leechers.toString,
        torrent.username,
        torrent.added.toString
      ),
    titles = List("ID", "Name", "Size", "SE", "LE", "User", "Added"),
    align = List(-1, 0, -1, -1, -1, 0, -1),
    padding = 3
  )

def magnetlink(id: String) =
  val magnetLink = Torrent.fromId(id.toInt).magnetLink
  println(magnetLink)

@main def main(args: String*): Unit =
  args(0) match
    case "--search" =>
      search(args(1))
    case "--magnetlink" =>
      magnetlink(args(1))
