package ScalaInDepth

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec, FunSuite}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import org.scalacheck.{Gen}
import org.scalacheck.Prop.forAll


/*
 * Chapter 11
 * 
 * 
 */ 
class Chap11Check extends FunSpec with ShouldMatchers with GeneratorDrivenPropertyChecks {

  trait Functor[T[_]] {
    def apply[A](x : A) : T[A]
    def map[A,B](x : T[A])(f : A=>B) : T[B]
  }
  trait Monad[T[_]] {
    def flatten[A](m : T[T[A]]) : T[A]
    def flatMap[A,B](x : T[A])(f : A => T[B])(implicit func : Functor[T]) : T[B] = flatten(func.map(x)(f))
  }
  
  // describe("11.2"){

  // }
  describe("11.4"){
    trait ManagedResource[T] {
      def using[U](f : T => U) : U
    }
    object ManagedResource {
      import com.mongodb.casbah.Imports._
      
      implicit def functorOps[F[_] : Functor, A](ma: F[A]) = new {
        val functor = implicitly[Functor[F]]
        final def map[B](f : A=>B) : F[B] = functor.map(ma)(f)
      }
      implicit def monadOps[M[_] : Functor : Monad, A](ma: M[A]) = new {
        val monad = implicitly[Monad[M]]
        def flatten[B](implicit $ev0: M[A] <:< M[M[B]]) : M[B] = monad.flatten(ma)
        def flatMap[B](f : A=>M[B]) : M[B] = monad.flatMap(ma)(f)
      }
      implicit object MrFunctor extends Functor[ManagedResource] {
        override final def apply[A](a : A) = new ManagedResource[A] {
          override def using[U](f : A => U) = f(a)
          override def toString = "ManagedResource(%s)".format(a)
        }
        override final def map[A,B](ma : ManagedResource[A])(mapping : A => B) = new ManagedResource[B] {
          override def using[U](f : B => U) = ma.using(mapping andThen f)
          override def toString = "ManagedResource.map(%s)(%s)".format(ma,mapping)
        }
      }
      implicit object MrMonad extends Monad[ManagedResource] {
        type MR[A] = ManagedResource[A]
        override final def flatten[A](mma : MR[MR[A]]) : MR[A] = new ManagedResource[A] {
          override def using[U](f : A => U) = mma.using(ma => ma.using(f))
          override def toString = "ManagedResource.flatten(%s)".format(mma)
        }
      }
      
      def connect() : ManagedResource[MongoConnection] = new ManagedResource[MongoConnection] {
        def using[U](f : MongoConnection => U) : U = {
          val connection = MongoConnection()
          try {
            f(connection)
          } finally {
            connection.close()
          }
        }
      }
      /*
      def db(dbname:String) : ManagedResource[MongoDB] = new ManagedResource[MongoDB] {
        def using[U](f : MongoDB => U) : U = {
          for {
            connection <- ManagedResource.connect()
            val result = f(connection(dbname))
          } yield result
        }
      }
      def col() : ManagedResource[MongoCollection] = new ManagedResource[MongoCollection] {
        def using[U](f : MongoCollection => U) : U = {
          for {
            db <- ManagedResource.db()
            coll <- db(colname)
          } yield f(coll)
        }
      }
      */ 
      def database(dbname:String) : ManagedResource[MongoDB] = for {
        connection <- ManagedResource.connect()
      } yield {
        connection(dbname)
      }
      def collection(db:MongoDB, colname:String) : ManagedResource[MongoCollection] = for {
        db <- MrFunctor(db)
      } yield {
        db(colname)
      }
      
    }

    case class mongoDB(dbname:String, colname:String) {
      import ManagedResource._
      import com.mongodb.casbah.Imports._

      def collection:ManagedResource[MongoCollection] = {
        for {
          db <- ManagedResource.database(dbname)
          collection <- ManagedResource.collection(db,colname)
        } yield collection
      }

      def drop() : ManagedResource[Unit] = {
        for {
          db <- ManagedResource.database(dbname)
          collection <- ManagedResource.collection(db,colname)
        } yield collection.drop()
      }
      def insert(data:MongoDBObject) : ManagedResource[Unit] = {
        for {
          db <- ManagedResource.database(dbname)
          collection <- ManagedResource.collection(db,colname)
        } yield collection.insert(data)
      }
      def find(query:MongoDBObject) : ManagedResource[Stream[DBObject]] = {
        def mkStream(cursor : MongoCursor) : Stream[DBObject] = cursor.toStream
        for {
          coll <- collection
          val cursor = coll.find(query)
        } yield {
          mkStream(cursor)
        }
      }
      
    }
    // it("mongoDBを使う"){
    //   import com.mongodb.casbah.Imports._
    //   val (dbname,colname) = ("scala-labo", "test")
    //   val client = mongoDB(dbname,colname)
    //   client.drop()
    //   for(i <- 1 to 100) {
    //     val data = MongoDBObject("id" -> i,
    //                              "name" -> "name%s".format(i),
	//                              "lname" -> "Simpson",
    //                              "fname" -> "Bobby",
    //                              "scores" -> MongoDBList(
    //                                MongoDBObject("score" -> i * 11,
    //                                              "dname" -> "ACADEMICS PLUS SCHOOL DISTRICT",
    //                                              "tyear" -> "2007",
	// 		                                     "grade" -> (i * 3).toString)))
    //     client.insert(data)
    //   }
      
    // }
  }
}
