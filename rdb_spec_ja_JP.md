#  カスタム機能ドキュメント

このドキュメントは、 のカスタム機能が提供する、
Flow、コマンド、設定定義についての説明及び出力するメッセージの定義について説明する。

- Contents
  - [Information](#Information)
  - [Description](#Description)
  - [Flow List](#Flow-List)
  - [Command List](#Command-List)
  - [Configuration List](#Configuration-List)
  - [Message List](#Message-List)

## Information

本カスタム機能の基本情報は以下の通り。

RDB（Relational DataBase）関連のコマンドを提供します。

- Name : `rdb`
- Custom Package : `com.epion_t3.rdb`

## Description
RDB（Relational DataBase）関連のコマンドを提供します。

## Flow List

本カスタム機能が提供するFlowの一覧及び詳細。

|Name|Summary|
|:---|:---|


## Command List

本カスタム機能が提供するコマンドの一覧及び詳細。

|Name|Summary|Assert|Evidence|
|:---|:---|:---|:---|
|[ExportRdbData](#ExportRdbData)|RDBのデータを抽出（エクスポート）します。エクスポートしたデータはエビデンスとしても保存可能になります。  ||X|
|[ImportRdbData](#ImportRdbData)|RDBに対してデータの挿入（インポート）を行います。  ||X|
|[ExecuteRdbScript](#ExecuteRdbScript)|RDBに対してスクリプト（SQL）を実行します。  |||
|[AssertRdbData](#AssertRdbData)|RDBのレコードの確認を行います。  ||X|
|[ExecuteRdbQuery](#ExecuteRdbQuery)|RDBに対してクエリー（SQL）を実行します。  |||

------

### ExportRdbData
RDBのデータを抽出（エクスポート）します。エクスポートしたデータはエビデンスとしても保存可能になります。
#### Command Type
- Assert : No
- Evidence : __Yes__

#### Functions
- RDBのデータを抽出（エクスポート）します。
- XMLで定義されたデータのエクスポートが行えます。（DBUnit）
- Excelで定義されたデータのエクスポートが行えます。（DBUnit）

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「ExportRdbData」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  rdbConnectConfigRef : RDBに対する接続先定義の参照ID # (1)
  dataSetType : (xml|flatXml|excel) # (2)
  tables : エクスポート対象のテーブルを定義 # (3)

```

1. RDBへの接続先の設定を行っている &#96;Configuration&#96; の参照IDを指定します。
1. DataSetの種類を指定します。DataSetとは、RDBのデータ構造を表したもので、DataSetには、CSV、XML、Excelの形式が選べます。本コマンドが利用するDataSetとはすべてDBUnitのDataSetを指します。現状では、CSVには対応ができておりません。
1. エクスポートを行う対象のテーブルについて細かく指定が行えます。
------

### ImportRdbData
RDBに対してデータの挿入（インポート）を行います。
#### Command Type
- Assert : No
- Evidence : __Yes__

#### Functions
- RDBに対してデータのインポートを行います。
- XMLで定義したデータのインポートが行えます。（DBUnit）
- Excelで定義したデータのインポートが行えます。（DBUnit）
- DBUnitで定義されているDataBaseOperationが利用可能です。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「ImportRdbData」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  rdbConnectConfigRef : RDBに対する接続先定義の参照ID # (1)
  dataSetType : (xml|flatXml|excel) # (2)
  operation : (insert|clean_insert|update|refresh) # (3)
  bind : 変数バインドを行うかどうかのフラグ # (4)
  value : インポートするDataSetのパス（相対） # (5)

```

1. RDBへの接続先の設定を行っている &#96;Configuration&#96; の参照IDを指定します。
1. DataSetの種類を指定します。DataSetとは、RDBのデータ構造を表したもので、DataSetには、CSV、XML、Excelの形式が選べます。本コマンドが利用するDataSetとはすべてDBUnitのDataSetを指します。現状では、CSVには対応ができておりません。
1. DataSetをインポートする際のロジックを指定します。Operationの詳細はJUnitのページを参照してください。
1. DataSetの中にVariableを利用するような変数参照が存在する場合に、バインドを行うかを指定できます。
1. インポートするDataSetのファイルのパスを相対パスで指定します。相対パスの基準ディレクトリは対象のシナリオファイルが配置している場所となります。 対象のシナリオファイルはパーツ定義に配置している場合はパーツのYAMLが配置している場所を指します。
------

### ExecuteRdbScript
RDBに対してスクリプト（SQL）を実行します。
#### Command Type
- Assert : No
- Evidence : No

#### Functions
- RDBに対してスクリプト（SQL）を実行します。
- スクリプト（SQL）へETTTの変数バインドが可能です。
- スクリプト（SQL）はUTF-8で読み込みます。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「ExecuteRdbScript」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  rdbConnectConfigRef : RDBに対する接続先定義の参照ID # (1)
  value : スクリプト（SQL）のパス（相対パス）

```

1. RDBへの接続先の設定を行っている &#96;Configuration&#96; の参照IDを指定します。
------

### AssertRdbData
RDBのレコードの確認を行います。
#### Command Type
- Assert : No
- Evidence : __Yes__

#### Functions
- RDBのレコードの確認を行います。
- 期待値はデータセット（DBUnit）で表します。
- 結果値は事前にエビデンスとしてエクスポート系のコマンドを利用して取得しておく必要があります。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「AssertRdbData」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  expectedDataSetPath : 期待値データセット相対パス # (1)
  expectedDataSetType : (xml|flatXml|excel) # (2)
  actualFlowId : 結果値を取得したFlowのFlowIDを指定 # (3)
  actualDataSetType : (xml|excel) # (4)
  tables : アサートの詳細設定

```

1. 期待値を定義したデータセットへの相対パス。このコマンドが配置されている場所からの相対パスになります。
1. DataSetの種類を指定します。DataSetとは、RDBのデータ構造を表したもので、DataSetには、CSV、XML、Excelの形式が選べます。本コマンドが利用するDataSetとはすべてDBUnitのDataSetを指します。現状では、CSVには対応ができておりません。
1. 結果値を取得したFlowIDを指定。
1. DataSetの種類を指定します。DataSetとは、RDBのデータ構造を表したもので、DataSetには、CSV、XML、Excelの形式が選べます。本コマンドが利用するDataSetとはすべてDBUnitのDataSetを指します。現状では、CSVには対応ができておりません。
------

### ExecuteRdbQuery
RDBに対してクエリー（SQL）を実行します。
#### Command Type
- Assert : No
- Evidence : No

#### Functions
- RDBに対してクエリー（SQL）を実行します。
- クエリー（SQL）へETTTの変数バインドが可能です。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「ExecuteRdbQuery」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  rdbConnectConfigRef : RDBに対する接続先定義の参照ID # (1)
  value : クエリー（SQL）のパス（相対パス）

```

1. RDBへの接続先の設定を行っている &#96;Configuration&#96; の参照IDを指定します。

## Configuration List

本カスタム機能が提供する設定定義の一覧及び詳細。

|Name|Summary|
|:---|:---|
|[RdbConnectionConfiguration](#RdbConnectionConfiguration)|RDB接続先設定です。  |

------

### RdbConnectionConfiguration
RDB接続先設定です。
#### Description
- RDBのJDBC接続を行う際に必要な情報を定義します。

#### Structure
```yaml
commands : 
  configuration : 「RdbConnectionConfiguration」固定
  id : 設定のID
  summary : 設定の概要（任意）
  description : 設定の詳細（任意）
  driverClassName : RDB接続用のJDBCドライバーのクラス名（FQCN）
  url : RDB接続用のJDBCのURL
  username : RDB接続用のユーザー
  password : RDB接続用のパスワード
  schema : RDBの接続スキーマ
  rdbKind : 「RDB」固定

```


## Message List

本カスタム機能が出力するメッセージの一覧及び内容。

|MessageID|MessageContents|
|:---|:---|
|com.epion_t3.basic.err.9008|対象のファイルの読み込みに失敗しました.パス：{0}|
|com.epion_t3.basic.err.9009|本コマンドはjava.util.Dateを扱うためのコマンドです.変数に格納されているものは型が異なります.|
|com.epion_t3.basic.err.9010|フォーマット後の格納先変数の指定は必須です.|
|com.epion_t3.basic.inf.0001|指定パターンに合致する文字列が含まれています.指定パターン:{0}|
|com.epion_t3.basic.err.9011|日付演算後の格納先変数の指定は必須です.|
|com.epion_t3.basic.err.9001|参照する変数のスコープが不正です.スコープ:{0}|
|com.epion_t3.basic.err.9002|指定パターンに合致する文字列が含まれていません.指定パターン:{0}|
|com.epion_t3.basic.err.9003|値（value）は必須です.|
|com.epion_t3.basic.err.9004|値（value）は数値で指定してください.|
|com.epion_t3.basic.err.9005|対象（target）は必須です.|
|com.epion_t3.basic.err.9006|ユーザー入力にてエラーが発生しました.|
|com.epion_t3.basic.err.9007|対象のファイルが見つかりません.パス：{0}|
