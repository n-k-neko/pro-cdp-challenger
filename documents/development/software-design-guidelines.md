# ソフトウェア設計ガイドライン
本資料は、[ソフトウェアアーキテクチャ](software-architecture.md)の内容を設計に落とし込む際のガイドラインである。  
あくまでガイドラインであるため、絶対に守るべきルールではなく**推奨事項**ではあるが、原則的には本ガイドラインに基づいた設計とする。

## 参考文献
- [『現場で役立つシステム設計の原則 ~変更を楽で安全にするオブジェクト指向の実践技法』](https://gihyo.jp/book/2017/978-4-7741-9087-7)
- [『良いコード/悪いコードで学ぶ設計入門 ―保守しやすい 成長し続けるコードの書き方』](https://gihyo.jp/book/2022/978-4-297-12783-1)
- [『[入門]ドメイン駆動設計』](https://gihyo.jp/book/2024/978-4-297-14317-6)

## アーキテクチャスタイル：ヘキサゴナルアーキテクチャ
概要については[ソフトウェアアーキテクチャ](./software-architecture.md)に記載しているため、割愛。
### 基本指針
- アプリケーションを構成するレイヤー構造（内側から**ドメイン層->アプリケーション層->アダプター層->インフラストラクチャ層**）を意識し、内側が外側に依存しないようにする
    - 外側を利用する場合はインタフェースを活用することで依存性を逆転させてから利用する
### パッケージ構成
構成例は以下。
```plaintext
src
├── shared
│   ├── exception
│   │   ├── Base.java
│   │   └── ErrorCodes.java
├── adapter
│   ├── inbound
│   │   ├── controller
│   │   ├── dto
│   │   ├── security
│   │   │   └── SecurityConfig.java
│   │   ├── handler
│   │       └── GlobalExceptionHandler.java
│   └── outbound
│       ├── repository
│       │   └── UserRegistraitonRepositoryImpl.java
│       ├── webclient
│       │   └── WebClientAdapter.java
├── application
│   ├── port
│   │   ├── inbound
│   │   │   └── UserRegistraitonUseCase.java
│   │   └── outbound
│   │       ├── UserRegistraitonRepository.java
│   │       └── WebClientPort.java
│   ├── service
│   ├── rule
│   ├── policy
│   └── exception
├── domain
│   ├── entity
│   ├── valueobject
│   ├── service
│   ├── rule
│   ├── policy
│   └── exception
└── infrastructure
    ├── exception
    ├── security
    └── webclient
```
### パッケージ詳細
#### shared層
- **横断的な関心事**を格納する。
  - 例：基底例外クラス

#### adapter層
- **inbound**: 外部からの入力を受け取り、受信ポートを呼び出す。
  - **例**: HTTPリクエストを処理する`Controller`。
  - **例**: Securityルールを定義する`SecurityConfig`。ただし、技術要素は**infrastructure層**に配置。
- **outbound**: 送信ポートを実装し、能動的な外部通信を行う。
  - **例**: データベース操作を行う`Repository`実装（例：`UserRegistrationRepositoryImpl`）。
  - **例**: 外部サービスのWebAPIを発行する**Webクライアント**実装（例：`WebClientAdapter`）。

#### application層
- **port**: アプリケーションと外部を抽象化するインタフェースを提供。
  - **inbound**: ユースケースを定義する受信ポート（例：`UserRegistraitonUseCase`）。
  - **outbound**: 永続化や外部サービス連携を抽象化する送信ポート（例：`UserRegistrationRepository`）。
- **service**: ユースケースを実装するサービスクラスを格納（例：`UserRegistrationService`）。
- **rule**: 外部制約に基づく業務ルールを表現（例：ユーザーID重複不可）。
- **policy**: 業務ルールの集合。

#### domain層
- **entity**: ドメインオブジェクトを格納（例：`UserForRegistration`）。
- **valueobject**: 値オブジェクトを格納（例：`UserId`）。
- **service**: ドメインロジックを提供（例：料金計算サービス）。
- **rule**: ドメインに特化したルールを表現（例：商品割引ルール）。
- **policy**: ドメインルールの集合。

#### infrastructure層
- **技術的な関心事を格納する。**
  - **例**: トークン操作など、Securityの技術的部分
  - **例**: WebClientのBean定義
---

## 設計パターン：DDD
### クラス・レコード・オブジェクト
- 可読性および保守性の観点から、オブジェクトは原則イミュータブルとする。そのため、**Setter**は禁止。
  - オブジェクトの値を変更する場合は新たにオブジェクトを生成して返す。
    ```java
    public SomeObj add(SomeObj obj){
        return new SomeObj(this.value() + obj.value());
    } 
    ```
  - メモリ使用率, CPU負荷, GC頻度は上昇するが、昨今のハードおよびソフトの性能向上を鑑みて問題ないと判断。ただし、リソース状況は定期的に確認し、必要に応じてスケールアウトする。
- オブジェクトのイミュータブルをJavaの機能として実現するために、`Java SE 16`から正式導入された**レコード**を積極的に用いる。  
    - ただし以下の場合はクラスとする。
      - DIコンテナに登録されたBeanをフィールドに有する場合
      - 継承またはインタフェースの実装を行う場合
      - 不変にできないフィールドを有する場合
- ドメイン中で用いられる広義の**値**は、**クラスまたはレコード**で表現し、**値オブジェクト**として扱う。
  - 例：ユーザーID, メールアドレス, 金額, カード番号
  - 値オブジェクトを採用する利点の一例
    - 定義上不適切な値の混入を防止（コンストラクタによるガード節）
    - コードが分かりやすく安全（引数に値オブジェクト型を指定することで誤混入防止など）
    - 値の定義自体を値オブジェクトに閉じ込めやすくなる（`public static final int MAX_LENGTH = 100;`など）
    - 業務ロジックを値オブジェクトに閉じ込めやすくなる（加算など） 
- 値オブジェクトやドメインエンティティは、データの入れ物として用いるのではなく、機能を具備する。
- ドメイン領域においては継承は原則使用しない。インタフェースやフィールドオブジェクトで代替する。
  - 可読性が低下するため。
  - ビジネスにおいて継承関係は変化しやすい。継承関係をコードに持ち込むとビジネスの変化に伴って大規模なリファクタリングが必要になる可能性がありうる。
  - ただし、インフラストラクチャ層またはアダプター層において、フレームワークやライブラリを利用する場合は、継承を許容する。
- ドメインエンティティおよび値オブジェクトをライブラリやフレームワークに非依存の設計とする。
  - `Lombok`は例外として利用可とする。
  - `O/Rマッパー`を使用したい場合は専用のクラスを用意する。
- ガード節のエラーは、`IllegalArgumentException`とするのではなく、ドメイン固有のエラーとする。異常値がコンストラクタに渡される事態は入力チェックや事前処理によって未然に防いでいる想定だが、引数として渡されることは当該ドメインの設計に綻びがありうる。
---
### ルールとポリシー

#### 基本概念
- **ルール**: 制約を表現するクラス。
- **ポリシー**: 複数のルールを組み合わせて、特定の目的に基づいた判断や検証を行うクラス。

#### 設計指針
1. **ポリシーはルールの集合とする**:
   - ポリシークラスは、1つ以上のルールクラスをフィールドとして持ち、全体の検証ロジックを管理する。
   - 各ルールクラスは、独立した単位で単一責任の原則（SRP）を守る。
   - ルールクラスは、検証に失敗した場合に例外をスローする責務を持つ。これにより、異常系の処理をルール内部に閉じ込めることができる。
   - 一方で、複数のルールを複雑に組み合わせる（例: OR / NOT 条件）場合には、戻り値が`boolean`型のルールを活用する設計も検討する。
        - 例えば、ルールクラスで例外をスローする上記の設計では、少なくとも1つのルールを満たせば良い場合でも1つのルールで例外がスローされた時点で処理が終了してしまうため。
        - `boolean`型のルールを利用することで、ポリシークラス内でルールの組み合わせを柔軟に実現できる。
            - **例**: OR条件をポリシークラスで実現する場合。
                ```java
                public boolean anySatisfiedBy(T target) {
                    return rules.stream().anyMatch(rule -> rule.isSatisfiedBy(target));
                }
                ```

2. **ユースケース内でのチェックロジックの簡素化**:
   - ユースケース実装クラスで条件分岐を大量に用いるのではなく、各ルールをクラス化し、ポリシーでそれらの検証を統合する。
   - ポリシーを利用することで、読みやすく拡張性の高いコードを実現する。
     - ルール追加の際は新規ルールクラスの作成だけで追加を実現可能。既存コードは影響を受けない。
   - ポリシーは、ルールの`validate`メソッドを順次実行し、ルール内でスローされた例外をそのまま上位に伝播させる。

3. **ルールとポリシーの分離**:
   - ルールクラスは、個別の検証ロジックを担当し、ポリシークラスはそれらの組み合わせと実行の調整を行う。
   - ポリシーにおいてルールの組み合わせ（AND/OR/NOT）を柔軟に扱えるように設計する。

#### 層ごとの適用
- **ドメイン層**:
  - 純粋なドメインモデル固有の制約をチェックする。
  - 例: 値オブジェクトやエンティティの状態に基づく検証。
  - ただし、単独で確認可能なチェック（不適切な値の混入など）については、値オブジェクトのコンストラクタによってチェックする。
- **アプリケーション層**:
  - 外部システムやユーザーインターフェースとのやり取りによる制約をチェックする。
  - 例: リクエストデータの整合性確認、IDの一意性、外部サービスとの依存関係に基づく条件。

#### 実装例
#### ルールインターフェース
```java
public interface Rule<T> {
    // AND条件ではvoid型（異常系は例外をスロー）、OR条件であればboolean型
    void validate(T target); 
}
```
#### サンプルルール（ドメイン層）
```java
public class DateRangeRule implements Rule<SomeDomainObject> {
    @Override
    public void validate(SomeDomainObject target) {
        if (target.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date is out of range");
        }
    }
}
```
#### ポリシークラス
```java
public class Policy<T> {
    private final List<Rule<T>> rules;

    public Policy(List<Rule<T>> rules) {
        this.rules = rules;
    }

    public void validate(T target) {
        for (Rule<T> rule : rules) {
            rule.validate(target); // ルール内で例外をスロー
        }
    }
}
```
#### ユースケースの利用例（アプリケーション層）
```java
public class SomeService implments SomeUseCase {
    private final Policy<SomeDomainObject> policy;

    public SomeService(Policy<SomeDomainObject> policy) {
        this.policy = policy;
    }

    public void execute(SomeDomainObject object) {
        policy.validate(object); // 異常系の場合は例外をスローして処理終了
        
        // 検証に成功した場合の処理
    }
}
```
### 推奨
1. ルールを小さな単位に分割して再利用性を高める。
2. ルール同士を組み合わせるロジックをポリシークラスに委譲する。
3. ルールやポリシーのテストを個別に作成し、ユニットテストを通じて品質を保証する。

### 注意点
- ポリシーに含めるルールの粒度が大きくなりすぎないよう注意する。
- 各層で必要な検証ロジックを適切に分離し、責務の分散を図る。