# ProCDP Challenger

## 目次
- [ProCDP Challenger](#procdp-challenger)
  - [目次](#目次)
  - [ProCDP Challenger について](#procdp-challenger-について)
  - [開発](#開発)
    - [スケジュールおよびタスク管理](#スケジュールおよびタスク管理)
    - [ブランチ戦略](#ブランチ戦略)
    - [開発方針](#開発方針)
  - [試しの疎通](#試しの疎通)
    - [ユーザー登録](#ユーザー登録)
    - [JWT発行](#jwt発行)
    - [ISS（国際宇宙ステーション）情報取得](#iss国際宇宙ステーション情報取得)

## ProCDP Challenger について
資格試験学習のモチベーション維持をサポートするためのWebアプリケーションです。  
主なサービスは以下です。

- **受験予定の資格試験を登録**: 試験日や目標学習時間を管理。
- **学習時間の記録**: 資格試験に向けた日々の学習時間を記録し、進捗状況を可視化。
- **友だち機能**：資格取得、学習状況の共有。
- **雑多機能**：プロジェクトオーナーが技術学習のために無理やり捻じ込んだ機能群を実現。
     - 国際宇宙ステーションの現在位置情報座標をWebAPIで取得して応答：WebClientとリトライ処理とサーキットブレーカー処理を実装してみたかったため。
     - 今後増えていく予定・・・。

本サービスは**モダンなWebアプリケーション開発の理解および実践**が主目的（※）となっており、正直な話をすると**技術検証駆動開発**になっています。そのため、使いづらい制約や必要性の低い機能も多々あります。  
※資格試験学習のモチベーション維持をサポートする機能は、プロジェクトオーナーが大学受験生時代に非常にお世話になった**Study Plus**に備わっています。

## 開発
### スケジュールおよびタスク管理
1スプリントを2週間でアジャイル開発。  
[GitHub Projects](https://github.com/users/n-k-neko/projects/1)で管理する。

### ブランチ戦略
スプリント2以降で、**GitHub Flow**を採用。方針は以下とする。
- 単一レベルでのブランチ命名とする
  -  タスクごとにブランチを切り分け、プロジェクトバックログとスプリントバックログを分離しない
- ブランチ名は、**<区分>/<issue番号>-<簡単な説明>** とする
  - 例：`feature/12-login-api`   
- プロダクトバックログの粒度が大きい場合、スプリントバックログでさらに小さなタスクに分割する
  - 分割の単位は、**タスクがユーザーストーリーの一部として独立して達成可能かどうか**を基準とする
  - つまり、技術的な関心事での分離とはしない

### 開発方針
#### 概要
フロントエンドは**React**または**Flutter**などで実装したく、**WebAPI**のみを提供する。
#### ドキュメント
[開発ドキュメント目次](/documents/development/TOC.md)

## 試しの疎通
開発環境での疎通手順。
### ユーザー登録
```bash
curl -X POST http://localhost:8080/api/users \
     -H "Content-Type: application/json" \
     -d '{"userId": "abcd3456", "password": "@1234aBcd", "emailAddress": "testtest1234@example.com"}'
```
### JWT発行
```bash
curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"userId": "abcd3456", "password": "@1234aBcd"}'
```
### ISS（国際宇宙ステーション）情報取得
```bash
curl -X GET http://localhost:8080/api/information/iss \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer {JWT}"
```