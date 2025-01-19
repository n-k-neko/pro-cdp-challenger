# ProCDP Challenger

## 目次
- [ProCDP Challenger](#procdp-challenger)
  - [目次](#目次)
  - [ProCDP Challenger について](#procdp-challenger-について)
  - [リリーススケジュール](#リリーススケジュール)
  - [開発方針](#開発方針)
  - [試しの疎通](#試しの疎通)
    - [ユーザー登録](#ユーザー登録)
    - [JWT発行](#jwt発行)

## ProCDP Challenger について
資格試験学習のモチベーション維持をサポートするためのWebアプリケーションです。  
主なサービスは以下です。

- **受験予定の資格試験を登録**: 試験日や目標学習時間を管理。
- **学習時間の記録**: 資格試験に向けた日々の学習時間を記録し、進捗状況を可視化。
- **友だち機能**：資格取得、学習状況の共有。  

本サービスはプロジェクトオーナーのWebアプリケーション開発の学習も兼ねており、*技術検証駆動開発*となっている節があります。そのため、使いづらい制約や必要性の低い機能もあります。
- 例：試験状況変更時の遷移先制約

## リリーススケジュール
2週間を1スプリントとして開発を行っています。   
GitHub Projectで管理を行っていますので参照ください。

## 開発方針
設計や実装などの方針をまとめています。
- [バックエンド開発方針](./documents/バックエンド開発方針.md)

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