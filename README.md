scala-labo
==========

Scalaの実験室。
Scala の言語機能や関連ライブラリーについて、その用法を試すためのレポジトリ。

= 実行条件

* sbt 
* mongodb
  mongodb 2.1以上がローカル環境にインストールされていることが必要となる。


= 使い方 

~~~
> sbt
~~~

全てのテストを実行する。

~~~
sbt> test
~~~

mongodbのテストを除外したい場合は、以下のようにDatabaseTestでタグ付けされたテストを除外する。

~~~
sbt> test-only -- -l DatabaseTest
~~~



