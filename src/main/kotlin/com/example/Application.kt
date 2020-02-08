package com.example

import com.example.application.commands.CreateProductCommand
import com.example.application.queries.GetProductQuery
import com.example.infra.diModule
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.getOrFail
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

/*@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    val client = HttpClient(Apache) {
    }

}*/

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    //region Http Configurations
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(AutoHeadResponse)
    //endregion

    val bus: CommandBus by KoinJavaComponent.inject(CommandBus::class.java)

    startKoin {
        modules(listOf(diModule))
    }

    //region Routings
    routing {
        route("/products") {

            get("/{id}") {
                val productId = call.parameters.getOrFail("id")
                val product = bus.executeQueryAsync(GetProductQuery(productId))
                call.respond { product }
            }

            post() {
                val command = call.receive<CreateProductCommand>()
                bus.executeCommandAsync(command)
            }
        }
    }
    //endregion
}