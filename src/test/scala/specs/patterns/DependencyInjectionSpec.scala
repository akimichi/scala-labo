import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * 依存性注入 Dependency Injection の基本的な方法を示す
 *
 * 
 * 
 */
class DependencyInjectionSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  /*
  describe("Connection Readerによる依存性注入の例") {
    case class Connection
    case class DB[A](g: Connection => A) {
      def apply(c:Connection) = g(c)
      def map[B](f: A => B): DB[B] = {
        conn:A => f(g(conn))
      }
      def flatMap[B](f: A => DB[B]):DB[B] = conn => f(g(conn))(conn)
    }
    it("Connection Reader を使う") {
      class FILE extends Connection
      class MEMORY extends Connection
      val file = new FILE
      val filedb = DB(file)
      filedb should equal("")
    }
  }
  */
  // describe("lazy val と関数渡しによる依存性注入の例") {
  //   case class DomainModel(val field:String) {
  //     val service_name:String
  //     def calc:String = {
  //       service_name
  //     }
  //   }
  //   trait ServiceConfig {
  //     val service_name:String
  //   }
  //   // class ServiceConfig(val service_name:String) extends ServiceConfig
  //   it("") {
  //     val domain_instance = new DomainModel("value") with ServiceConfig{ val service_name = "test"}
  //     domain_instance.calc should equal("")
  //   }
  // }
  describe("objectに対する実行時の依存性注入の例") {
    case class Service(name:String)
    object ServiceInTest extends Service("test")
    object ServiceInProduction extends Service("production")
   
    abstract class AbstractInterface {
      def service_name:Service => String
    }
    object ConcreteObject extends AbstractInterface {
      override def service_name:Service => String = {
        service => {
          service.name
        }
      }
    }
    it("カリー化によってobjectに依存性を注入する手法") {
      ConcreteObject.service_name(ServiceInTest) should equal("test")
      ConcreteObject.service_name(ServiceInProduction) should equal("production")
    }
  }
  describe("引数渡しによるIoC ") {
    trait DBConnection {
      def authenticate(user:String,password:String):Boolean = true
    }
    class DBClient {
      def authenticate(user:String,password:String, conn:DBConnection):Boolean = {
        conn.authenticate(user,password)
      }
    }
    it("クライアントインスタンスのメソッドによって依存性を注入する手法") {
      val connection = new DBConnection {}
      val client = new DBClient
      /** authenticateメソッドの引数に、依存する DBConnectionのインスタンスを注入している */
      client.authenticate("user","password",connection) should equal(true)
      /* しかしこの手法では依存対象を引数として渡し続けなければならない。
       * したがってメソッドの階層が深くなると管理が複雑になる欠点がある。
       */
	}
  }
  describe("カリー化によるIoC ") {
    trait DBConnection {
      def authenticate(user:String,password:String):Boolean = true
    }
    /**
     * DBClientの authenticateメソッドは、 DBConnection に依存している。その依存対象を関数で渡している。
     */ 
    class DBClient {
      def authenticate(user:String,password:String): DBConnection => Boolean = {
        conn => {
          conn.authenticate(user,password)
        }
      }
    }
    it("カリー化によって関数渡しで依存性を注入する手法") {
      val connection = new DBConnection {}
      val client = new DBClient
      client.authenticate("user","password")(connection) should equal(true)
    }
    it("カリー化によって関数渡しで依存性を注入する手法は、実行を遅延できる") {
      val client = new DBClient
      val authenticator = client.authenticate("user","password")
      /* 必要に応じて、適切な DBConnection に対して authenticate を実行できる
       * すなわち、関数を委譲の対象とすることで、委譲先の実行のタイミングを任意の時点まで遅延できる。
       */
      val connection = new DBConnection {}
      authenticator(connection) should equal(true)
    }
    describe("DataMapper,DomainModelの依存性解消をカリー化によるDIで解決する ") {
      trait DBCollectionInterface {
        val collection_name:String
      }
      class DBCollection(val collection_name:String) extends DBCollectionInterface
      
      trait DataMapperInferface {
        val underlying_collection:DBCollectionInterface
      }
      class DataMapper(val collection:DBCollectionInterface) extends DataMapperInferface {
        val underlying_collection:DBCollectionInterface = collection
      }
      object DataMapperFactory {
        def create: DBCollection => DataMapper = {
          collection => {
            new DataMapper(collection)
          }
        }
      }
      val collection = new DBCollection("collection_name")
      val data_mapper_factory = DataMapperFactory
      val data_mapper:DataMapper = data_mapper_factory.create(collection)
      data_mapper.underlying_collection.collection_name should equal("collection_name")
    }
  }
  describe("Config,Database,DataMapperの例") {
    // Config
    trait ConfigComponent {
      trait ConfigInterface
      def config:ConfigInterface
      
      abstract class HttpConfig extends ConfigInterface
      object DefaultConfig extends HttpConfig
    }
    trait ConfigServiceComponent extends ConfigComponent {
      def service_name: String
      def config = DefaultConfig
    }
    // Database depends upon ConfigComponent
    trait DatabaseComponent { self : ConfigComponent =>
      trait DatabaseInterface { def underlying_database = "underlying_database" }
      def database: DatabaseInterface

      case class Database(service_name:String) extends DatabaseInterface
    }

    /* service_name によって Database を切りかえる */
    trait DatabaseServiceComponent extends DatabaseComponent with ConfigServiceComponent {
      def service_name: String
      
      val database: DatabaseInterface = Database(service_name)
    }
    it("Databaseを実体化する") {
      object TestDatabase extends DatabaseServiceComponent {
        def service_name = "test"
      }
      TestDatabase.service_name should equal("test")
      
      var test_database_service = new DatabaseServiceComponent {
        def service_name = "test"
      }
      test_database_service.service_name should equal("test")
      
      case class DatabaseFactory(val service_name:String) extends DatabaseServiceComponent
      test_database_service = DatabaseFactory("test")
      test_database_service.service_name should equal("test")
      test_database_service.database.underlying_database should equal("underlying_database")
    }
  }
  describe("Database,DataMapperの例") {
    trait ConfigComponent {
      trait ConfigInterface
      def config:ConfigInterface
      object DefaultConfig extends ConfigInterface
    }
    trait ConfigServiceComponent extends ConfigComponent {
      def service_name: String
      def config = DefaultConfig
    }
    trait DatabaseComponent { self : ConfigComponent =>
      trait DatabaseInterface { def underlying_database = "underlying_database" }
      def database: DatabaseInterface

      case class Database(service_name:String) extends DatabaseInterface
    }
    trait DatabaseServiceComponent extends DatabaseComponent with ConfigServiceComponent {
      def service_name: String
      def database: DatabaseInterface = Database(service_name)
    }

    trait DataMapperComponent { self: DatabaseServiceComponent =>
      def collection_name: String
      trait DataMapperInterface {
        def underlying_collection:String
      }
      def data_mapper: DataMapperInterface

      case class DataMapper(val collection_name:String) extends DataMapperInterface {
        def underlying_collection = collection_name
      }
    }
    trait DataMapperFactoryComponent extends DataMapperComponent with DatabaseServiceComponent {
      def service_name: String
      def collection_name: String
      //lazy val data_mapper: DataMapperInterface = DataMapper(collection_name)
      def data_mapper: DataMapperInterface = DataMapper(collection_name)
    }
    it("object で DataMapperを実体化する") {
      object TestDataMapper extends DataMapperFactoryComponent {
        def service_name = "test_service"
        def collection_name = "test_collection"
      }
      TestDataMapper.service_name should equal("test_service")
      TestDataMapper.data_mapper.underlying_collection should equal("test_collection")
    }
    it("あらかじめ service_name のみ指定し、インスタンス生成時に collection_name を決定する方法") {
      case class TestDataMapperFactory(val collection_name:String) extends DataMapperFactoryComponent {
        val service_name = "test"
      }
      val test_data_mapper_archetype = TestDataMapperFactory("archetypes")
      test_data_mapper_archetype.data_mapper.underlying_collection should equal("archetypes")
    }
    it("あらかじめ collection_name のみ指定し、インスタンス生成時に service_name を決定する方法") {
      case class ArchetypeDataMapperFactory(val service_name:String) extends DataMapperFactoryComponent {
        val collection_name = "archetypes"
      }
      val archetype_data_mapper_test = ArchetypeDataMapperFactory("test")
      archetype_data_mapper_test.data_mapper.underlying_collection should equal("archetypes")
      
    }
    it("無名クラスのインスタンス内に collection_name と service_name を決定する方法") {
      val test_archetypes_data_mapper = new DataMapperFactoryComponent {
        val service_name = "test"
        val collection_name = "archetypes"
      }
      test_archetypes_data_mapper.data_mapper.underlying_collection should equal("archetypes")
    }
  }

}


