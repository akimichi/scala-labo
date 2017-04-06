import org.specs2.mutable._

/**
 * Specs2の基本的な用法を示す
 */
class Specs2UsageSpec extends Specification {
  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size(11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
    "must not beNull" in {
      "something" must not beNull
      // 1 must beEqualTo(1)
      // 1 must be equalTo(1)	
    }
    "1 must not beNull" in {
      1 must beEqualTo(1)
      1 must be equalTo(1)
      1 must not beNull
    }
  }
}
