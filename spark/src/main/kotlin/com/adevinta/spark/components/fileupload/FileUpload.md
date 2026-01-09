# File upload

File upload components allow users to select, upload, and preview files such as images or documents.

## Components

- `FileUploadSingleButton`: High-level component for single file selection with a button trigger and optional file preview.
- `FileUploadMultipleButton`: High-level component for multiple file selection with a button trigger and optional file previews.
- `FileUploadPattern`: Reusable pattern wrapper that can integrate file upload functionality into any component.
- `PreviewFile`: Visual representation of a single uploaded file, showing icon, name, size and a clear button.

## Pattern API

The file upload functionality is built on a reusable pattern that can be integrated into any component. The pattern handles all file picking logic while allowing you to customize the UI trigger and preview display.

### Basic Usage

```kotlin
// Single file upload
var selectedFile by remember { mutableStateOf<UploadedFile?>(null) }

FileUploadSingleButton(
    onResult = { file -> selectedFile = file },
    label = "Select file",
)

// Multiple file upload with max limit
var selectedFiles by remember { mutableStateOf<List<UploadedFile>>(emptyList()) }

FileUploadMultipleButton(
    onResult = { files -> selectedFiles = files },
    label = "Select files",
    maxFiles = 5, // Optional: limit to 5 files
)
```

### Using the Pattern with Custom Components

The `FileUploadPattern` can wrap any component to add file upload functionality:

```kotlin
val pattern = rememberFileUploadPattern(
    type = FileUploadType.Image(ImageSource.Both),
    mode = FileUploadMode.Single,
    onFilesSelected = { files -> /* handle files */ }
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

## File Types

You can specify what type of files to select:

```kotlin
// Images only (with camera/gallery selection)
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select image",
    type = FileUploadType.Image(
        source = ImageSource.Both // Shows dialog to choose camera or gallery
    )
)

// Images only (camera only)
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Take photo",
    type = FileUploadType.Image(source = ImageSource.Camera)
)

// Images only (gallery only)
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Choose from gallery",
    type = FileUploadType.Image(source = ImageSource.Gallery)
)

// Videos only
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select video",
    type = FileUploadType.Video
)

// Images and videos
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select media",
    type = FileUploadType.ImageAndVideo
)

// Generic files with extension filter
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select PDF",
    type = FileUploadType.File(extensions = setOf("pdf"))
)
```

## Custom Preview

You can customize how selected files are displayed:

```kotlin
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Upload",
    preview = { file ->
        file?.let {
            // Custom preview layout
            Row {
                Icon(SparkIcons.Check)
                Text(it.name)
            }
        }
    }
)

FileUploadMultipleButton(
    onResult = { /* handle */ },
    label = "Upload multiple",
    preview = { files ->
        // Custom preview for multiple files
        files.forEach { file ->
            PreviewFile(
                file = file,
                onClear = { /* remove file */ }
            )
        }
    }
)
```

## Multiple File Selection

For multiple file selection, you can optionally limit the maximum number of files:

```kotlin
FileUploadMultipleButton(
    onResult = { files -> /* handle */ },
    label = "Select up to 5 files",
    maxFiles = 5, // Limit to 5 files
)
```

## Low-Level Pattern API

For advanced use cases, you can use the pattern state directly:

```kotlin
val pattern = rememberFileUploadPattern(
    type = FileUploadType.File(),
    mode = FileUploadMode.Multiple(maxFiles = 10),
    onFilesSelected = { files -> /* handle */ }
)

// Access selected files
val files = pattern.selectedFiles

// Launch specific pickers
pattern.launchFilePicker()      // File/gallery picker
pattern.launchCameraPicker()   // Camera picker
pattern.launchGalleryPicker()  // Gallery picker
pattern.launchPicker()          // Smart launcher (handles source selection)
```

## Migration from Old API

If you were using the old `FileUploadSingleButton` with the `camera` parameter:

```kotlin
// Old API (deprecated)
FileUploadSingleButton(
    onResult = { /* handle */ },
    mode = FileKitMode.Single,
    label = "Upload",
    camera = true
)

// New API
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Upload",
    type = FileUploadType.Image(source = ImageSource.Camera)
)
```
