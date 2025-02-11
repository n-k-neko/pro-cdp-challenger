# ロギング戦略
## 目的
**サービスが安定稼働していること**、**サービスの利用状況**を確認する。 

## ロギングのレベル
各ログレベル（DEBUG、INFO、WARN、ERROR）の使用基準を定義する。  
- **DEBUG**：本番環境では使用しない  
- **INFO**：サービスのエンドポイントへのリクエスト  
- **WARN**：サービスの安定稼働には影響ないが、サービス提供者に通知が必要な事象
    - 例：一日のユーザー登録数上限に到達  
- **ERROR**：サービス提供に重大な影響を与えるエラー
    - 例：各種ランタイムエラー
    - 例：想定外のドメインルール逸脱。値オブジェクトのコンストラクタ内におけるガード節での異常値検知（入力チェックで弾けていない想定外の事態）など。

## ロギングの内容
- ログメッセージには、タイムスタンプ、ユーザーIDを含める。パスワードや個人情報は含めない。
- エラーの場合は、エラーの詳細情報も記録する。詳細は[エラーハンドリングガイドライン](TODO:作成)を参照。

## ログ運用
詳細は、[システムアーキテクチャ](TODO：作成する)に記載するが、以下の運用を実施する。
- **監視とアラート**: **AWS CloudWatch Logs**を使用してログを監視し、異常が検知された場合には**SNS**を利用してアラートを発出する。
- **保存**: ログは**S3**に日次で保存する。

## 設計
### ロギングフレームワークの選定
本プロジェクトでは、ロギングのAPIとして**SLF4J (Simple Logging Facade for Java)**を使用し、具体的なロギングの実装として**Logback**を採用する。

#### SLF4Jの役割
- **統一されたロギングAPI**: SLF4Jは、ロギングのための統一されたAPIを提供します。これにより、アプリケーションコードは特定のロギング実装に依存せずにロギングを行うことができる。
- **柔軟性**: SLF4Jを使用することで、ロギングの実装をLogbackから他の実装（例：Log4j、java.util.logging）に変更する際も、アプリケーションコードを変更する必要がない。

#### Logbackの役割
- **高性能なロギング実装**: Logbackは、SLF4Jのための高性能なロギング実装である。設定が柔軟で、ファイルローテーションやログのフィルタリングなどの機能を提供する。
- **設定の柔軟性**: LogbackはXMLやGroovyで設定を行うことができ、ログの出力先やフォーマットを簡単に変更できる。
- **導入方法**: Logbackの設定ファイルを`src/main/resources`ディレクトリに用意し、プロジェクトにLogbackの依存関係を追加することで、SLF4Jは自動的にLogbackを使用するようになる。
    - **依存関係の追加例**:
        - **Mavenの場合**:
            ```xml
            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-classic</artifactId>
                <version>1.2.11</version>
            </dependency>
             ```

### ロギングの設定例
以下は、Logbackの設定ファイルの例となる。`src/main/resources/logback.xml`に配置する。

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

### ロギングの使用例
各クラスでSLF4Jの`Logger`を使用してログを記録する。

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SomeService {
    // ログメッセージに出力クラスを表示するために引数に自身のクラスを指定
    private static final Logger logger = LoggerFactory.getLogger(SomeService.class);

    public void someMethod() {
        // ログレベルがINFO以下の場合はログファイルおよびコンソールに出力
        logger.info("This is an info message");
        // ログレベルがDEBUG以下の場合はログファイルおよびコンソールに出力
        logger.debug("This is a debug message");
        try {
            // Some code that might throw an exception
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
    }
}
```
### 実装方針
通常のログ設計では、logback.xmlで設定を行い、アプリケーションコードではSLF4JのLoggerを使用してログを記録するだけで十分。つまり、infrastructure層に特別なコードを追加する必要はない。
ただし、今後以下のような要件が出てきた場合は`infrastructure`での実装を検討する。
- カスタムアペンダー:特定の要件に応じて、Logbackの標準アペンダーでは対応できない場合
- カスタムフィルター:ログメッセージをフィルタリングするため
- ログの初期化コード:特定の初期化処理が必要な場合、アプリケーションの起動時にログの設定をプログラムで変更