package ciris

import cats.effect.Blocker
import java.nio.file.Path
import cats.effect.IO
import cats.effect.ContextShift
import scala.jdk.CollectionConverters._
import java.io.StringReader

package object propsfile {
  def propsFileConfigSource(blocker: Blocker)(path: Path)(implicit shift: ContextShift[IO]): IO[String => ConfigValue[String]] = {
    file(path, blocker).load[IO].flatMap { fileContents =>
      IO.delay {
        val props = new java.util.Properties
        props.load(new StringReader(fileContents))
        props.asScala
      }
    }.map { x =>
      configKey =>
        x.get(configKey).fold[ConfigValue[String]] {
          ConfigValue.missing(ConfigKey(configKey))
        } { value =>
          ConfigValue.loaded(ConfigKey(configKey), value)
        }
    }
  }
  
}
