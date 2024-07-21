package org.javafxgoogleapi;

import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.web.WebView;

interface GoogleApiService {

    public void setWebView(WebView webView);

    public Task<List<String>> getFileListTask();

}
