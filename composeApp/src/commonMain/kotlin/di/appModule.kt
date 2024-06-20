package di

import data.remote.FlexiStoreClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import utils.Constant

val appModule = module {
    single {
        HttpClient {
            install(ContentNegotiation){
                json(
                    Json {
                        isLenient = true
                        prettyPrint = true
                    }
                )
            }
            install(Logging){
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                filter { request->
                    request.url.host.contains("192.168.10")
                }
                sanitizeHeader { header-> header == HttpHeaders.Authorization }
            }
            defaultRequest {
                url(Constant.BASE_URL)
            }
        }
    }
    single {FlexiStoreClient(get())}
}