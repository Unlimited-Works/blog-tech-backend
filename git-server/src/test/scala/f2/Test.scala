package f2

import org.junit.Test

/**
  *
  */
class Test1 {

  @Test
  def test(): Unit = {
    import cats.effect.{IO, Sync}
    import fs2.{io, text}
    import java.nio.file.Paths

    def fahrenheitToCelsius(f: Double): Double =
      (f - 32.0) * (5.0 / 9.0)

    def converter[F[_]](implicit F: Sync[F]): F[Unit] = {
      val path = "/Users/lorancechen/version_control_project/_unlimited-works/git-server/src/test/resources"

      val x: fs2.Stream[F, Byte] = io.file.readAll[F](Paths.get(s"$path/fs.txt"), 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(s => !s.trim.isEmpty && !s.startsWith("//"))
        .map(line => fahrenheitToCelsius(line.toDouble).toString)
        .intersperse("\n")
        .through(text.utf8Encode)

      x.through(io.file.writeAll(Paths.get(s"$path/fs-output.txt")))
        .compile.drain

    }

    // at the end of the universe...
    val u: Unit = converter[IO].unsafeRunSync()
  }

}
