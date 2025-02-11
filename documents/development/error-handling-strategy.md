# エラーハンドリング戦略
## 方針
1. **異常系の例外スローとハンドリング**:
   - 業務ロジックやシステムエラーなどの異常系は例外をスローしてハンドリングする。

2. **基底例外クラス `BaseException` の設置**:
   - アプリケーション全体で共通して使用できる基底例外クラスを設ける。
   - 基底例外クラスには、エラー区分（`errorCategory`）、エラーコード (`errorCode`) を保持するフィールドを用意し、エラーの識別と管理を一元化する。

3. **層ごとの例外クラスの継承設計**:
   - ヘキサゴナルアーキテクチャの各層（`domain`, `application`, `adapter`, `infrastructure`）で基底例外を継承し、独自の例外クラスを設ける。
   - 層ごとに異なるエラーメッセージやハンドリングを可能にする。

4. **例外ハンドリングの集中管理**:
   - Spring Bootの`@RestControllerAdvice`を用いてグローバル例外ハンドラーを実装し、各層の例外に応じた適切なレスポンスを返す。

5. **ロギング**：
    - エラー区分が`WARN`および`ERROR`の例外については、ログに記録する。詳細は、[ロギング戦略](logging-strategy.md)を参照する。

## 実装例

### 1. 基底例外クラス: `BaseException`
基底例外クラスはエラーコードを保持し、共通の例外処理を提供する。

```java
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final ErrorCategory errorCategory;

    protected BaseException(String message, String errorCode, ErrorCategory errorCategory) {
        super(message);
        this.errorCode = errorCode;
        this.errorCategory = errorCategory;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }
}
```
### 2. エラー区分クラス：`ErrorCategory`
エラー区分によってロギングの必要性の有無の判定を実施するため、規定された値以外が入力されないようにする。
```java
public enum ErrorCategory {
    INFO,
    WARN,
    ERROR
}
```

### 3. 層ごとの例外クラス

#### `domain`層例外
**ドメイン層**では、ビジネスルールやドメインモデルの整合性違反を表現する例外を設ける。`DomainRuleViolationException`など。

#### `application`層例外
**アプリケーション層**では、ユースケースや業務ルール違反を表現する例外を設ける。`BusinessRuleViolationException`など。

### 4. エラーコードの管理
エラーコードは一元管理することで、一貫性と再利用性を確保する。
``` java
public class ErrorCodes {
    // Domain層エラーコード
    public static final String INVALID_USER_ID = "DOM_001";

    // Application層エラーコード
    public static final String DUPLICATE_USER = "APP_001";

    // Infrastructure層エラーコード
    public static final String DATABASE_ERROR = "INFRA_001";
}
```
### 5. グローバル例外ハンドラー
各層の例外をキャッチし、適切なレスポンスを返す。
ハンドラークラスは、`/adapter/inbound/handler`に配置する。  
※ロギングについての記載は省略している。
``` java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(BusinessRuleViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(InfrastructureException ex) {
      // エンドユーザーには一般化されたエラーメッセージを表示
        log.error("Infrastructure error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(ex.getErrorCode(), "An unexpected error occurred."));
    }
}

```
### 6. エラーレスポンスDTO
レスポンスとしてクライアントに返すエラーメッセージを定義する。エラーコード及びメッセージを応答する。
``` java
public record ErrorResponse(String errorCode, String message) {}
```