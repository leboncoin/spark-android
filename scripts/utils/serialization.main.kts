@file:DependsOn(
    "org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.10.0",
)

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray


fun JsonElement.prettyPrint() = Json { prettyPrint = true }.encodeToString<JsonElement>(value = this, serializer = JsonElement.serializer())
fun Collection<String>.toJsonArray() = buildJsonArray { this@toJsonArray.forEach { add(it) } }
