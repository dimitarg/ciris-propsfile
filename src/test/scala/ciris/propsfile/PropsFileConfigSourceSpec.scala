package ciris.propsfile

import cats.implicits._
import weaver.pure._
import fs2._
import cats.effect.IO
import ciris._
import cats.effect.Blocker
import java.nio.file.Paths

object PropsFileConfigSourceSpec extends Suite {

  final case class DbUrl(value: String)
  final case class Bla(value: Int)
  final case class Shrubbery(value: String)

  final case class AppConfig(dbUrl: DbUrl, bla: Bla, shrubbery: Option[Shrubbery])

  object AppConfig {
    def fromSource(src: String => ConfigValue[String]): ConfigValue[AppConfig] = {
      val db = src("DB_URL").map(DbUrl.apply)
      val bla = src("BLA").as[Int].map(Bla.apply)
      val shrubbery = src("SHRUBBERY").option.map(_.map(Shrubbery.apply))

      (db, bla, shrubbery).parMapN(AppConfig.apply)
    }
  }

  override def suitesStream: Stream[IO,Test] = 
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      Stream(
        test("can load config from file props source") {
          for {
            path <- IO(Paths.get(getClass().getResource("/app.props").toURI()))
            configSrc <- propsFileConfigSource(blocker)(path)
            config <- AppConfig.fromSource(configSrc).load[IO]
          } yield expect(config == AppConfig(
            DbUrl("jdbc:foo://bla"),
            Bla(32),
            None
          ))          
        }
      )
    }
  
}
