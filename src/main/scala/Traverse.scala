import scala.util.Try

object Traverse {

  implicit class SeqTry2TrySeq[E](val s: Seq[Try[E]]) extends AnyVal {
    def toTrySeq: Try[Seq[E]] = (Try(Seq[E]()) /: s) { (a, b) =>
      a flatMap (c => b map (d => c :+ d))
    }
  }

}
