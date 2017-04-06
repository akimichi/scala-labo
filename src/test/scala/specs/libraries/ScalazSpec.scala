import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * Scalazライブラリの基本的な用法を示す
 * c.f. http://code.google.com/p/scalaz/
 */
// class ScalazSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
//   import scalaz._
//   import Scalaz._
  
//   describe("Scalazを用いて") {
//     describe("Either"){
//       def r(n: Int): Either[String, Int] = Right(n)
      
//       {
//         for {
//           x <- r(1);
//           y <- r(x)
//         } yield x+y
//       }  should equal(2.right)

//       {
//         for {
//           x <- 1.right[String]
//           y <- x.right[String]
//         } yield x+y
//       } should equal(2.right)
//     }
//     describe("Tree"){
//       import collection.immutable.Stream
      
//       val tree: Tree[Int] = node(1,
//                                  Stream(leaf(2),
//                                         node(3, Stream(leaf(4)))))
      
//       // A tree of TreeLocs (aka Zipper). Each TreeLoc is rooted at `tree` but focussed on a different node.
//       val allTreeLocs: Tree[TreeLoc[Int]] = tree.loc.cojoin.toTree
//       // Getting the label of the focussed node from each TreeLoc restores the original tree
//       allTreeLocs.map(_.getLabel) assert_≟ tree
//       // Alternatively, we can get the path to root from each node
//       allTreeLocs.map(_.path).drawTree.println
      
//       /**
//        * Returns the paths from each leaf node back to the root node.
//        */
//       def leafPaths[T](tree: Tree[T]): Stream[Stream[T]]     = tree.loc.cojoin.toTree.flatten.filter(_.isLeaf).map(_.path)
//       leafPaths(tree).toList.map(_.toList.reverse)  assert_≟ List(List(1, 2), List(1, 3, 4))
      
//       it("Treeを使う"){
//         /*
//          * sealed trait Tree[A] {
//          *   /** The label at the root of this tree. */
//          *   def rootLabel: A
//          *   /** The child nodes of this tree. */
//          *   def subForest: Stream[Tree[A]]
//          * }
//          */ 
//         tree.rootLabel should equal(1)
//         println("tree.drawTree.println")
//         tree.drawTree.println
//         // tree.subForest.mkString should equal("")
//       }
//       describe("freeTreeの例"){
//         def freeTree: Tree[Char] =
//           'P'.node(
//             'O'.node(
//               'L'.node('N'.leaf, 'T'.leaf),
//               'Y'.node('S'.leaf, 'A'.leaf)),
//             'L'.node(
//               'W'.node('C'.leaf, 'R'.leaf),
//               'A'.node('A'.leaf, 'C'.leaf)))
//         val loc:scalaz.TreeLoc[Char] =  freeTree.loc
//         val result = freeTree.loc.getChild(2) >>= {_.getChild(1)} >>= {_.getLabel.some}
//         result should equal('W'.some)
//         it("findChild"){
//           /** Select the first immediate child of the current node that satisfies the given predicate.
//            *  def findChild(p: Tree[A] => Boolean): Option[TreeLoc[A]] = ...
//            */ 
//           freeTree.loc.findChild{_.rootLabel.some == 'L'.some}.get.getLabel should equal('L')
//           // freeTree.loc.findChild{_.rootLabel.some == 'R'.some}.get.getLabel should equal('L')
//           // freeTree.flatten.tail.map(x => freeTree.loc.find(_.tree.rootLabel == 'L')).force should equal(None)
//           // freeTree.flatten.drawTree.println
//         } 
//       }
//       describe("FileLikeの例"){
//         object test {
//           trait FileLike {
//             def name:String
//           }
//           case class File(name:String) extends FileLike
//           case class Directory(name:String) extends FileLike
//           implicit val FileShow = new Show[File] {
//             def show(a: File) = "File(%s)".format(a.name).toList
//           }
//           implicit val DirectoryShow = new Show[Directory] {
//             def show(a: Directory) = "Directory(%s)".format(a.name).toList
//           }
//         }
//         it("FileLike"){
//           import test._
//           val tree: Tree[FileLike] = node(Directory("root"), Stream(leaf(File("a")), node(Directory("child"), Stream(leaf(File("b"))))))
//           val allTreeLocs: Tree[TreeLoc[FileLike]] = tree.loc.cojoin.toTree
          
//           assert(true)
//         }
//       }
//     }

//     describe("Validationモナドを使う"){
//       it("throwsで例外を捕捉する"){
//         (() => "100".toInt).throws match {
//           case Success(the_value) => {
//             the_value should equal(100)
//           }
//           case Failure(f) => fail()
//         }
//       }
//     }
//     describe("ValidationNEL"){
//       def parseAddress(in: String): ValidationNEL[String,List[String]] = {
//         val address = in.split(",\\s*")
//         if (address.length > 1)
//           address.toList.successNel[String]
//         else
//           ("An address needs to have a street and a city, separated by a comma; found ["+in+"]").failNel[List[String]]
//       }
//       parseAddress("tokyo") match {
//         case Success(the_value) => fail()
//         case Failure(f) => f.isInstanceOf[NonEmptyList[String]] should equal(true)
//       }
//       parseAddress("Bart Schuller;2012-02-29;Some Street 123, Some Town") match {
//         case Success(the_value) => {
//           the_value(0) should equal("Bart Schuller;2012-02-29;Some Street 123")
//         }
//         case Failure(f) => fail()
//       }

//       it("for内包表現中で使う(成功のみのケース)"){
//         val result = for{
//           tokyo <- parseAddress("Akimichi Tatsukawa;2013-02-29;Some Street 321, Tokyo")
//           town <- parseAddress("Bart Schuller;2012-02-29;Some Street 123, Some Town")
//         } yield {
//           tokyo:::town
//         }
//         info("パターンマッチを使う")
//         result match {
//           case Success(the_value) =>
//             // println(the_value)
//             the_value should equal(List("Akimichi Tatsukawa;2013-02-29;Some Street 321",
//                                         "Tokyo",
//                                         "Bart Schuller;2012-02-29;Some Street 123",
//                                         "Some Town"))
//           case Failure(nel) => {
//             fail()
            
//           }
//         }
//       }
//       it("for内包表現中で使う(失敗を含むケース)"){
//         val result = for{
//           tokyo <- parseAddress("tokyo")
//           town <- parseAddress("Bart Schuller;2012-02-29;Some Street 123, Some Town")        
//         } yield {
//           tokyo:::town
//         }
//         info("パターンマッチを使う")
//         result match {
//           case Success(the_value) => fail()
//           case Failure(nel) => {
//             nel.head should equal("An address needs to have a street and a city, separated by a comma; found [tokyo]")
//           }
//         }
//         info("foldを使う")
//         result.fold(err => {err.head should equal("An address needs to have a street and a city, separated by a comma; found [tokyo]")},
//                   succ => {fail()})
//         info("<**>を使う")
//         val k3 = (parseAddress("tokyo") <**> parseAddress("Bart Schuller;2012-02-29;Some Street 123, Some Town")){ _:::_ }
//         k3 match {
//           case Success(the_value) => fail()
//           case Failure(nel) => {
//             nel.head should equal("An address needs to have a street and a city, separated by a comma; found [tokyo]")
//           }
//         }
//       }
//     }
    
//     // it("NonEmptyList"){
//     //   1.wrapNel should equal(NonEmptyList(1))
//     // }

//     it("Show"){
//       3.shows should equal("3")
//     }
//     /*
//      * https://github.com/jrwest/learn_you_a_scalaz/blob/master/1-intro.md
//      * 
//      */ 
//     describe("Option型を使う"){
//       some(3) map { _ + 1 } should equal{
//         Some(4)
//       }
//       (none : Option[Int]) map { _ + 1 } should equal{
//         None
//       }
//       3.some map { _ + 1 } getOrElse 2 should equal{
//         4
//       }

//       (none : Option[Int]) map { _ + 1 } getOrElse 11 should equal{
//         11
//       }
//       it("|演算子は getOrElse と同等"){
//         (10.some | 0) should equal(10)
//         ((none : Option[Int]) | 11 ) should equal{
//           11
//         }
//       }
//       it("<|*|>演算子で2つのOption型を処理する"){
//         3.some <|*|> 2.some match {
//           case Some((a, b)) if a == b => {
//             fail()
//           }
//           case Some((_, _)) => {
//             assert(true)
//           }
//           case None => fail()
//         }
//       }
//     }
//     describe("Memoization"){
//       object test {
//         case class Foo(value:Int)
//         case class Bar(value:Int)
//         def expensive(f:Foo) = Bar(f.value)
          
//         val memo = immutableHashMapMemo {
//           foo:Foo => expensive(foo)
//         }
        
//       }
//       import test._
//       val f:Foo = Foo(1)
//       memo(f) should equal(Bar(1))
//     }
//   }
// }


