package org.javafxgoogleapi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.google.inject.Inject;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JavaFXGoogleApiMainController {

    @FXML
    private Button fxButton;

    @FXML
    private ListView<String> fxListView;

    private final Stage primaryStage;
    private final GoogleApiService googleApiService;
    private final ExecutorService executorService;
    private final Stage oauthConsentStage = new Stage();

    @Inject
    JavaFXGoogleApiMainController(
            Stage primaryStage,
            GoogleApiService googleApiService,
            ExecutorService executorService) {
        this.primaryStage = primaryStage;
        this.googleApiService = googleApiService;
        this.executorService = executorService;
    }

    /*
     * コントローラーの初期設定
     */
    public void initialize() throws IOException {
        /*
         * OAuth同意画面ウィンドウのロードと設定
         */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OAuthConsent.fxml"));
        VBox vbox = loader.load();
        oauthConsentStage.initModality(Modality.APPLICATION_MODAL);
        oauthConsentStage.initOwner(primaryStage);
        oauthConsentStage.setTitle("Google認証ウィンドウ");
        oauthConsentStage.setScene(new Scene(vbox));

        /*
         * OAuth同意画面ウィンドウのコントローラークラスから取得したWebViewをGoogleApiServiceに設定
         */
        OAuthConsentController oAuthConsentController = loader.getController();
        googleApiService.setWebView(oAuthConsentController.getFxWebView());

        /*
         * Google Drive APIボタンのアクションを設定
         */
        fxButton.setOnAction(event -> {
            /*
             * タスクでGoogle Drive APIを実行する
             */
            Task<List<String>> task = googleApiService.getFileListTask();
            if (task == null) {
                /*
                 * nullの場合Google APIの認証がキャンセルされたので何もしない
                 */
                return;
            }
            task.setOnSucceeded(e -> {
                /*
                 * タスクが成功した場合、ListViewに結果を設定する
                 */
                List<String> list = task.getValue();
                fxListView.getItems().setAll(list);
            });
            task.setOnFailed(e -> {
                /*
                 * タスクが失敗した場合、何らかの処理を行う
                 */
            });

            /*
             * タスクの実行
             */
            executorService.submit(task);
        });
    }

}
