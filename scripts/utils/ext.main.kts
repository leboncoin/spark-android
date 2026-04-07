
inline fun <reified T : Any> Any?.cast(): T = this as T

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> = filterValues { it != null } as Map<K, V>

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K?, V>.filterNotNullKeys(): Map<K, V> = filterKeys { it != null } as Map<K, V>

fun String.toKebabCase() = "(?<=[a-zA-Z])[A-Z]".toRegex().replace(this) { "-${it.value}" }.lowercase()
fun String.toSnakeCase() = "(?<=.)[A-Z]".toRegex().replace(this, "_$0").lowercase()
fun String.toPascalCase() = split("_").joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }
