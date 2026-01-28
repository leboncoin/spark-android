# Package com.adevinta.spark.components.fileupload

File upload components allow users to select, upload, and preview files such as images or documents.

## Components

- `FileUpload.Button`: High-level component for multiple file selection with a button trigger and optional file previews. This is the default component for file uploads.
- `FileUpload.ButtonSingleSelect`: High-level component for single file selection with a button trigger and optional file preview.
- `FileUploadPattern`: Reusable pattern wrapper that can integrate file upload functionality into any component.
- `FileUploadList`: Default preview implementation that displays uploaded files in a list with progress bars, error states, and clear buttons.
- `PreviewFile`: Visual representation of a single uploaded file, showing icon, name, size and a clear button.

## Pattern API

The file upload functionality is built on a reusable pattern that can be integrated into any component and aim to allow developers to make their iwn file upload components. The pattern handles all file picking logic while allowing you to customize the UI trigger and preview display.

### Basic Usage

```kotlin
// Single file upload
var selectedFile by remember { mutableStateOf<UploadedFile?>(null) }

FileUpload.ButtonSingleSelect(
    onResult = { file -> selectedFile = file },
    label = "Select file",
)

// Multiple file upload with max limit
var selectedFiles by remember { mutableStateOf<ImmutableList<UploadedFile>>(persistentListOf()) }

FileUpload.Button(
    onResult = { files -> selectedFiles = files },
    label = "Select files",
    maxFiles = 5, // Optional: limit to 5 files
)
```

### Using the Pattern with Custom Components

The `FileUploadPattern` can wrap any component to add file upload functionality:

```kotlin
val pattern = rememberFileUploadPattern(
    type = FileUploadType.Image(ImageSource.Gallery),
    mode = FileUploadMode.Single,
    onFilesSelect = { files -> /* handle files */ }
)

// With a Button
FileUploadPattern(pattern = pattern) { onClick ->
    ButtonFilled(
        onClick = onClick,
        text = "Upload Image"
    )
}

// With a Surface
FileUploadPattern(pattern = pattern) { onClick ->
    Surface(
        onClick = onClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Tap to upload")
    }
}

// With direct launcher access (e.g., in a TextField trailing icon)
TextField(
    trailingIcon = {
        IconButton(onClick = { pattern.launchPicker() }) {
            Icon(SparkIcons.Upload)
        }
    }
)
```

## Default Preview

The `FileUploadList` component provides a standard implementation for displaying uploaded files:

```kotlin
FileUpload.Button(
    onResult = { files -> selectedFiles = files },
    label = "Select files",
)

FileUploadList(
    files = selectedFiles,
    onClearFile = { file -> selectedFiles = selectedFiles.remove(file) },
    onClick = { file -> /* handle file click */ }
)
```

The default preview automatically:

- Shows each file with its icon, name, and size
- Displays progress bars during upload
- Shows error messages with appropriate styling
- Provides clear buttons for removing files
- Supports clickable previews for opening files

## File Types

You can specify what type of files to select:

```kotlin
// Images only (camera only)
FileUpload.ButtonSingleSelect(
    onResult = { /* handle */ },
    label = "Take photo",
    type = FileUploadType.Image(source = ImageSource.Camera)
)

// Images only (gallery only)
FileUpload.ButtonSingleSelect(
    onResult = { /* handle */ },
    label = "Choose from gallery",
    type = FileUploadType.Image(source = ImageSource.Gallery)
)

// Videos only
FileUpload.ButtonSingleSelect(
    onResult = { /* handle */ },
    label = "Select video",
    type = FileUploadType.Video
)

// Images and videos
FileUpload.ButtonSingleSelect(
    onResult = { /* handle */ },
    label = "Select media",
    type = FileUploadType.ImageAndVideo
)

// Generic files with extension filter
FileUpload.ButtonSingleSelect(
    onResult = { /* handle */ },
    label = "Select PDF",
    type = FileUploadType.File(extensions = setOf("pdf"))
)
```

## Custom Preview

You can customize how selected files are displayed by managing state yourself and using `PreviewFile` or `FileUploadList`:

```kotlin
var selectedFile by remember { mutableStateOf<UploadedFile?>(null) }

FileUpload.ButtonSingleSelect(
    onResult = { file -> selectedFile = file },
    label = "Upload",
)

// Custom preview for single file
selectedFile?.let { file ->
    Row {
        Icon(SparkIcons.Check)
        Text(file.name)
    }
}

// Or use PreviewFile
selectedFile?.let { file ->
    PreviewFile(
        file = file,
        onClear = { selectedFile = null }
    )
}
```

For multiple files:

```kotlin
var selectedFiles by remember { mutableStateOf<ImmutableList<UploadedFile>>(persistentListOf()) }

FileUpload.Button(
    onResult = { files -> selectedFiles = files },
    label = "Upload multiple",
)

// Custom preview for multiple files
selectedFiles.forEach { file ->
    PreviewFile(
        file = file,
        onClear = { selectedFiles = selectedFiles.remove(file) }
    )
}
```

## Multiple File Selection

For multiple file selection, you can optionally limit the maximum number of files:

```kotlin
FileUpload.Button(
    onResult = { files -> /* handle */ },
    label = "Select up to 5 files",
    maxFiles = 5, // Limit to 5 files
)
```

## Opening Files

You can open files with the system's default application using `FileUploadDefaults.openFile()`:

```kotlin
PreviewFile(
    file = uploadedFile,
    onClear = { /* remove file */ },
    onClick = {
        // Open file with default application
        FileUploadDefaults.openFile(uploadedFile)
    }
)
```

Note: On Android, if opening files from custom locations (e.g., `FileKit.filesDir` or `FileKit.cacheDir`), you may need to configure FileProvider in your app's AndroidManifest.xml. See [FileKit documentation](https://filekit.mintlify.app/dialogs/open-file) for details.

## PreviewFile Customization

The `PreviewFile` component supports several customization options:

```kotlin
PreviewFile(
    file = uploadedFile,
    onClear = { /* remove file */ },
    modifier = Modifier, // modifier for the component
    progress = { 0.5f }, // determinate progress (0.0 to 1.0)
    isLoading = false, // indeterminate loading state
    errorMessage = null, // error message to display
    enabled = true, // whether the component is enabled
    clearContentDescription = "Remove ${uploadedFile.name}", // accessibility label for the clear/remove file button
    onClick = { /* handle file click */ }, // makes the preview clickable, use null to make the preview read only
    clearIcon = SparkIcons.Close, // customize the clear/remove button icon
)
```

### Loading States

The `PreviewFile` component supports two types of loading indicators:

1. **Determinate Progress**: Use the `progress` parameter to show a progress bar with a specific value (0.0 to 1.0)
2. **Indeterminate Loading**: Use the `isLoading` parameter to show an indeterminate progress bar when the exact progress is unknown

When either `progress` or `isLoading` is active, the clear button is automatically disabled.

## Button Semantics

You can add accessibility labels to the file upload buttons:

```kotlin
FileUpload.Button(
    onResult = { files -> /* handle */ },
    label = "Select files",
    onClickLabel = "Open file picker to select multiple files",
)

FileUpload.ButtonSingleSelect(
    onResult = { file -> /* handle */ },
    label = "Select file",
    onClickLabel = "Open file picker to select a single file",
)
```
