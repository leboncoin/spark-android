import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile
import kotlin.io.path.walk

fun Path.files() = walk().filter { it.isRegularFile() }
fun Path.files(predicate: (Path) -> Boolean) = files().filter(predicate)
fun Path.filesWithExtension(vararg extensions: String) = files { it.extension in extensions }

fun Path.parents(includeSelf: Boolean = false) = generateSequence(
    seed = if (includeSelf) absolute() else absolute().parent,
    nextFunction = Path::getParent,
)
