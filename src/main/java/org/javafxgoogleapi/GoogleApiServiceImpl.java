package org.javafxgoogleapi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import javafx.concurrent.Task;
import javafx.scene.web.WebView;

class GoogleApiServiceImpl implements GoogleApiService {

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private final JsonFactory jsonFactory;
    private final NetHttpTransport netHttpTransport;

    private WebView webView;

    GoogleApiServiceImpl() throws GeneralSecurityException, IOException {
        jsonFactory = GsonFactory.getDefaultInstance();
        netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    /*
     * WebViewのsetter
     */
    @Override
    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    private Credential getCredential() throws IOException {
        List<String> scopes = List.of(DriveScopes.DRIVE_METADATA_READONLY);

        // Load client secrets.
        GoogleClientSecrets clientSecrets;
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                getClass().getResourceAsStream("/credentials.json"))) {
            clientSecrets = GoogleClientSecrets.load(jsonFactory, inputStreamReader);
        }

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder()
                .setPort(-1) // 未使用のポートを使う
                .setLandingPages(
                        getClass().getResource("/success.html").toString(), // OAuth 2.0認証成功時の表示ページ
                        getClass().getResource("/error.html").toString()) // OAuth 2.0認証失敗時の表示ページ
                .build();
        /*
         * カスタムブラウザの生成
         */
        CustomBrowser customBrowser = new CustomBrowser(webView, localServerReceiver);
        AuthorizationCodeInstalledApp authorizationCodeInstalledApp = new AuthorizationCodeInstalledApp(
                googleAuthorizationCodeFlow, localServerReceiver,
                customBrowser); // カスタムブラウザの指定
        Credential credential;
        try {
            credential = authorizationCodeInstalledApp.authorize("user");
        } catch (NullPointerException e) {
            if (!customBrowser.isShowing()) {
                /*
                 * カスタムブラウザが閉じられた場合の処理
                 */
                return null;
            }
            throw e;
        }
        return credential;
    }

    private List<String> getFileList(Credential credential) throws IOException {
        Drive service = new Drive.Builder(netHttpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();

        List<String> list = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            // ファイルあり
            files.forEach(file -> list.add(file.getName()));
        }
        return list;
    }

    @Override
    public Task<List<String>> getFileListTask() {
        try {
            /*
             * Credentialの取得
             */
            Credential credential = getCredential();
            if (credential == null) {
                /*
                 * ウィンドウを閉じられてcredentialが取得できなかった場合nullを返す
                 */
                return null;
            }

            return new Task<List<String>>() {

                @Override
                protected List<String> call() throws Exception {
                    /*
                     * Google Drive APIの実行
                     */
                    return getFileList(credential);
                }

            };
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
