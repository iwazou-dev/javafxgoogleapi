package org.javafxgoogleapi;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import javafx.scene.web.WebView;
import javafx.stage.Stage;

/*
 * カスタムブラウザクラス
 */
class CustomBrowser implements AuthorizationCodeInstalledApp.Browser {

    private final WebView webView;
    private final Stage stage;

    CustomBrowser(WebView webView, LocalServerReceiver localServerReceiver) {
        this.webView = webView;
        this.stage = (Stage) webView.getScene().getWindow();
        this.stage.setOnHidden(event -> {
            /*
             * ウィンドウが閉じられた場合にLocalServerReceiverをstopする
             */
            try {
                localServerReceiver.stop();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    @Override
    public void browse(String url) throws IOException {
        /*
         * WebViewでブラウズする
         */
        webView.getEngine().load(url);
        stage.showAndWait();
    }

    boolean isShowing() {
        /*
         * ウィンドウを表示しているか否かを返す
         */
        return stage.isShowing();
    }
}