package org.javafxgoogleapi;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class OAuthConsentController {

    @FXML
    private WebView fxWebView;

    @FXML
    private Button fxCloseButton;

    public void initialize() {
        /*
         * 閉じるボタンのアクションを設定
         */
        fxCloseButton.setOnAction(event -> {
            /*
             * ウィンドウを閉じる
             */
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    /*
     * WebViewのgetter
     */
    WebView getFxWebView() {
        return this.fxWebView;
    }

}
