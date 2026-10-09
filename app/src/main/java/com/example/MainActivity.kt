package com.example

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        GeminiVaultApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiVaultApp() {
  val context = LocalContext.current
  var webViewInstance by remember { mutableStateOf<WebView?>(null) }
  var loadingProgress by remember { mutableFloatStateOf(0f) }
  var isLoading by remember { mutableStateOf(true) }
  var showExportDialog by remember { mutableStateOf(false) }
  var canGoBack by remember { mutableStateOf(false) }

  // Handle system back navigation inside WebView
  BackHandler(enabled = canGoBack) {
    webViewInstance?.let { webView ->
      if (webView.canGoBack()) {
        webView.goBack()
      }
    }
  }

  val htmlContent = remember {
    try {
      context.assets.open("index.html").bufferedReader().use { it.readText() }
    } catch (e: Exception) {
      "<!-- Error loading index.html: ${e.message} -->"
    }
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .testTag("gemini_vault_scaffold"),
    topBar = {
      Column(modifier = Modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Code,
                  contentDescription = "Vault Icon",
                  tint = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Gemini Switcher AI",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
              )
            }
          },
          actions = {
            IconButton(
              onClick = { webViewInstance?.reload() },
              modifier = Modifier.testTag("reload_button")
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reload Webview"
              )
            }
            IconButton(
              onClick = { showExportDialog = true },
              modifier = Modifier.testTag("export_button")
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Export index.html"
              )
            }
          },
          colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
          )
        )

        // Webview Loading progress bar
        AnimatedVisibility(visible = isLoading) {
          LinearProgressIndicator(
            progress = { loadingProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(2.dp)
              .testTag("loading_progress_bar"),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      GeminiWebView(
        htmlData = htmlContent,
        modifier = Modifier.fillMaxSize(),
        onWebViewCreated = { webViewInstance = it },
        onProgressChanged = { progress ->
          loadingProgress = progress / 100f
          isLoading = progress < 100
        },
        onNavigationStateChanged = { canBack ->
          canGoBack = canBack
        }
      )
    }

    // Export / Share index.html Dialog
    if (showExportDialog) {
      ExportHtmlDialog(
        context = context,
        onDismiss = { showExportDialog = false }
      )
    }
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GeminiWebView(
  htmlData: String,
  modifier: Modifier = Modifier,
  onWebViewCreated: (WebView) -> Unit,
  onProgressChanged: (Int) -> Unit,
  onNavigationStateChanged: (Boolean) -> Unit
) {
  val context = LocalContext.current

  AndroidView(
    modifier = modifier.testTag("gemini_web_view"),
    factory = { ctx ->
      WebView(ctx).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )

        settings.apply {
          javaScriptEnabled = true
          domStorageEnabled = true
          databaseEnabled = true
          allowFileAccess = false
          allowContentAccess = false
          loadsImagesAutomatically = true
          mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
          useWideViewPort = true
          loadWithOverviewMode = true
          cacheMode = WebSettings.LOAD_DEFAULT
        }

        webChromeClient = object : WebChromeClient() {
          override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            onProgressChanged(newProgress)
          }

          override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
          ): Boolean {
            Toast.makeText(context, message ?: "", Toast.LENGTH_SHORT).show()
            result?.confirm()
            return true
          }

          override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
          ): Boolean {
            // Confirm dialogs automatically allow operation for smooth UX
            result?.confirm()
            return true
          }

          override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            return super.onConsoleMessage(consoleMessage)
          }
        }

        webViewClient = object : WebViewClient() {
          override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            onNavigationStateChanged(canGoBack())
          }

          override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            onNavigationStateChanged(canGoBack())
          }

          override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
          ) {
            super.onReceivedError(view, request, error)
          }

          override fun onRenderProcessGone(
            view: WebView?,
            detail: RenderProcessGoneDetail?
          ): Boolean {
            view?.let {
              it.loadDataWithBaseURL(
                "https://generativelanguage.googleapis.com",
                htmlData,
                "text/html",
                "UTF-8",
                null
              )
            }
            return true
          }
        }

        loadDataWithBaseURL(
          "https://generativelanguage.googleapis.com",
          htmlData,
          "text/html",
          "UTF-8",
          null
        )
        onWebViewCreated(this)
      }
    },
    update = { webView ->
      onNavigationStateChanged(webView.canGoBack())
    }
  )
}

@Composable
fun ExportHtmlDialog(
  context: Context,
  onDismiss: () -> Unit
) {
  val htmlContent = remember {
    try {
      context.assets.open("index.html").bufferedReader().use { it.readText() }
    } catch (e: Exception) {
      "<!-- Error loading index.html: ${e.message} -->"
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Standalone index.html", style = MaterialTheme.typography.titleMedium)
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "The complete single-page web app is packaged inside this APK and also saved at the project root (/index.html). It contains the entire zero-server client with smart auto-rotation on 429 limits, Tailwind CSS, and local vault management.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = "File stats:",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "• Location: /index.html & assets/index.html\n• Size: ~${htmlContent.length / 1024} KB\n• Dependencies: Embedded / CDN\n• Ready to deploy anywhere (Vercel, GitHub Pages, Netlify)",
              style = MaterialTheme.typography.bodySmall,
              fontFamily = FontFamily.Monospace,
              fontSize = 11.sp
            )
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip = ClipData.newPlainText("index.html", htmlContent)
          clipboard.setPrimaryClip(clip)
          Toast.makeText(context, "Full index.html copied to clipboard!", Toast.LENGTH_LONG).show()
          onDismiss()
        },
        modifier = Modifier.testTag("copy_html_button")
      ) {
        Icon(
          imageVector = Icons.Default.ContentCopy,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Copy HTML")
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_SUBJECT, "Gemini Vault - index.html")
            putExtra(Intent.EXTRA_TEXT, htmlContent)
          }
          context.startActivity(Intent.createChooser(shareIntent, "Share index.html"))
          onDismiss()
        },
        modifier = Modifier.testTag("share_html_button")
      ) {
        Text("Share")
      }
    }
  )
}
