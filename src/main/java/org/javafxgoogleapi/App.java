package org.javafxgoogleapi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
         * GuiceのInjectorの生成
         */
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                /*
                 * DIの設定
                 */
                bind(Stage.class).toInstance(primaryStage);
                bind(GoogleApiService.class).to(GoogleApiServiceImpl.class);
                bind(ExecutorService.class).toInstance(executorService);
            }
        });

        FXMLLoader loader = new FXMLLoader(
                this.getClass().getResource("JavaFXGoogleApiMain.fxml"), null, null,
                injector::getInstance); // コントローラーファクトリーの指定
        Parent root = loader.load();

        primaryStage.setTitle("JavaFX and Guice");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // アプリケーション終了時にExecutorServiceをシャットダウン
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        launch(args);
    }

}