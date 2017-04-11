scala-labo
==========

Scalaの実験室。
Scala の言語機能や関連ライブラリーについて、その用法を試すためのレポジトリ。


![](https://raw.github.com/wiki/akimichi/scala-labo/images/screenshot.png)


# 実行条件

* sbt 
* mongodb
  バージョン2.1以上がローカル環境にインストールされていることが必要となる。

ただし下記のdockerを利用する場合は、dockerのみ必要となる。

## インストール

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

## dockerを使う

Dockerfile.baseからdockerイメージを生成する。
オプション -t でイメージ名を指定する。
イメージ名は、なんでもかまわない。

~~~
$ docker build -t="akimichi/scala-labo:v1" - < Dockerfile.base
~~~

sbtをインタラクティブモードで利用するには、以下のコマンドを実行する。
sbtコンソールにはいるので、testコマンドを入力する。

~~~
$ docker run -it --rm  -v $(pwd):/workspace/scala akimichi/scala-labo:v1 sbt
~~~

sbt上でテストを実行するだけならば、以下のコマンドを実行する。

~~~
$ docker run --rm  -v $(pwd):/workspace/scala akimichi/scala-labo:v1 sbt test
scala-labo> test
~~~

