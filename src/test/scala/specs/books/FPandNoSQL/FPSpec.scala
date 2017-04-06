import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{ FunSpec, BeforeAndAfterAll, BeforeAndAfterEach }


/**
 * 関数型プログラミングの例
 */
/*
 # 総論 関数型プログラミング
 ## なぜ関数型プログラミングが重要か
    複雑性への対処と並列化の利点を説明する。
 ## 1級市民としての関数
 ### λ式
 ### 高階関数
 #### map
 #### fold
 #### Loanパターンによるリソース処理の紹介
 ### カリー化による遅延評価
 ## 書き換え戦略と不変性
 ### 参照透明性
 ### persistent collection としての Vector の例
 ## 関数合成
 ## 部分適用
 
 
 */
class FPSpec extends FunSpec with ShouldMatchers with BeforeAndAfterAll {
  describe("導入"){
    describe("typesafe stackによるscala開発環境の導入"){}
    describe("sbtによるscala用プロジェクトの準備"){}
  }
  describe("総論 関数型プログラミング") {
    describe("なぜ関数型プログラミングが重要か"){
      info("複雑性への対処と並列化の利点を説明する。")
    }
    describe("1級市民としての関数"){
      describe("クロージャとしてのラムダ式"){
        val square = (x:Int) => x * x
        square(2) should equal(4)
        val succ = (x:Int) => x + 1
        succ(10) should equal(11)
      }
      describe("高階関数"){
        info("map関数")
        info("畳み込み関数")
        info("Loanパターンによるリソース処理の紹介") 
      }
      describe("カリー化による遅延評価"){}
    }
    describe("関数の適用について"){
      describe("関数合成 compose"){}
      describe("部分適用"){
        List(1,2,3,4,5).collect {
          case n:Int if n % 2 == 0 => n
        }.toList should equal{
          List(2,4)
        }
      }
    }
    describe("代数型データ構造") {
      describe("List. Why Functional Programming Matters,p.6"){}
      describe("Tree. Why Functional Programming Matters,p.7"){}
      describe("無限ストリーム Stream"){
        val f = (x: Int) => {  
          println(x)  
          x  
        }
        List(1, 2, 3, 4, 5).map(f).find(_ == 3) should equal(Some(3))
        info("Streamを使うと余分な関数適用を回避できる")
        Stream(1, 2, 3, 4, 5).map(f).find(_ == 3) should equal(Some(3))
      }
      describe("並列コレクション"){}
      describe("Conc List の例"){
        /* c.f.http://www.infoq.com/presentations/Thinking-Parallel-Programming */ 
      }
    }
    describe("参照透明性"){
      describe("不変なデータ"){}
      describe("不変コレクションとしての Vector の例"){}
    }
    describe("型システム"){
      describe("Typeclass によるアドホック多相"){}
      describe("Typeclass を利用した Cakeパターン"){}
      describe("Monad による処理の合成"){
        /*
        object test {
          case class Query[+A](value:A) {
            def map[B](f:A => B) : Config[B] = Config(f(value))
            def flatMap[B](f:A => Config[B]) : Config[B] = f(value)
          }
          def filter(p:Elem => Boolean):List[Elem] = elems.filter(p)
          def where(pred:T => Bool)(data:T) : List[T] = {
            if(pred(data))
              List(data)
            else
              Nil
          }
        }
        it(""){
          for {
            data:T <- collection:List[T]
            result:Option[T] <- where(predicate:T => Bool)(data:T)
          } yield {
            result
          }
        }
        */
      }
    }
    describe("再帰処理"){
      info("再帰的なデータ構造には、再帰的な反復が自然である。")
      describe("末尾再帰"){}
      describe("メモ化による再帰処理の高速化"){}
      describe("(Yコンビネータ)"){}
    }
  }
  describe("各論 NoSQL"){
    describe("KVSで汎用タガーを自作してみよう"){
      describe("シンプルなKVSを使う"){
        describe("KyotoCabinet とは"){}
        describe("KyotoCabinet をScalaから使う"){}
      }
      describe("グラフ構造を探索する"){
        describe("ラティス構造と最短パス探索アルゴリズム"){}
        describe("Scala での実装"){}
      }
      describe("汎用タガーを作ってみよう"){
        describe("ScalaでCommon Prefix Search をする"){}
        describe("KVSを用いてラティスを作成する"){}
        describe("ラティスに対する最短経路探索"){}
      }
      describe("KVSの応用例"){
        describe("日本語形態素解析器を作ってみよう"){}
        describe("その他の応用例"){
          describe("自動リンク付け器（はてなキーワードとか）"){}
          describe("スパムフィルタ"){}
        }
      }
    }
    describe("MongoDB"){
      describe("MongoDBの特徴"){
        describe("ドキュメント指向データベース"){}
        describe("さまざまなクエリーを実行する"){}
        describe("RDBと比較したときの MongoDBの制限"){}
      }
      describe("Scalaから MongoDB を使う"){
        describe("Casbahライブラリの紹介"){}
        describe("Loanパターンで安全にリソースを処理する"){}
        describe("Cakeパターンでデータベースの接続を管理する"){}
        describe("データマッパー、ドメインモデルを作成する"){}
      }
      describe("MongoDBの応用例"){
        describe("疑似XMLデータベースを開発する"){
          describe("case class を利用して、JSONのモデルを作る"){}
          describe("Typeclass を利用して、XMLインスタンスをJSONインスタンスに変換する"){}
        }
        describe("join操作を、Akka/Futureを用いて並列実行する"){
          describe("MongoDBのクエリーをFutureで非同期に処理する"){}
          describe("Akkaでクエリーを並列実行させながらjoinを実施する"){}
        }
        describe("Play Framework の WebSocket を利用して、 MongoDBと対話的に交信する"){
          describe("Iterateeの用法"){}
          describe("WebSocketでの双方向通信"){}
        }
      }
    }
    describe("Scalaで作るセマンティックウェブアプリケーション"){
      describe("RDFの紹介"){
        describe("RDFデータの特徴"){
          describe("なぜRDFか"){}
          describe("他のNoSQLデータとの違い"){}
        }
        describe("RDFデータを作る"){
          describe("事物を命名する、記述する"){}
          describe("データスキーマを定義する"){}
          describe("RDFデータを記録する"){}
        }
        describe("RDFデータを問い合わせる"){
          describe("SPARQLによる問い合わせ"){}
        }
        describe("どんな用途が向いているのか"){
          describe("Linked Open Data（LOD）の紹介"){}
          describe("LODを利用したアプリケーション例"){}
        }
      }
      describe("ScalaでRDFデータを利用する"){
        describe("Java API をラップして使う"){
          describe("Jena APIをラップ"){}
          describe("サンプル例"){}
        }
        describe("ネイティブのScala APIを使う"){
          describe("Scarardf を利用"){}
          describe("サンプル例"){}
        }
        describe("商用RDFストアを使う"){
          describe("Scala API for Allegro Graph"){}
          describe("サンプル例"){}
        }
      }
      describe("Scalaで活用するLinked Open Data"){
        describe("WWW上で利用可能なLOD"){
          describe("DB Pedia、臨床医学オントロジ??など"){}
          describe("Scala APIで問い合わせる"){}
          describe("SPARQLで問い合わせる"){}
        }
        describe("LODをマッシュアップ"){
          describe("LD Spiderで公開データを収集"){}
          describe("ローカルのRDFストアに格納"){}
          describe("SPARQLで問い合わせる"){}
        }
        describe("アプリケーション例"){
        }
      }
    }
  }
}

    

