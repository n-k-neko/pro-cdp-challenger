# テスト戦略
## 方針
- **時間がないからこそテストを書く**
  - テストを書くことで、後々のバグ修正や機能追加の際に時間を節約できる
  - 後からテストコードを書く方が、テスト対象とほぼ同時にテストコードを書くよりも遥かに難しい
  - コードを書いたらすぐにテストも書くことで、品質を保ちながら開発を進める。

## 具体的な記述
- テストメソッド名は日本語にしない
  - CIツールとの連携を意識し、英語で記述する。
- 単体、結合、システム試験の割合はピラミッド型になるようにする。
- 複合性能試験も最終的にはやってみたい。
- 最終的にはE2Eまでを目指したい。