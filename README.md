scala-labo
==========

Scalaの実験室。
Scala の言語機能や関連ライブラリーについて、その用法を試すためのレポジトリ。



![](https://raw.github.com/wiki/akimichi/scala-labo/images/screenshot.png)


# 実行条件

* sbt 
* mongodb
  バージョン2.1以上がローカル環境にインストールされていることが必要となる。

例えば、ubuntu 16.04 では以下のようにインストールする。
なお、sbtは別途インストールする必要がある。

~~~
$ sudo apt-get install openjdk-8-jdk openjdk-8-jre mongodb-server  
~~~


# 使い方 

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



