# RDB カスタム機能ドキュメント

このドキュメントは、RDB のカスタム機能が提供する、
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
|[StoreRdbQueryResult](#StoreRdbQueryResult)|RDBに対してクエリー（SQL）を実行し、結果を変数として保持します。  |||
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

### StoreRdbQueryResult
RDBに対してクエリー（SQL）を実行し、結果を変数として保持します。
#### Command Type
- Assert : No
- Evidence : No

#### Functions
- RDBに対してクエリー（SQL）を実行し、結果を変数として保持します。
- クエリー（SQL）へETTTの変数バインドが可能です。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「StoreRdbQueryResult」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  rdbConnectConfigRef : RDBに対する接続先定義の参照ID # (1)
  value : クエリー（SQL）を指定します。
  target : 変数名を指定

```

1. RDBへの接続先の設定を行っている &#96;Configuration&#96; の参照IDを指定します。
1. 変数名は「スコープ.変数名」の形式で指定します。「global.hoge」であればグローバルスコープにhogeという変数名で値を定義することになります。
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
  rdbKind : 「oracle」「mysql」「postgresql」「snowflake」のいずれかを指定します。
  dbName : rdbKindが「snowflake」の場合に必須となる。Snowflakeのデータベース名を指定します。

```


## Message List

本カスタム機能が出力するメッセージの一覧及び内容。

|MessageID|MessageContents|
|:---|:---|
|com.zomu.t.epion.t3.rdb.err.0010|DataSetのインポートに失敗しました.|
|com.zomu.t.epion.t3.rdb.err.0011|RDBアクセスに失敗したため、DataSetのエクスポートに失敗しました.|
|com.zomu.t.epion.t3.rdb.err.0020|指定できるQueryは1つです。複数のクエリーを指定しないでください。|
|com.zomu.t.epion.t3.rdb.err.0003|Scriptのパスが指定されていません.|
|com.zomu.t.epion.t3.rdb.err.0014|RDBへの接続先定義のRDB種別が不正です.対応するRDBの値を設定してください.RDB種別：{0}|
|com.zomu.t.epion.t3.rdb.err.0004|Scriptのパスが存在しません.パス：{0}|
|com.zomu.t.epion.t3.rdb.err.0015|RDBへのコネクションの確率に失敗しました.|
|com.zomu.t.epion.t3.rdb.err.0001|Queryが指定されていません.|
|com.zomu.t.epion.t3.rdb.err.0012|RDBへの接続先定義は必須です.|
|com.zomu.t.epion.t3.rdb.err.0002|Queryの実行時にエラーが発生しました.|
|com.zomu.t.epion.t3.rdb.err.0013|RDBへの接続先定義のRDB種別は必須です.|
|com.zomu.t.epion.t3.rdb.err.0007|DataSetの種別が解決できません.種別：{0}|
|com.zomu.t.epion.t3.rdb.err.0018|結果値を参照するためのFlowIDが指定されていません.|
|com.zomu.t.epion.t3.rdb.err.0008|CSVによるDataSetには現状対応していません.|
|com.zomu.t.epion.t3.rdb.err.0019|カラムの型が解決できません.テーブル：{0},カラム：{1}|
|com.zomu.t.epion.t3.rdb.err.0005|DataSetのパスが指定されていません.|
|com.zomu.t.epion.t3.rdb.err.0016|DataSetの読み込みに失敗しました.パス：{0}|
|com.zomu.t.epion.t3.rdb.err.0006|DataSetのパスが存在しません.パス：{0}|
|com.zomu.t.epion.t3.rdb.err.0017|期待値のDataSetのパスが指定されていません.|
|com.zomu.t.epion.t3.rdb.err.0009|インポート用のオペレーションではありません.オペレーション：{0}|
