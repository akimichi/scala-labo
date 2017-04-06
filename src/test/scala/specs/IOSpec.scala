import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * IO処理の基本的な用法を示す
 * 
 */
class IOSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("ファイル操作") {
    it("scala.io.Sourceを用いてファイルを読みこむ"){
      import scala.io._
      val source = try {
        Source.fromFile("src/test/resources/zips.json","UTF-8")

      } catch {
        case ex:java.io.FileNotFoundException => throw ex
        case ex => throw ex
      }
      source.getLines.length should equal(29470)
      source.mkString.startsWith("""[{"city": "ACMAR", "loc": [-86.51557, 33.584132], "pop": 6055, "state": "AL", "_id": "35004"}""")
      /** @notice 必ず最後に close する必要がある */
      info("必ず最後に close する必要がある")
      source.close()
    }
    // describe("scalax.ioを用いて"){
    //   import scalax.io.Resource
    //   it("ファイル名を指定して読みこむ"){
    //     val resource = Resource.fromFile("src/test/resources/zips.json")
    //     resource.slurpString.size should equal(2871006)
    //   }
    //   it("あるディレクトリ内のファイルを読みこむ"){
    //     import scalax.io._
    //     import scalax.file.Path
    //     import scalax.file.PathSet
    //     import scalax.file.defaultfs.DefaultPath

    //     val paths = Path("src/test/resources",'/').children().filter { file =>
    //       file.isFile && file.name.endsWith(".json")
    //     }
    //     paths.foreach { path =>
    //       val jfile:java.io.File = path match {
    //         case x:DefaultPath => x.jfile
    //       }
    //       val content = scala.io.Source.fromFile(jfile).mkString
    //       println(content)
    //       // val contents = scala.io.Source.fromFile(path.toString).mkString
    //       // println(contents)
    //       path.name.endsWith(".json") should equal(true)
    //     }
    //   }
    // }
  }
}
   

