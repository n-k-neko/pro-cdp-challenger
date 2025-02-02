# Webクライアント
## 方針
- WebAPIサービスを利用する場合には、**WebClient**を利用する。
- 実装レベルでは、メソッドを徹底的に抽象化することで、WebAPIサービスごとに個別メソッドを用意することで生じる様々なリスクを防ぐ。
- 一度リクエストが失敗した場合に即時エラーとするのではなく、複数回リトライをする。
- **サーキットブレーカー**を導入する。つまり、一定期間においてWebAPIサービスの障害を複数回検知した場合には通信を遮断し、一定時間経過後は設定した割合で通信を復旧させ、WebAPIサービスの復旧を検知すると通信を完全復旧する。

## WebClientの選定理由
- デファクトスタンダードであり信頼性が高いライブラリ
    - Webクライアント処理に特殊要件はないため、他ライブラリを採用するメリットがない
- 非ブロッキングI/Oを活用しており、リソース効率が良く、高スループットを実現できる
- 非同期だけではなく、`block()`メソッドによって同期的に使用することも可能であり、拡張性に優れている
- タイムアウトやリトライなどの設定が容易
- 他ライブラリ同様、純粋実装だとライブラリに依存する形になるが、**インタフェース**を用いることで依存性を逆転させることが容易

### 補足 同期と非同期
- **同期**：処理が順番に実行され、1つの処理が完了するまで次の処理が待機（ブロック）する。
  ```java
  String response = webClient.get()
    .uri("https://api.example.com/endpoint")
    .retrieve()
    .bodyToMono(String.class)
    .block(); //  同期的に処理を開始
  ```

- **非同期**：リクエストを送信すると、その結果を待たずに次の処理を実行する。結果は後から通知（コールバックやリアクティブ型）を受けとる。スレッドを待機させないため、大量のリクエスト処理や高スループットのシステムで有効。

  ```java
  webClient.get()
          .uri("https://api.example.com/endpoint")
          .retrieve()
          .bodyToMono(String.class)
          .doOnNext(response -> System.out.println("Received: " + response)) // レスポンス準備後に実行
          .subscribe(); // 非同期的に処理を開始
  System.out.println("Request sent!"); // この行はリクエストを送信後すぐに実行される
  ```
**同期と非同期の選択基準**
- 同期を選択するケース：処理がユーザー応答に直結し、すぐに結果が必要な場合。
- 非同期を選択するケース：処理がエンドユーザー応答に直接関係しない場合（例：通知送信、ログ保存）。

## 実装方針
- ビジネスのコアであるアプリケーション層／ドメイン層が、ライブラリやWebAPIサービスの仕様変更に影響を受ける（=依存する）事態を防ぐため、**インタフェース**を活用して依存性を逆転させる。
    - 実際のコードでは、**ヘキサゴナルアーキテクチャ**の層構造で表現することで関係性を明示する。
        - インタフェース：アプリケーション層。引数は取得したいドメインエンティティクラスのみ。**application/port/outbound/ApiClientPort.java**。
        - 実装および詳細設定クラス：アダプター層。アプリケーション層から渡されたドメインエンティティクラスをもとにWebAPIサービス情報のマッピングも実施する。**adapter/outbound/webclient/**。
- エンドポイントごとにメソッドを設けるのではなく、Javaの**ジェネリクス型推論**を利用することで、メソッドを抽象化し、拡張性と保守性を高める。
    - 抽象化されたメソッドでは対応ができない特殊要件は個別実装する。
  ```java
  // 引数および戻り値の型を抽象化
  public <T, R> R fetchDataSync(Class<R> domainType) {
        /*
         * 引数で渡されてたドメインエンティティクラスをもとに、
         * WebAPIサービスのエンドポイントおよびレスポンスマッピングクラスをアダプター層で取得。
         * アプリケーション層が外部APIサービスの詳細を知らない（=依存していない）状態を実現。
         */
        ResponseMapper<T, R> mapper = findMapper(domainType);
        final String endpoint = endpointMap.get(domainType);

    return webClient.get()
            .uri(endpoint)
            .retrieve()
            .bodyToMono((Class<T>)mapper.getResponseType())
            .map(mapper::mapResponse)
            .block();
  }
  ```
## 実装例
### WebAPIレスポンスDTO
役割：WebAPIのレスポンスをマッピングする  
仮にレスポンス内容がドメインエンティティのフィールドと一致していた場合であっても、以下の理由により必ず作成する。
- WebAPIサービスの仕様が変更となる可能性がある。
- レスポンスをドメインエンティティにマッピングしようとすると、ドメインエンティティのフィールドの型はプリミティブ型ないしString型にせざるを得ない。安全性、凝集性などの観点からドメインエンティティのフィールドの型は値オブジェクトクラスとするべきであり、適切ではない。

#### 実装例   
**ドメインエンティティ**：`position`フィールドのみ
```java
public class IssDomainObject {
    private final String position; // 本番では値オブジェクトを用意する
}
```
**外部APIレスポンスDTO**：`position`に加えて、`message`フィールドも含まれる。`message`フィールドはドメインエンティティには不要。
```java
public class IssResponse {
    private String message;
    private String issPosition;
}
```
---
### Mapper
役割：外部APIレスポンスDTOをドメインエンティティに変換する
#### 実装例 
**レスポンス変換用インタフェース**：Mapperを利用するクラスは、本インターフェースを指定することで、WebAPIサービスごとにメソッドを設けて変換する必要がなくなる。
```java
public interface ResponseMapper<T, R> {
R mapResponse(T response); // レスポンスDTOをドメインエンティティに変換
Class<T> getResponseType(); // 対応するレスポンスDTOの型を取得
Class<R> getDomainType();   // 対応するドメインの型を取得
}
```
**Mapperの実装**：上記インタフェースを実装
```java
@Component
public class IssResponseMapper implements ResponseMapper<IssResponse, IssDomainObject> {
    @Override
    public IssDomainObject mapResponse(IssResponse response) {
        return new IssDomainObject(response.getIssPosition());
    }

    @Override
    public Class<IssResponse> getResponseType() {
        return IssResponse.class; // このマッパーが対応するレスポンスの型
    }

    @Override
    public Class<IssDomainObject> getDomainType() {
        return IssDomainObject.class; // このマッパーが対応するドメインの型
    }
}
```
---
### WebAPIサービスエンドポイント情報
役割：ドメインエンティティを取得するための情報ソースを管理する  
前提：アプリケーション層の関心事は、**サービスを提供するための情報**であって、当該情報のソースは関心事**ではない**
- 例えば、ISS（国際宇宙ステーション）の位置情報をユーザーに提供する機能の場合、位置情報自体はアプリケーション層の関心事だが、情報のソースはアプリケーション層の関心事ではなく、むしろ外部サービスへの依存が生じるため、関心事とすべきではない。

つまり、外部との通信を制御するアダプター層で情報ソースを管理することが、関心の分離的にも構造的にも優れている。
#### 実装例
Javaの`Map`を利用し、ドメインエンティティのクラスをキーとして情報ソースを管理する。  
Bean化してDIコンテナに登録する際には、他に同様のマップを作成することを考慮して、Beanに名前を付与する。
```java
@Bean(name = "externalApiEndpointMap")
public Map<Class<?>, String> externalApiEndpointMap() {
    Map<Class<?>, String> endpointMap = new HashMap<>();

    endpointMap.put(Iss.class, "http://api.open-notify.org/iss-now.json");

    return endpointMap;
}
```
---
### WebClientAdapter
役割：WebAPIサービスとの通信フローを制御する
#### 実装例
```java
private final WebClient webClient; // WebClientライブラリ
private final List<ResponseMapper<?, ?>> mappers; // Mapper
private final Map<Class<?>, String> endpointMap; // エンドポイントマップ

// 引数および戻り値の型を抽象化
public <T, R> R fetchDataSync(Class<R> domainType) {
    /*
        * ドメインエンティティクラスをもとに、
        * WebAPIサービスのエンドポイントおよびレスポンスマッピングクラスをアダプター層で取得。
        * アプリケーション層が外部APIサービスの詳細を知らない（=依存していない）状態を実現。
        */
    ResponseMapper<T, R> mapper = findMapper(domainType);
    final String endpoint = endpointMap.get(domainType);

return webClient.get()
        .uri(endpoint)
        .retrieve()
        // Spring WebFluxのbodyToMono(Class<T>)は、レスポンスボディを指定された型にデシリアライズする
        // マッピング処理は、内部的にJackson（デフォルトのJSONライブラリ）が行う
        .bodyToMono((Class<T>)mapper.getResponseType())
        // レスポンスクラスをドメインエンティティクラスに変換する
        .map(mapper::mapResponse)
        .block();
}

// ドメインエンティティをもとに、適切なMapperを見つける
@SuppressWarnings("unchecked")
private <T, R> ResponseMapper<T, R> findMapper(Class<R> domainType) {
    return (ResponseMapper<T, R>) mappers.stream()
            .filter(mapper -> isValidMapper(mapper, domainType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                    "No mapper found for domainType: " + domainType.getName()));
}

private boolean isValidMapper(ResponseMapper<?, ?> mapper, Class<?> domainType) {
    // マッパーが対応するレスポンス型を検証
    return mapper.getResponseType().equals(domainType);
}
```
---
## リトライ
WebAPIサービス側の都合によって、リクエストが正常に処理されずエラーが応答されることがある。以下は一例。
- リクエストレートリミッター
- サーバーの停止起動タイミング

上記を考慮し、リクエストが失敗した場合は即座にエラーとするのではなく、複数回のリトライを行う方針とする。  
リトライの条件や回数、待機時間については、システムの要件や外部サービスの特性を考慮し、今後詳細を検討する。
### 実現方式
マイクロサービスや分散システムにおける回復力を向上させるためのライブラリである**Resilience4j**を利用する。  
Resilience4jは、以下のような機能を提供する。
- サーキットブレーカ: 連続した失敗を防ぎ、システムの過負荷を回避
- リトライ: 一時的な障害を克服するための再試行
- レートリミッター: リクエスト数を制限し、システムの安定性を保つ

これらの機能を**WebClient**と組み合わせて使用することができる。例えば、**WebClient**のリクエストに対して**Resilience4j**のサーキットブレーカやリトライ機能を適用することで、HTTPリクエストの信頼性を向上させ、システムの安定性を強化することができる。
### 実装例
**WebClientAdapter.java**：リトライ処理を記述
```java
// リトライインスタンスを取得
Retry retry = retryRegistry.retry("IssRetry"); // 本番ではハードコーディングせずにメソッドで設定
// リトライを適用する
return Retry.decorateSupplier(retry, () ->
    // Webクライアント処理
).get();
```
**application.yml**：リトライインスタンスを設定する
```yml
resilience4j:
  retry:
    instances:
      IssRetry:
        maxAttempts: 3
        waitDuration: 500ms
        exponentialBackoffMultiplier: 2.0
        retryExceptions:
          - org.springframework.web.reactive.function.client.WebClientResponseException$TooManyRequests
          - org.springframework.web.reactive.function.client.WebClientResponseException$InternalServerError
          - org.springframework.web.reactive.function.client.WebClientResponseException$BadGateway
          - org.springframework.web.reactive.function.client.WebClientResponseException$ServiceUnavailable
          - org.springframework.web.reactive.function.client.WebClientResponseException$GatewayTimeout
        ignoreExceptions: []
```
- **maxAttempts**：初回の試行を含めたトライの最大試行回数を指定
- **waitDuration**：各リトライの間に待機する時間を指定
- **retryExceptions**：リトライを行う対象の例外を指定
- **ignoreExceptions**：リトライの対象としない例外を指定
- **exponentialBackoffMultiplier**：各リトライごとに待機時間を倍増させるための倍率を指定
    - 2.0を設定した場合：1回目のリトライ 500ms, 2回目のリトライ 1000ms
---  
## サーキットブレーカ
WebAPIサービスが継続的にエラーを応答する場合、上記のリトライはエンドユーザーへの待ち時間を増やすだけになる。  
そのため、サーキットブレーカを導入し、以下のように動作させる。

- **クローズ状態**: 通常の通信が行われる状態。エラー率が一定の閾値を超えるとオープン状態に移行。
- **オープン状態**: 通信を遮断し、一定時間経過後にハーフオープン状態に移行。
- **ハーフオープン状態**: 設定した割合で通信を許可し、WebAPIサービスの復旧を検知するとクローズ状態に戻る。

この仕組みにより、WebAPIサービスの障害を複数回検知した場合には通信を遮断し、システムの安定性を保つことができる。
### 実現方式
リトライ同様、**Resilience4j**を利用する。  
**WebClient**のリクエストに対して**Resilience4j**のサーキットブレーカを適用する。
### 実装例
**WebClientAdapter.java**：サーキットブレーカ処理を記述
```java
CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(IssCircuitBreaker); // 本番ではハードコーディングせずにメソッドで設定
circuitBreaker.executeSupplier(() -> 
    webClient.get()
    .block()
)
```
**application.yml**：サーキットブレーカインスタンスを設定する
```yml
resilience4j:
  circuitbreaker:
    instances:
      IssCircuitBreaker:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        recordExceptions:
          - org.springframework.web.reactive.function.client.WebClientResponseException$TooManyRequests
          - org.springframework.web.reactive.function.client.WebClientResponseException$InternalServerError
          - org.springframework.web.reactive.function.client.WebClientResponseException$BadGateway
          - org.springframework.web.reactive.function.client.WebClientResponseException$ServiceUnavailable
          - org.springframework.web.reactive.function.client.WebClientResponseException$GatewayTimeout
        ignoreExceptions: []
```
- **registerHealthIndicator**：サーキットブレーカの状態をSpring Bootのアクチュエータのヘルスインジケータとして登録する。
- **slidingWindowSize**：エラー率を計算するために使用するスライディングウィンドウのサイズを指定する。例では、直近の10回の呼び出しを基にエラー率を計算する。
- **minimumNumberOfCalls**：サーキットブレーカがエラー率を計算するために必要な最小呼び出し数を指定する。
- **failureRateThreshold**：サーキットブレーカがオープン状態になるためのエラー率の閾値を指定する。例では、エラー率が50%を超えるとサーキットブレーカがオープンになる。
- **waitDurationInOpenState**：ーキットブレーカがオープン状態のままでいる時間を指定する。
- **permittedNumberOfCallsInHalfOpenState**：サーキットブレーカがハーフオープン状態で許可する呼び出しの数を指定する。
- **automaticTransitionFromOpenToHalfOpenEnabled**：オープン状態からハーフオープン状態への自動遷移を有効にする。指定された時間が経過すると自動的にハーフオープン状態に移行する。
- **recordExceptions**：サーキットブレーカの対象とする例外を指定する。
- **ignoreExceptions**：サーキットブレーカのエラー率計算から除外する例外を指定する。
    - 特定の例外を無視することで、サーキットブレーカが不必要にオープン状態になるのを防ぐ。例えば、クライアントのリクエストエラー（400系）など、サーバー側の問題ではない例外を無視することが一般的。