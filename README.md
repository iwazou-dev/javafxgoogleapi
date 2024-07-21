# javafxgoogleapi

JavaFX の WebView を使用した Google API の OAuth 同意画面の実装サンプルです。

## 動作環境

- Java 21.0.3
- JavaFX 21.0.3
- Gradle 8.8

## 実行手順

1. [Google Drive API のクイックスタート](https://developers.google.com/drive/api/quickstart/java)の「環境を設定する」に従って認証情報をダウンロードする。
2. 認証情報を`credentials.json`にリネームして`src/main/resources`に格納する。
3. `./gradle run`を実行する。
