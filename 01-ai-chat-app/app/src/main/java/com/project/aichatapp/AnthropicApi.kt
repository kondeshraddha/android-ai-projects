package com.project.aichatapp

class AnthropicApi {
}import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnthropicApi {

    @Headers(
        "content-type: application/json",
        "anthropic-version: 2023-06-01",
        "x-api-key: YOUR_API_KEY_HERE"
    )
    @POST("v1/messages")
    suspend fun sendMessage(@Body request: ChatRequest): Response<ChatResponse>
}

data class ChatRequest(
    val model: String = "claude-haiku-4-5-20251001",
    val max_tokens: Int = 1024,
    val messages: List<ApiMessage>
)

data class ApiMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val content: List<ContentBlock>
)

data class ContentBlock(
    val text: String
)