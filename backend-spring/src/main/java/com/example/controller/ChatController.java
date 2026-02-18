package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Value("${openai.api-key:}")
    private String openaiApiKey;

    @PostMapping
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String userMessage = request.get("message");

        if (userMessage == null || userMessage.isBlank()) {
            response.put("success", false);
            response.put("message", "Pesan tidak boleh kosong");
            return ResponseEntity.badRequest().body(response);
        }

        if (openaiApiKey == null || openaiApiKey.isBlank()) {
            response.put("success", false);
            response.put("reply", "OpenAI API key belum dikonfigurasi. Tambahkan OPENAI_API_KEY di environment variable.");
            return ResponseEntity.ok(response);
        }

        try {
            String reply = callOpenAI(userMessage);
            response.put("success", true);
            response.put("reply", reply);
        } catch (Exception e) {
            response.put("success", false);
            response.put("reply", "Terjadi kesalahan saat menghubungi OpenAI: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    private String callOpenAI(String message) throws IOException {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + openaiApiKey);
        conn.setDoOutput(true);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);

        String escapedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");

        String body = "{\"model\":\"gpt-3.5-turbo\"," +
                "\"messages\":[{\"role\":\"user\",\"content\":\"" + escapedMessage + "\"}]," +
                "\"max_tokens\":1000}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = conn.getResponseCode();
        if (statusCode != 200) {
            try (Scanner sc = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8)) {
                String errorBody = sc.useDelimiter("\\A").hasNext() ? sc.next() : "";
                throw new IOException("OpenAI API error " + statusCode + ": " + errorBody);
            }
        }

        try (Scanner sc = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
            String responseBody = sc.useDelimiter("\\A").hasNext() ? sc.next() : "";
            return extractContent(responseBody);
        }
    }

    private String extractContent(String json) {
        // Parse "content" field from choices[0].message.content
        int idx = json.indexOf("\"content\"");
        if (idx == -1) return "Tidak ada respons dari AI.";
        int start = json.indexOf("\"", idx + 9) + 1;
        int end = start;
        StringBuilder sb = new StringBuilder();
        while (end < json.length()) {
            char c = json.charAt(end);
            if (c == '\\' && end + 1 < json.length()) {
                char next = json.charAt(end + 1);
                switch (next) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    default: sb.append(next); break;
                }
                end += 2;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
                end++;
            }
        }
        return sb.toString();
    }
}
