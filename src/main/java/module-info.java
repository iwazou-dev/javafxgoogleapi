module javafxgoogleapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.web;

    // DI対応
    requires com.google.guice;
    requires jakarta.inject;

    // Google API
    requires com.google.api.client;
    requires com.google.api.client.auth;
    requires com.google.api.services.drive;
    requires google.api.client;
    requires com.google.api.client.http.apache.v2;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.json.gson;
    requires com.google.gson;
    requires jdk.httpserver;
    requires jdk.crypto.ec; // jlinkしたカスタムJREでSSL/TLS接続時の暗号スイートが不足しないように指定する

    opens org.javafxgoogleapi to
            javafx.fxml,
            com.google.guice,
            com.google.auth.oauth2;

    exports org.javafxgoogleapi;
}
